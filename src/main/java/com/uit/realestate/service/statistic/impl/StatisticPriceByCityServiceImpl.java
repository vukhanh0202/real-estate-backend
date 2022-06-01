package com.uit.realestate.service.statistic.impl;

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
import com.uit.realestate.service.statistic.IStatisticApartmentService;
import com.uit.realestate.utils.CalculatorUnit;
import com.uit.realestate.utils.MessageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StatisticPriceByCityServiceImpl extends IStatisticApartmentService {

    private final MessageHelper messageHelper;

    private final ApartmentRepository apartmentRepository;

    @Override
    public StatisticDto executeStatistic(EStatistic statistic, ECriteria criteria, Long userId, String ip, ETypeApartment typeApartment) {
        if (statistic.getFrom() == null || statistic.getTo() == null) {
            throw new NotFoundException(messageHelper.getMessage(MessageCode.Area.INVALID));
        }
        Double priceFrom = Math.ceil(statistic.getFrom());
        Double priceTo = Math.floor(statistic.getTo());
        List<Apartment> apartments;
        if (priceTo == -1) {
            priceTo = null;
        }
        apartments = apartmentRepository.findAllByStatusAndPriceBetweenAndAreaBetween(EApartmentStatus.OPEN.name(), typeApartment.name(), priceFrom, priceTo, null, null);

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
        SearchApartmentRequest search = new SearchApartmentRequest();
        search.setPriceFrom(priceFrom);
        search.setPriceTo(priceTo);
        result.setHighLightApartments(this.getSuitableApartment(search, userId, ip));

        result.setTotalStatisticDto(displaySummary(apartments, typeApartment));
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

