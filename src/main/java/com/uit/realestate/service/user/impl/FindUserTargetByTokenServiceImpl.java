package com.uit.realestate.service.user.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.dto.user.UserTargetDto;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.mapper.user.UserMapper;
import com.uit.realestate.repository.location.DistrictRepository;
import com.uit.realestate.repository.location.ProvinceRepository;
import com.uit.realestate.repository.user.UserRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.user.IFindUserTargetByTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FindUserTargetByTokenServiceImpl extends AbstractBaseService<Long, List<UserTargetDto>>
        implements IFindUserTargetByTokenService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public List<UserTargetDto> doing(Long userid) {
        var user = userRepository.findById(userid)
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.User.NOT_FOUND)));
        return userMapper.toUserTargetDtoList(user.getUserTargets());
    }
}
