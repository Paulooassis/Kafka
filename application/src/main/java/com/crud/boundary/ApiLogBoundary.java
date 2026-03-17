package com.crud.boundary;

import com.crud.entities.ApiLog;
import com.crud.entities.protoBuf.LogMessageProto;
import com.crud.entities.protoBuf.LogMessageProto.ApiLogProto;
import com.crud.enums.CriticidadeLog;
import com.crud.enums.*;
import com.crud.kafka.KafkaProducer;
import com.crud.repository.ApiLogRepository;
import io.micronaut.logging.LogLevel;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import com.crud.entities.*;

import java.time.Instant;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class ApiLogBoundary {

    private final ApiLogRepository repo;
    private final KafkaProducer kafkaProducer;

    public ApiLog save(CriticidadeLog level, String message, Object requestObj) {
        ApiLog apiLog = ApiLog.builder()
                .id(UUID.randomUUID().toString())
                .instante(Instant.now())
                .logLevel(level)
                .message(message)
                .logStatus(LogStatus.CREATED)
                .request(this.toJsonSafe(requestObj))
                .build();

        
        ApiLogProto logMessageProto = ApiLogProto.newBuilder()
                .setId(apiLog.getId())
                .setInstante(String.valueOf(apiLog.getInstante()))
                .setLogLevel(LogMessageProto.CriticidadeLogProto.valueOf(apiLog.getLogLevel().name()))
                .setLogStatus(LogMessageProto.LogStatusProto.valueOf(apiLog.getLogStatus().name()))
                .setMessage(apiLog.getMessage())
                .setRequest(apiLog.getRequest())
                .build();

        if(apiLog.getLogLevel() == CriticidadeLog.ERROR){
            kafkaProducer.sendErrorLog(apiLog.getId(), logMessageProto.toByteArray());
        }else if(apiLog.getLogLevel() == CriticidadeLog.WARN || apiLog.getLogLevel() == CriticidadeLog.INFO){
            kafkaProducer.sendWarnOrInfoLog(apiLog.getId(), logMessageProto.toByteArray());
        }else {
            throw new RuntimeException("Erro ao salvar");
        }

        return repo.save(apiLog);
    }

    private String toJsonSafe(Object obj) {
        if (obj == null) return null;
        if (obj instanceof String s) return s;
        try {
            return String.valueOf(obj);
        } catch (Exception e) {
            return "{\"error\":\"Falha ao serializar request\",\"class\":\"" + obj.getClass().getName() + "\"}";
        }
    }
}
