package com.uit.realestate.service.user.impl;

import com.uit.realestate.constant.MessageCode;
import com.uit.realestate.domain.user.UserTarget;
import com.uit.realestate.exception.NotFoundException;
import com.uit.realestate.repository.user.UserTargetRepository;
import com.uit.realestate.service.AbstractBaseService;
import com.uit.realestate.service.user.IRemoveUserTargetByTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RemoveUserTargetByTokenServiceImpl extends AbstractBaseService<Long, Boolean>
        implements IRemoveUserTargetByTokenService {

    private final UserTargetRepository userTargetRepository;

    @Override
    public Boolean doing(Long id) {
        log.info("Remove user target "+ id);
        UserTarget userTarget = userTargetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageHelper.getMessage(MessageCode.UserTarget.NOT_FOUND)));
        userTargetRepository.delete(userTarget);
        return true;
    }
}
