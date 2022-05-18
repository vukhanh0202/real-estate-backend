package com.uit.realestate.repository.tracking;

import com.uit.realestate.domain.TrackingTemporaryChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrackingTemporaryChatRepository extends JpaRepository<TrackingTemporaryChat, Long> {

    Optional<TrackingTemporaryChat> findByKey(String key);
}
