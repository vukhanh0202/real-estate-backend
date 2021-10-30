package com.uit.realestate.service.user.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.domain.user.User;
import com.uit.realestate.exception.InvalidException;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.mapper.user.UserMapper;
import com.uit.realestate.payload.user.UpdateUserRequest;
import com.uit.realestate.repository.location.DistrictRepository;
import com.uit.realestate.repository.location.ProvinceRepository;
import com.uit.realestate.repository.user.UserRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.user.IAddUserTargetByTokenService;
import com.uit.realestate.service.user.IUpdateInformationByTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddUserTargetByTokenServiceImpl extends AbstractBaseService<IAddUserTargetByTokenService.Input, Boolean>
        implements IAddUserTargetByTokenService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final DistrictRepository districtRepository;

    private final ProvinceRepository provinceRepository;

    @Override
    public void preExecute(Input input) {
        if (input.getProvinceId() != null && !provinceRepository.existsById(input.getProvinceId())) {
            throw new NotFoundException(messageHelper.getMessage(MessageCode.Province.INVALID));
        }
        if (input.getDistrictId() != null) {
            if (input.getProvinceId() == null) {
                throw new InvalidException(messageHelper.getMessage(MessageCode.District.NOT_HAVE_PROVINCE));
            }
            var district = districtRepository.findById(input.getDistrictId())
                    .orElseThrow(() ->new NotFoundException(messageHelper.getMessage(MessageCode.District.INVALID)));
            if (!Objects.equals(district.getProvince().getId(), input.getProvinceId())) {
                throw new InvalidException(messageHelper.getMessage(MessageCode.District.NOT_HAVE_PROVINCE));
            }
        }
    }

    @Override
    public Boolean doing(Input input) {
        log.info("Add user target to user ID:" + input.getUserId());
        User user = userRepository.findById(input.getUserId())
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.User.NOT_FOUND)));
        user.addTarget(userMapper.toUserTarget(input));
        userRepository.save(user);
        return true;
    }
}
