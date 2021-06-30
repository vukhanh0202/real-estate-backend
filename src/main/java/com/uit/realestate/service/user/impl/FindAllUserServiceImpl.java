package com.uit.realestate.service.user.impl;

import com.uit.realestate.domain.user.User;
import com.uit.realestate.dto.response.PaginationResponse;
import com.uit.realestate.dto.user.UserDto;
import com.uit.realestate.mapper.user.UserMapper;
import com.uit.realestate.repository.user.UserRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.user.IFindAllUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FindAllUserServiceImpl extends AbstractBaseService<IFindAllUserService.Input, PaginationResponse<UserDto>>
        implements IFindAllUserService<IFindAllUserService.Input, PaginationResponse<UserDto>> {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public FindAllUserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public PaginationResponse<UserDto> doing(Input input) {
        log.info("Find all User");
        Page<User> result = userRepository.findByUsernameContainingOrFullNameContainingOrEmailContaining(input.getSearch(),
                input.getSearch(), input.getSearch(), input.getPageable());
        return new PaginationResponse(
                result.getTotalElements()
                , result.getNumberOfElements()
                , result.getNumber() + 1
                , userMapper.toUserDtoList(result.getContent()));
    }
}
