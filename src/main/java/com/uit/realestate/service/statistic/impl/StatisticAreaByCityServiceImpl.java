package com.uit.realestate.service.statistic.impl;

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
public class StatisticAreaByCityServiceImpl extends IStatisticApartmentService {

    private final MessageHelper messageHelper;

    private final ApartmentRepository apartmentRepository;

    private final ApartmentMapper apartmentMapper;

    @Override
    public StatisticDto executeStatistic(EStatistic statistic, ECriteria criteria, Long userId, ETypeApartment typeApartment) {
        if (statistic.getFrom() == null || statistic.getTo() == null) {
            throw new NotFoundException(messageHelper.getMessage(MessageCode.Area.INVALID));
        }
        var areaFrom = Math.ceil(statistic.getFrom());
        var areaTo = Math.floor(statistic.getTo());
        List<Apartment> apartments;
        if (areaTo == -1) {
            apartments = apartmentRepository.findAllByStatusAndTypeApartmentAndAreaGreaterThan(EApartmentStatus.OPEN, typeApartment, areaFrom);
        } else {
            apartments = apartmentRepository.findAllByStatusAndTypeApartmentAndAreaBetween(EApartmentStatus.OPEN, typeApartment, areaFrom, areaTo);
        }

        Map<String, Long> map = new LinkedHashMap<>();
        for (Apartment apartment : apartments) {
            String key = apartment.getApartmentAddress().getProvince().getName();
            if (map.containsKey(key)) {
                Long quantity = map.get(key);
                quantity++;
                map.put(key, quantity);
            } else {
                map.put(key, 1L);
            }

        }
        var sorted_map = new TreeMap<String, Long>(new ValueComparator(map));
        sorted_map.putAll(map);
        List<String> labels = new ArrayList<>(sorted_map.keySet());
        if (labels.size() > 10) {
            labels = labels.subList(0, 10);
        }
        StatisticDto result = new StatisticDto();
        result.setLabels(labels);
        List<Long> data = new ArrayList<>();
        for (String item : sorted_map.keySet()) {
            data.add(map.get(item));
        }
        result.setData(data);

        result.setHighLightApartments(this.getSuitableApartment(apartments, userId));

        DecimalFormat df = new DecimalFormat("###,###,###");
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

    class ValueComparator implements Comparator<String> {
        Map<String, Long> base;

        public ValueComparator(Map<String, Long> base) {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with
        // equals.
        public int compare(String a, String b) {
            if (base.get(a) >= base.get(b)) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }
    }
}

