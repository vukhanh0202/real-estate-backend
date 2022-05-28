package com.uit.realestate.service.statistic.impl;

import com.uit.realestate.constant.AppConstant;
import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.constant.SuitabilityConstant;
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
public class StatisticCityByPriceServiceImpl extends IStatisticApartmentService {

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
            throw new NotFoundException(messageHelper.getMessage(MessageCode.Price.INVALID));
        }
        Double priceFrom = Math.ceil(criteria.getFrom());
        Double priceTo = Math.floor(criteria.getTo());
        Long gap;
        List<Apartment> apartments;
        if (priceTo == -1) {
            gap = Math.round((AppConstant.DEFAULT_MAX_VALUE_PRICE - priceFrom) / 10);
            isGreaterMaxValue = true;
            apartments = apartmentRepository.findAllByApartmentAddressProvinceIdAndStatusAndTypeApartmentAndPriceGreaterThan(statistic.getId(),
                    EApartmentStatus.OPEN, typeApartment, priceFrom);
        } else {
            gap = Math.round((priceTo - priceFrom) / 10);
            apartments = apartmentRepository.findAllByApartmentAddressProvinceIdAndStatusAndTypeApartmentAndPriceBetween(statistic.getId(),
                    EApartmentStatus.OPEN, typeApartment, priceFrom, priceTo);
        }
        UnitDto unitForGap = CalculatorUnit.calculatorUnit(gap);

        Map<Double, Long> map = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            int finalI = i;
            map.put(priceFrom + gap * (i), apartments
                    .stream()
                    .filter(apartment -> apartment.getTotalPrice() >= priceFrom + gap * (finalI - 1) &&
                            (apartment.getTotalPrice() < priceFrom + gap * (finalI)))
                    .count());
        }
        DecimalFormat df = new DecimalFormat("###,###,###");
        StatisticDto result = new StatisticDto();
        result.setLabels(map.keySet().stream().map(item -> df.format(item / unitForGap.getUnit()) + unitForGap.getUnitCoinStr())
                .collect(Collectors.toList()));
        result.getLabels().sort(Comparator.comparing(a -> Long.valueOf(a.replace(",", "").replace("> ","").split(" ")[0])));
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
        search.setTypeApartment(typeApartment.name());
        search.setPriceFrom(priceFrom);
        if (priceTo != -1) {
            search.setAreaTo(priceTo);
        }
        result.setHighLightApartments(this.getSuitableApartment(search, userId, ip));

        TotalStatisticDto totalStatisticDto = new TotalStatisticDto();
        int sizeApartmentsDivide = apartments.size();
        if (sizeApartmentsDivide == 0){
            sizeApartmentsDivide = 1;
        }
        totalStatisticDto.setTotalApartment(df.format(apartments.size()) + " BĐS");
        var totalPrice = (long) apartments.stream().mapToDouble(Apartment::getTotalPrice).sum();
        UnitDto unitForTotalPrice = CalculatorUnit.calculatorUnit(totalPrice);
        totalStatisticDto.setTotalPrice(df.format(totalPrice / unitForTotalPrice.getUnit()) + unitForTotalPrice.getUnitCoinStr());
        UnitDto unitForAveragePrice = CalculatorUnit.calculatorUnit(totalPrice / sizeApartmentsDivide);
        totalStatisticDto.setAveragePrice(df.format((totalPrice / sizeApartmentsDivide) / unitForAveragePrice.getUnit()) + unitForAveragePrice.getUnitCoinStr());
        var totalArea = (long) apartments.stream().mapToDouble(Apartment::getArea).sum();
        UnitDto unitForTotalSquare = CalculatorUnit.calculatorUnit(totalArea);
        totalStatisticDto.setTotalSquare(df.format(totalArea / unitForTotalSquare.getUnit()) + unitForTotalSquare.getUnitAreaStr());
        UnitDto unitForAverageSquare = CalculatorUnit.calculatorUnit(totalArea / sizeApartmentsDivide);
        totalStatisticDto.setAverageSquare(df.format((totalArea / sizeApartmentsDivide) / unitForAverageSquare.getUnit()) + unitForAverageSquare.getUnitAreaStr());
        result.setTotalStatisticDto(totalStatisticDto);
        return result;
    }

}
