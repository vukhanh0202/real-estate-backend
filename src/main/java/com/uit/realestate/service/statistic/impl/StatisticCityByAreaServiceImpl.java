package com.uit.realestate.service.statistic.impl;

import com.uit.realestate.constant.AppConstant;
import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.constant.enums.statistic.ECriteria;
import com.uit.realestate.constant.enums.statistic.EStatistic;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.dto.apartment.ApartmentBasicDto;
import com.uit.realestate.dto.statistic.StatisticDto;
import com.uit.realestate.dto.statistic.TotalStatisticDto;
import com.uit.realestate.dto.statistic.UnitDto;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.payload.apartment.SearchApartmentRequest;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.repository.location.ProvinceRepository;
import com.uit.realestate.service.statistic.IStatisticApartmentService;
import com.uit.realestate.utils.CalculatorUnit;
import com.uit.realestate.utils.MessageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticCityByAreaServiceImpl extends IStatisticApartmentService {

    private final ProvinceRepository provinceRepository;

    private final MessageHelper messageHelper;

    private final ApartmentRepository apartmentRepository;

    private final ApartmentMapper apartmentMapper;

    @Override
    public StatisticDto executeStatistic(EStatistic statistic, ECriteria criteria, Long userId, String ip, ETypeApartment typeApartment) {
        boolean isGreaterMaxValue = false;
        if (statistic.getId() == null || !provinceRepository.existsById(statistic.getId())) {
            throw new NotFoundException(messageHelper.getMessage(MessageCode.Province.INVALID));
        }
        if (criteria.getFrom() == null || criteria.getTo() == null) {
            throw new NotFoundException(messageHelper.getMessage(MessageCode.Area.INVALID));
        }
        Double areaFrom = Math.ceil(criteria.getFrom());
        Double areaTo = Math.floor(criteria.getTo());
        Long gap;
        List<Apartment> apartments;
        if (areaTo == -1) {
            gap = Math.round((AppConstant.DEFAULT_MAX_VALUE_AREA - areaFrom) / 10);
            isGreaterMaxValue = true;
            areaTo = null;
        } else {
            gap = Math.round((areaTo - areaFrom) / 10);
        }
        apartments = apartmentRepository.findAllByStatusAndDistrictAndPriceBetweenAndAreaBetween(EApartmentStatus.OPEN.name(), statistic.getId(), typeApartment.name(), null, null, areaFrom, areaTo);

        UnitDto unitForGap = CalculatorUnit.calculatorUnit(gap);

        Map<Double, Long> map = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            int finalI = i;
            map.put(areaFrom + gap * (i), apartments
                    .stream()
                    .filter(apartment -> apartment.getArea() >= areaFrom + gap * (finalI - 1) &&
                            (apartment.getArea() < areaFrom + gap * (finalI)))
                    .count());
        }
        DecimalFormat df = new DecimalFormat("###,###,###");
        StatisticDto result = new StatisticDto();
        result.setLabels(map.keySet().stream().map(item -> df.format(item / unitForGap.getUnit()) + unitForGap.getUnitAreaStr())
                .collect(Collectors.toList()));
        result.getLabels().sort(Comparator.comparing(a -> Long.valueOf(a.replace(",", "").replace("> ", "").split(" ")[0])));
        if (isGreaterMaxValue) {
            List<String> newLabels = result.getLabels();
            String lastLabel = newLabels.get(newLabels.size() - 1);
            newLabels.set(newLabels.size() - 1, "> " + lastLabel);
            result.setLabels(newLabels);
        }
        List<Long> data = new ArrayList<>();
        for (Double item : map.keySet()) {
            data.add(map.get(item));
        }
        result.setData(data);
        SearchApartmentRequest search = new SearchApartmentRequest();
        search.setAreaFrom(areaFrom);
        search.setAreaTo(areaTo);
        search.setTypeApartment(typeApartment.name());
        result.setHighLightApartments(this.getSuitableApartment(search, userId, ip));

        result.setTotalStatisticDto(displaySummary(apartments, typeApartment));
        return result;
    }

}
