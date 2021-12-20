package com.uit.realestate.service.user.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.domain.user.UserTarget;
import com.uit.realestate.exception.InvalidException;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.mapper.user.UserMapper;
import com.uit.realestate.repository.location.DistrictRepository;
import com.uit.realestate.repository.location.ProvinceRepository;
import com.uit.realestate.repository.user.UserRepository;
import com.uit.realestate.repository.user.UserTargetRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.user.IUpdateUserTargetByTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateUserTargetByTokenServiceImpl extends AbstractBaseService<IUpdateUserTargetByTokenService.Input, Boolean>
        implements IUpdateUserTargetByTokenService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final DistrictRepository districtRepository;

    private final ProvinceRepository provinceRepository;

    private final UserTargetRepository userTargetRepository;

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
                    .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.District.INVALID)));
            if (!Objects.equals(district.getProvince().getId(), input.getProvinceId())) {
                throw new InvalidException(messageHelper.getMessage(MessageCode.District.NOT_HAVE_PROVINCE));
            }
        }
    }

    @Override
    public Boolean doing(Input input) {
        log.info("Update user target ID:" + input.getId());

        UserTarget userTarget = userTargetRepository.findById(input.getId())
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.UserTarget.NOT_FOUND)));
        userMapper.updateUserTarget(input, userTarget);
        userTargetRepository.save(userTarget);
        return true;
    }
}
