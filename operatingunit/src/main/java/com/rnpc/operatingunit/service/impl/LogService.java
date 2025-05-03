package com.rnpc.operatingunit.service.impl;

import com.rnpc.operatingunit.analyzer.controller.DateRangeRequest;
import com.rnpc.operatingunit.enums.LogAffectedEntityType;
import com.rnpc.operatingunit.enums.LogOperationType;
import com.rnpc.operatingunit.model.LogEntity;
import com.rnpc.operatingunit.repository.LogRepository;
import com.rnpc.operatingunit.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {
    private final LogRepository logRepository;
    private final AppUserService appUserService;

    public void createLog(LogAffectedEntityType affectedEntityType, LogOperationType operationType){
        LogEntity logEntity = new LogEntity();
        logEntity.setAffectedEntityType(affectedEntityType);
        logEntity.setOperationType(operationType);
        logEntity.setUsername(appUserService.getCurrentUser().getLogin());
        logRepository.save(logEntity);
    }

    public List<LogEntity> findBetweenDates(DateRangeRequest dateRangeRequest){
        LocalDateTime start = dateRangeRequest.getStartDate().atStartOfDay();
        LocalDateTime end = dateRangeRequest.getEndDate().atTime(23, 59);
        return logRepository.findByDateBetween(start, end);
    }
}
