package com.uit.realestate.service.statistic.impl;

import com.uit.realestate.domain.location.Province;
import com.uit.realestate.dto.statistic.StatisticRankDto;
import com.uit.realestate.repository.location.ProvinceRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.statistic.IStatisticRankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticRankServiceImpl extends AbstractBaseService<Void, StatisticRankDto>
        implements IStatisticRankService {

    private final ProvinceRepository provinceRepository;

    @Override
    public StatisticRankDto doing(Void unused) {
        log.info("Find Statistic Rank");
        DecimalFormat df = new DecimalFormat("###,###,###");
        StatisticRankDto result = new StatisticRankDto();
        List<Province> listTotalPosts = provinceRepository.findAllByApartmentAddressesCountDesc(PageRequest.of(0, 3));
        List<String> totalPosts = listTotalPosts.stream()
                .map(province -> province.getName() + " ("+ df.format(province.getApartmentAddresses().size()) +" BĐS)")
                .collect(Collectors.toList());
        result.setTotalPosts(totalPosts);

        List<Province> listTotalPrices = provinceRepository.findAllByApartmentPrices();
        listTotalPrices.sort((o1, o2) -> {
            var totalO1 = (long) o1.getApartmentAddresses().stream().mapToDouble(item -> item.getApartment().getTotalPrice()).sum();
            var totalO2 = (long) o2.getApartmentAddresses().stream().mapToDouble(item -> item.getApartment().getTotalPrice()).sum();
            if (totalO1 > totalO2){
                return -1;
            }else  if (totalO1 < totalO2) {
                return 1;
            }
            return 0;
        });
        List<String> totalPrices = listTotalPrices.subList(0, 3).stream()
                .map(province -> {
                    Long unit;
                    String unitStr;
                    var total = (long) province.getApartmentAddresses().stream().mapToDouble(item -> item.getApartment().getTotalPrice()).sum();
                    if (total < 1000000L) {
                        unit = 1L;
                        unitStr = " VNĐ";
                    } else if (total < 1000000000L) {
                        unit = 1000000L;
                        unitStr = " Triệu VNĐ";
                    } else if (total < 1000000000000L) {
                        unit = 1000000000L;
                        unitStr = " Tỷ VNĐ";
                    } else {
                        unit = 1000000000000L;
                        unitStr = " Nghìn Tỷ";
                    }
                    return province.getName() + " ("+ df.format(total / unit) + unitStr +")";
                })
                .collect(Collectors.toList());
        result.setTotalPrices(totalPrices);

        listTotalPrices.sort((o1, o2) -> {
            var totalO1 = (long) o1.getApartmentAddresses().stream().mapToDouble(item -> item.getApartment().getArea()).sum();
            var totalO2 = (long) o2.getApartmentAddresses().stream().mapToDouble(item -> item.getApartment().getArea()).sum();
            if (totalO1 > totalO2){
                return -1;
            }else  if (totalO1 < totalO2) {
                return 1;
            }
            return 0;
        });

        List<String> totalAreas = listTotalPrices.subList(0, 3).stream()
                .map(province -> {
                    Long unit;
                    String unitStr;
                    var total = (long) province.getApartmentAddresses().stream().mapToDouble(item -> item.getApartment().getArea()).sum();
                    if (total < 1000000L) {
                        unit = 1L;
                        unitStr = " m2";
                    } else {
                        unit = 1000000L;
                        unitStr = " Triệu m2";
                    }
                    return province.getName() + " ("+ df.format(total / unit) + unitStr +")";
                })
                .collect(Collectors.toList());
        result.setTotalAreas(totalAreas);
        return result;
    }
}
