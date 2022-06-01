package com.uit.realestate.controller;

import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.constant.enums.statistic.ECriteria;
import com.uit.realestate.constant.enums.statistic.EStatistic;
import com.uit.realestate.data.UserPrincipal;
import com.uit.realestate.dto.response.ApiResponse;
import com.uit.realestate.dto.response.FileCaption;
import com.uit.realestate.payload.apartment.RecommendApartmentRequest;
import com.uit.realestate.service.file.DownloadImageService;
import com.uit.realestate.service.file.UploadService;
import com.uit.realestate.service.statistic.IStatisticApartmentFactoryService;
import com.uit.realestate.service.statistic.IStatisticRankService;
import com.uit.realestate.service.statistic.impl.StatisticApartmentFactoryServiceImpl;
import com.uit.realestate.service.statistic.impl.StatisticCityByPriceServiceImpl;
import com.uit.realestate.utils.IPUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

@RestController
@Slf4j
@Api(value = "Statistic APIs")
@RequiredArgsConstructor
public class StatisticController {

    private final IStatisticApartmentFactoryService statisticApartmentFactoryService;

    private final IStatisticRankService statisticRankService;

    /**
     * Search all apartment no pagination
     * @return
     */
    @ApiOperation(value = "Statistic apartment")
    @GetMapping(value = "/public/statistic")
    public ResponseEntity<?> findStatistic(@RequestParam(value = "statistic") EStatistic statistic,
                                           @RequestParam(value = "statistic_city_id", required = false) Long statisticCityId,
                                           @RequestParam(value = "statistic_from", required = false) Double statisticPriceFrom,
                                           @RequestParam(value = "statistic_to", required = false) Double statisticPriceTo,
                                           @RequestParam(value = "criteria") ECriteria criteria,
                                           @RequestParam(value = "criteria_from", required = false) Double criteriaPriceFrom,
                                           @RequestParam(value = "criteria_to", required = false) Double criteriaPriceTo,
                                           @RequestParam(value = "user_id", required = false) Long userId,
                                           @RequestParam(value = "type_apartment", defaultValue = "RENT") ETypeApartment typeApartment,
                                           HttpServletRequest request) {
        statistic.setId(statisticCityId);
        statistic.setFrom(statisticPriceFrom);
        statistic.setTo(statisticPriceTo);
        criteria.setFrom(criteriaPriceFrom);
        criteria.setTo(criteriaPriceTo);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(statisticApartmentFactoryService
                        .execute(new IStatisticApartmentFactoryService.Input(statistic, criteria, userId, IPUtils.getIp(request), typeApartment))));
    }

    /**
     * Search all apartment no pagination
     * @return
     */
    @ApiOperation(value = "Statistic apartment")
    @GetMapping(value = "/public/statistic/rank")
    public ResponseEntity<?> getTopRank() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(statisticRankService.execute()));
    }
}
