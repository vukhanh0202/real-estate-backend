package com.uit.realestate.service.user;

import com.uit.realestate.dto.user.UserTargetDto;
import com.uit.realestate.service.IService;

import java.util.List;

public interface IFindUserTargetByTokenService extends IService<Long, List<UserTargetDto>> {

}
