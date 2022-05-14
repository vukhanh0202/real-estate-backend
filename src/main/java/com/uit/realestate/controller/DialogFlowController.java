package com.uit.realestate.controller;

import com.uit.realestate.constant.ExtendEventChatBot;
import com.uit.realestate.dialogflow.Chip;
import com.uit.realestate.dialogflow.DialogRequest;
import com.uit.realestate.dialogflow.DialogResponse;
import com.uit.realestate.dialogflow.ItemList;
import com.uit.realestate.dto.apartment.ThumbnailChatDto;
import com.uit.realestate.payload.apartment.ApartmentQueryParam;
import com.uit.realestate.service.apartment.ApartmentService;
import com.uit.realestate.utils.IPUtils;
import com.uit.realestate.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@Slf4j
@Api(value = "ChatBox")
@RequiredArgsConstructor
public class DialogFlowController {

    private final ApartmentService apartmentService;

    @ApiOperation(value = "Search apartment")
    @PostMapping(value = "/public/dialog", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object chatDialogFlow(@RequestBody String rq, HttpServletRequest request) {
        DialogRequest dialogRequest = JsonUtils.unmarshal(rq, DialogRequest.class);
        ApartmentQueryParam pr = ApartmentQueryParam.of(dialogRequest.getQueryResult().getParameters());
        pr.setPage(1);
        pr.setSize(5);
        pr.createPageable();
        pr.setIp(IPUtils.getIp(request));
        pr.setUserId(Long.valueOf(dialogRequest.getOriginalDetectIntentRequest().get("payload").get("userId").toString()));
        String key = pr.generateKey();
        ExtendEventChatBot event = ExtendEventChatBot.of(dialogRequest.getQueryResult().getAction());
        final Instant deadline = Instant.now().plusMillis(4000);
        // No result
        DialogResponse dialogResponse = new DialogResponse();
        System.out.println(event.name());
        if (event.isInitEvent()){
            apartmentService.findAndSaveRecommendApartmentForChatBox(pr, key);
        }
        try{
            while(Instant.now().isBefore(deadline)){
                List<ThumbnailChatDto> result = apartmentService.findApartmentForChat(key);
                if (!result.isEmpty()){
                    dialogResponse.setItemList(result.stream()
                            .map(item -> new ItemList(item.getTitle(), item.getSubtitle(), item.getImage(), item.getLink()))
                            .collect(Collectors.toList()));
                    dialogResponse.setTexts(List.of("Sau đây là 1 số bất động sản phù hợp với bạn"));
                    Map<String, String> values = new HashMap<>();
                    values.put("Xem thêm", "https://google.com");
                    values.put("DS nổi bật", "https://google.com");
                    dialogResponse.setChips(List.of(new Chip(values)));
                    return dialogResponse.convertText();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if (Objects.nonNull(event.getEventNext())){
            return event.generateJsonNextEvent();
        }
        return dialogResponse.noResponseText();
    }
}
