package com.uit.realestate.service.user.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.domain.user.User;
import com.uit.realestate.exception.InvalidException;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.mapper.user.UserMapper;
import com.uit.realestate.payload.user.UpdateUserRequest;
import com.uit.realestate.repository.location.DistrictRepository;
import com.uit.realestate.repository.user.UserRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.user.IUpdateInformationByTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UpdateInformationByTokenServiceImpl extends AbstractBaseService<UpdateUserRequest, Boolean>
        implements IUpdateInformationByTokenService<UpdateUserRequest, Boolean> {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final DistrictRepository districtRepository;

    public UpdateInformationByTokenServiceImpl(UserRepository userRepository, UserMapper userMapper, DistrictRepository districtRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.districtRepository = districtRepository;
    }

    @Override
    public void preExecute(UpdateUserRequest updateUserRequest) {
        User user = userRepository.findById(updateUserRequest.getId()).orElseThrow(() ->
                new NotFoundException(messageHelper.getMessage(MessageCode.User.NOT_FOUND)));
        if (updateUserRequest.getAddress() != null) {
            Long districtId = updateUserRequest.getAddress().getDistrictId();
            Long provinceId = updateUserRequest.getAddress().getProvinceId();
            if (districtId == null && user.getUserAddress().getDistrict() != null) {
                districtId = user.getUserAddress().getDistrict().getId();
            }
            if (provinceId == null && user.getUserAddress().getProvince() != null) {
                provinceId = user.getUserAddress().getProvince().getId();
            }
            if (districtId != null && provinceId != null) {
                if (!districtRepository.findById(districtId).get().getProvince().getId().equals(provinceId)) {
                    throw new InvalidException(messageHelper.getMessage(MessageCode.Address.INVALID));
                }
            }
        }
    }

    @Override
    public Boolean doing(UpdateUserRequest updateUserRequest) {
        log.info("Update information user from user ID");
        User user = userRepository.findById(updateUserRequest.getId()).get();
        userMapper.updateUser(updateUserRequest, user);
        userRepository.save(user);
        return true;
    }
}
