package com.uit.realestate.repository.user;

import com.uit.realestate.domain.user.UserTarget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTargetRepository extends JpaRepository<UserTarget, Long> {

    List<UserTarget> findAllByUserId(Long userId);
}

