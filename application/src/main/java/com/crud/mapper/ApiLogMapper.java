package com.crud.mapper;

import com.crud.entities.ApiLog;
import com.crud.enums.CriticidadeLog;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.internal.logging.LogMessage;
import io.micronaut.context.annotation.Mapper;
import lombok.RequiredArgsConstructor;

@Mapper
@RequiredArgsConstructor
public abstract class ApiLogMapper {
    private final ObjectMapper objectMapper;

    public abstract ApiLog toApiLog(CriticidadeLog level, String message, Object requestObj);
}
