package com.uit.realestate.service.apartment.impl;

import com.uit.realestate.domain.apartment.Apartment;
import com.uit.realestate.dto.apartment.ApartmentDto;
import com.uit.realestate.dto.response.PaginationResponse;
import com.uit.realestate.mapper.apartment.ApartmentMapper;
import com.uit.realestate.repository.apartment.ApartmentRepository;
import com.uit.realestate.repository.location.DistrictRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.apartment.ISearchApartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SearchApartmentServiceImpl extends AbstractBaseService<ISearchApartmentService.Input, PaginationResponse<ApartmentDto>>
        implements ISearchApartmentService<ISearchApartmentService.Input, PaginationResponse<ApartmentDto>> {

    @Autowired
    ApartmentMapper apartmentMapper;

    @Autowired
    ApartmentRepository apartmentRepository;

    @Override
    public void preExecute(Input input) {
        super.preExecute(input);
    }

    @Override
    public PaginationResponse<ApartmentDto> doing(Input input) {
        log.info("Search Apartment");
        Page<Apartment> result = apartmentRepository.findAll(getApartSpecification(input), input.getPageable());
        return new PaginationResponse(
                result.getTotalElements()
                , result.getNumberOfElements()
                , result.getNumber() + 1
                , apartmentMapper.toApartmentPreviewDtoList(result.getContent(), input.getUserId()));
    }

    private Specification<Apartment> getApartSpecification(ISearchApartmentService.Input input) {
        return (Specification<Apartment>) (root, query, builder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (input.getDistrictId() != null)
                predicateList.add(builder.equal(root.get("apartmentAddress").get("district").get("id"), input.getDistrictId()));
            if (input.getProvinceId() != null)
                predicateList.add(builder.equal(root.get("apartmentAddress").get("province").get("id"), input.getProvinceId()));
            if (input.getPriceFrom() != null)
                predicateList.add(builder.greaterThanOrEqualTo(root.get("totalPrice"), input.getPriceFrom()));
            if (input.getPriceTo() != null)
                predicateList.add(builder.lessThanOrEqualTo(root.get("totalPrice"), input.getPriceTo()));
            if (input.getAreaFrom() != null)
                predicateList.add(builder.greaterThanOrEqualTo(root.get("area"), input.getAreaFrom()));
            if (input.getAreaTo() != null)
                predicateList.add(builder.lessThanOrEqualTo(root.get("area"), input.getAreaTo()));
            if (input.getCategoryId() != null)
                predicateList.add(builder.equal(root.get("category").get("id"), input.getCategoryId()));
            if (input.getTypeApartment() != null)
                predicateList.add(builder.equal(root.get("typeApartment"), input.getTypeApartment()));
            if (input.getApartmentStatus() != null)
                predicateList.add(builder.equal(root.get("status"), input.getApartmentStatus()));
            if (!input.getSearch().equals(""))
                predicateList.add(builder.like(builder.lower(root.get("title")), "%" + input.getSearch().toLowerCase() + "%"));
            return builder.and(predicateList.toArray(new Predicate[0]));
        };
    }
}
