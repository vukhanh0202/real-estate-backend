package com.uit.realestate.service.service.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.dto.user.UserDto;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.mapper.user.UserMapper;
import com.uit.realestate.repository.user.UserRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.service.IFindUserByIdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FindUserByIdServiceImpl extends AbstractBaseService<Long, UserDto>
        implements IFindUserByIdService<Long, UserDto> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void preExecute(Long userId) {
        if (userRepository.findById(userId).isEmpty()){
            throw new NotFoundException(messageHelper.getMessage(MessageCode.User.NOT_FOUND));
        }
    }

    @Override
    public UserDto doing(Long userId) {
        log.info("Find information user from user ID");
        return userMapper.toUserDto(userRepository.findById(userId).get());
    }
}
