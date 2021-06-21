package com.uit.realestate.service.auth;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.constant.enums.user.ERoleType;
import com.uit.realestate.domain.user.User;
import com.uit.realestate.exception.InvalidException;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.mapper.user.UserMapper;
import com.uit.realestate.payload.auth.ChangePasswordRequest;
import com.uit.realestate.payload.auth.NewAccountRequest;
import com.uit.realestate.repository.user.RoleRepository;
import com.uit.realestate.repository.user.UserRepository;
import com.uit.realestate.utils.MessageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MessageHelper messageHelper;

    @Autowired
    private RoleRepository roleRepository;

    public Boolean register(NewAccountRequest newAccountRequest) {
        if (userRepository.findByUsername(newAccountRequest.getUsername()).isPresent()) {
            throw new InvalidException(messageHelper.getMessage(MessageCode.User.EXIST));
        }
        String pwdBcrypt = BCrypt.hashpw(newAccountRequest.getPassword(), BCrypt.gensalt(10));
        User user = new User();
        user.setUsername(newAccountRequest.getUsername());
        user.setPassword(pwdBcrypt);
        user.setRole(roleRepository.findById(ERoleType.USER).get());
        userRepository.save(user);
        return true;
    }


    public Boolean changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findById(changePasswordRequest.getId())
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.User.NOT_FOUND)));
        BCryptPasswordEncoder b = new BCryptPasswordEncoder();
        if (!b.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new InvalidException(messageHelper.getMessage(MessageCode.User.USER_WRONG));
        }
        String pwdBcrypt = BCrypt.hashpw(changePasswordRequest.getNewPassword(), BCrypt.gensalt(10));
        user.setPassword(pwdBcrypt);
        userRepository.save(user);
        return true;
    }
}
