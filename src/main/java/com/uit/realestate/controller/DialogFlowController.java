package com.uit.realestate.controller;

import com.uit.realestate.constant.ExtendEventChatBot;
import com.uit.realestate.constant.enums.EScraper;
import com.uit.realestate.dialogflow.Chip;
import com.uit.realestate.dialogflow.DialogRequest;
import com.uit.realestate.dialogflow.DialogResponse;
import com.uit.realestate.dialogflow.ItemList;
import com.uit.realestate.dto.apartment.ThumbnailChatDto;
import com.uit.realestate.dto.response.ApiResponse;
import com.uit.realestate.payload.apartment.ApartmentQueryParam;
import com.uit.realestate.service.apartment.ApartmentService;
import com.uit.realestate.service.scraper.ScraperServiceFactory;
import com.uit.realestate.utils.IPUtils;
import com.uit.realestate.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@Api(value = "ChatBox")
@RequiredArgsConstructor
public class DialogFlowController {

    private final ApartmentService apartmentService;

    @ApiOperation(value = "Search apartment")
    @PostMapping(value = "/public/dialog")
    public Object chatDialogFlow(@RequestBody String rq, HttpServletRequest request) {
        DialogRequest dialogRequest = JsonUtils.unmarshal(rq, DialogRequest.class);
        ApartmentQueryParam pr = ApartmentQueryParam.of(dialogRequest.getQueryResult().getParameters());
        pr.setPage(1);
        pr.setSize(5);
        pr.createPageable();
        pr.setIp(IPUtils.getIp(request));
//        pr.setUserId(Long.valueOf(dialogRequest.getOriginalDetectIntentRequest().get("payload").get("userId").toString()));
        pr.setUserId(-1L);
        String key = pr.generateKey();
        ExtendEventChatBot event = ExtendEventChatBot.of(dialogRequest.getQueryResult().getAction());
        final Instant deadline = Instant.now().plusMillis(3000);
        System.out.println(event.name());
        // No result
        DialogResponse dialogResponse = new DialogResponse();
        dialogResponse.setItemList(List.of(new ItemList("List item 1 title", "List item 1 subtitle", "http://localhost:8080/api/public/image/apartment/1_2022-04-17T03-58-21-90760989.jpg", "https://google.com")));
        Map<String, String> values = new HashMap<>();
        values.put("Case Studies","https://cloud.google.com/dialogflow/case-studies");
        dialogResponse.setChips(List.of(new Chip(values)));
        if (event.isInitEvent()){
            apartmentService.findAndSaveRecommendApartmentForChatBox(pr, key);
        }
        try{
            while(Instant.now().isBefore(deadline)){
                System.out.println(Instant.now());
                List<ThumbnailChatDto> result = apartmentService.findApartmentForChat(key);
                if (!result.isEmpty()){
                    System.out.println("Success:" + event.name());
                    System.out.println(dialogResponse.convertText());
                    return dialogResponse.convertText();
                }
                TimeUnit.SECONDS.sleep(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if (Objects.nonNull(event.getEventNext())){
            return event.generateJsonNextEvent();
        }


        return dialogResponse.convertText();
    }
}
