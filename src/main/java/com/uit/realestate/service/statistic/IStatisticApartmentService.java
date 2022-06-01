package com.uit.realestate.service.statistic;

import com.uit.realestate.constant.enums.apartment.EApartmentStatus;
import com.uit.realestate.constant.enums.apartment.ETypeApartment;
import com.uit.realestate.constant.enums.statistic.ECriteria;
import com.uit.realestate.constant.enums.statistic.EStatistic;
import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.domain.apartment.ApartmentRating;
import com.uit.realestate.dto.apartment.ApartmentBasicDto;
import com.uit.realestate.dto.statistic.StatisticDto;
import com.uit.realestate.dto.statistic.TotalStatisticDto;
import com.uit.realestate.dto.statistic.UnitDto;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.payload.apartment.SearchApartmentRequest;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.utils.CalculatorUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

@Service
public abstract class IStatisticApartmentService {

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private ApartmentMapper apartmentMapper;

    public abstract StatisticDto executeStatistic(EStatistic statistic, ECriteria criteria, Long userId , String ip, ETypeApartment typeApartment);

    protected List<ApartmentBasicDto> getSuitableApartment(SearchApartmentRequest searchApartmentRequest, Long userId, String ip) {
        searchApartmentRequest.setApartmentStatus(EApartmentStatus.OPEN.name());
        searchApartmentRequest.setUserId(userId == null ? -1 : userId);
        searchApartmentRequest.setIp(ip);
        searchApartmentRequest.setPageable(PageRequest.of(0, 3, JpaSort.unsafe(Sort.Direction.DESC, "(rating)")));
        List<ApartmentRating> result = apartmentRepository
                .findRecommendApartmentByUserIdAndIp(searchApartmentRequest, searchApartmentRequest.getUserId(), searchApartmentRequest.getIp(), searchApartmentRequest.getPageable());
        return apartmentMapper.toApartmentBasicRatingDtoList(result, searchApartmentRequest.getUserId(), searchApartmentRequest.getIp());
    }

    protected TotalStatisticDto displaySummary(List<Apartment> apartments, ETypeApartment typeApartment) {
        TotalStatisticDto totalStatisticDto = new TotalStatisticDto();
        DecimalFormat df = new DecimalFormat("###,###,###");
        int sizeApartmentsDivide = apartments.size();
        if (sizeApartmentsDivide == 0) {
            sizeApartmentsDivide = 1;
        }
        totalStatisticDto.setTotalApartment(df.format(apartments.size()) + " BĐS");
        if (typeApartment.equals(ETypeApartment.BUY)){
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
        }else{
            var totalPrice = (long) apartments.stream().mapToDouble(Apartment::getPriceRent).sum();
            UnitDto unitForTotalPrice = CalculatorUnit.calculatorUnit(totalPrice);
            totalStatisticDto.setTotalPrice(df.format(totalPrice / unitForTotalPrice.getUnit()) + unitForTotalPrice.getUnitCoinStr() + " / tháng");
            UnitDto unitForAveragePrice = CalculatorUnit.calculatorUnit(totalPrice / sizeApartmentsDivide);
            totalStatisticDto.setAveragePrice(df.format((totalPrice / sizeApartmentsDivide) / unitForAveragePrice.getUnit()) + unitForAveragePrice.getUnitCoinStr() + " / tháng");
            var totalArea = (long) apartments.stream().mapToDouble(Apartment::getArea).sum();
            UnitDto unitForTotalSquare = CalculatorUnit.calculatorUnit(totalArea);
            totalStatisticDto.setTotalSquare(df.format(totalArea / unitForTotalSquare.getUnit()) + unitForTotalSquare.getUnitAreaStr());
            UnitDto unitForAverageSquare = CalculatorUnit.calculatorUnit(totalArea / sizeApartmentsDivide);
            totalStatisticDto.setAverageSquare(df.format((totalArea / sizeApartmentsDivide) / unitForAverageSquare.getUnit()) + unitForAverageSquare.getUnitAreaStr());
        }
        return totalStatisticDto;
    }
}
