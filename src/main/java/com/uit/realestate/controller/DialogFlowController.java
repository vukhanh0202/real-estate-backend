package com.uit.realestate.controller;

import com.uit.realestate.constant.ExtendEventChatBot;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.dialogflow.*;
import com.uit.realestate.dto.apartment.ThumbnailChatDto;
import com.uit.realestate.payload.apartment.ApartmentQueryParam;
import com.uit.realestate.service.AsyncService;
import com.uit.realestate.service.apartment.ApartmentService;
import com.uit.realestate.utils.IPUtils;
import com.uit.realestate.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Slf4j
@Api(value = "ChatBox")
@RequiredArgsConstructor
public class DialogFlowController {

    @Value("${host-fe}")
    private String hostFE;

    private final ApartmentService apartmentService;
    private final AsyncService asyncService;

    @ApiOperation(value = "Search apartment")
    @PostMapping(value = "/public/dialog", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object chatDialogFlow(@RequestBody String rq, HttpServletRequest request) {
        DialogRequest dialogRequest = JsonUtils.unmarshal(rq, DialogRequest.class);
        List<Map<String, Object>> mapList = new ArrayList<>();
        mapList.add(dialogRequest.getQueryResult().getParameters());
        mapList.addAll(JsonUtils.jsonArrayToList(JsonUtils.marshal(dialogRequest.getQueryResult().getOutputContexts()), OutputContext.class)
                .stream().map(OutputContext::getParameters).collect(Collectors.toList()));
        Map<String, Object> param = mapList.stream().filter(item -> !item.isEmpty()).findFirst().get();
        ApartmentQueryParam pr = ApartmentQueryParam.of(param);
        pr.setPage(1);
        pr.setSize(5);
        pr.createPageable();
        pr.setIp(IPUtils.getIp(request));
        pr.setUserId(Long.valueOf(dialogRequest.getOriginalDetectIntentRequest().get("payload").get("userId").toString()));
        String key = pr.generateKey();
        ExtendEventChatBot event = ExtendEventChatBot.of(dialogRequest.getQueryResult().getAction());
        System.out.println(key);
        final Instant deadline = Instant.now().plusMillis(4000);
        DialogResponse dialogResponse = new DialogResponse();
        if (event.isInitEvent()) {
            apartmentService.findAndSaveRecommendApartmentForChatBox(pr, key);
        }
        try {
            while (Instant.now().isBefore(deadline)) {
                List<ThumbnailChatDto> result = apartmentService.findApartmentForChat(key, pr.getUserId());
                if (!result.isEmpty()) {
                    dialogResponse.setItemList(result.stream()
                            .map(item -> new ItemList(item.getTitle(), item.getSubtitle(), item.getImage(), item.getLink()))
                            .collect(Collectors.toList()));
                    dialogResponse.setTexts(List.of("Sau đây là 1 số bất động sản phù hợp với bạn" + asyncService.generateTitle(pr)));
                    Map<String, String> values = new HashMap<>();
                    String type;
                    if (pr.getType() == null || ETypeApartment.valueOf(pr.getType()).equals(ETypeApartment.BUY)) {
                        type = "/nha-dat-ban";
                    } else {
                        type = "/nha-dat-thue";
                    }
                    values.put("Xem thêm", hostFE + type + "?" + pr.generateSearch());
                    values.put("Biểu đồ thống kê", hostFE + "/thong-ke");
                    dialogResponse.setChips(List.of(new Chip(values)));
                    return dialogResponse.convertText();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Objects.nonNull(event.getEventNext())) {
            return event.generateJsonNextEvent(JsonUtils.marshal(param));
        }
        return dialogResponse.noResponseText();
    }
}
