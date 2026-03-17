package com.crud.service;

import com.crud.entities.ApiLog;
import com.crud.entities.protoBuf.LogMessageProto.ApiLogProto;
import com.crud.enums.LogStatus;
import com.crud.repository.ApiLogRepository;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class LogService {

    private final ApiLogRepository logRepository;

    public ApiLog updateLog(ApiLogProto logProto) {

        ApiLog log = logRepository.findById(logProto.getId()).orElseThrow(() -> new RuntimeException("Log not found"));
        log.setId(logProto.getId());
        log.setLogStatus(LogStatus.PROCESSED);
        return logRepository.update(log);
    }
}
