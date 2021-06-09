package com.uit.realestate.service.service.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.domain.user.User;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.mapper.user.UserMapper;
import com.uit.realestate.payload.user.UpdateUserRequest;
import com.uit.realestate.repository.user.UserRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.service.IUpdateInformationByTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UpdateInformationByTokenServiceImpl extends AbstractBaseService<UpdateUserRequest, Boolean>
        implements IUpdateInformationByTokenService<UpdateUserRequest, Boolean> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void preExecute(UpdateUserRequest updateUserRequest) {
        if (userRepository.findById(updateUserRequest.getId()).isEmpty()){
            throw new NotFoundException(messageHelper.getMessage(MessageCode.User.NOT_FOUND));
        }
    }

    @Override
    public Boolean doing(UpdateUserRequest updateUserRequest) {
        log.info("Update information user from user ID");
        User user = userRepository.findById(updateUserRequest.getId()).get();
        userMapper.updateUser(updateUserRequest,user);
        userRepository.save(user);
        return true;
    }
}
