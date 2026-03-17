package com.crud.kafka;

import com.crud.entities.ApiLog;
import com.crud.entities.protoBuf.LogMessageProto.ApiLogProto;
import com.crud.service.LogService;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KafkaListener(groupId = "log-consumer-group")
@RequiredArgsConstructor
public class LogConsumer {
    private final LogService logService;
     private static final Logger LOG = LoggerFactory.getLogger(LogConsumer.class);

    @Topic("warn-info-topic")
    public void consumerWarnInfoTopic(byte[] data) {
        lerLog(tratarLog(data));
    }

    @Topic("error-topic")
    public void consumerErrorTopic(byte[] data) {
        lerLog(tratarLog(data));
    }

    private ApiLogProto tratarLog(byte[] data){
        try{
            ApiLogProto apiLogProto = ApiLogProto.parseFrom(data);
            return apiLogProto;
        } catch (Exception e) {
            throw new RuntimeException("Error processing log message", e);
        }
    }

    private void lerLog(ApiLogProto apiLogProto) {
        LOG.info("Log recebido ID={} Criticidade={} Status={} Mensagem={}", apiLogProto.getId(), apiLogProto.getLogLevel(), apiLogProto.getLogStatus(), apiLogProto.getMessage());

        ApiLog apiLog = logService.updateLog(apiLogProto);

        LOG.info("Log atualizado ID={} Criticidade={} Status={} Mensagem={}",
                apiLog.getId(), apiLog.getLogLevel(), apiLog.getLogStatus(), apiLog.getMessage());
    }
}
