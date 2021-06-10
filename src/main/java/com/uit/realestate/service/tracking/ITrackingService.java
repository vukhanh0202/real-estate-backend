package com.uit.realestate.service.tracking;

import org.springframework.stereotype.Service;

@Service
public interface ITrackingService {
    void tracking(Long userId, String ip, Long targetId, Long rating);
}
