package com.uit.realestate.repository.user;

import com.uit.realestate.constant.enums.user.ERoleType;
import com.uit.realestate.domain.user.Role;
import com.uit.realestate.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, ERoleType> {
}

