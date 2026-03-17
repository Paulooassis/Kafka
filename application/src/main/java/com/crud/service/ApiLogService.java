package com.crud.service;

import com.crud.boundary.ApiLogBoundary;
import com.crud.entities.ApiLog;
import com.crud.enums.CriticidadeLog;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class ApiLogService {

    private final ApiLogBoundary apiLogBoundary;

    public ApiLog save(CriticidadeLog level, String message, Object requestObj) {
        if (level == null) {
            throw new IllegalArgumentException("CriticidadeLog não pode ser nulo");
        }

        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Mensagem de log não pode ser nula ou vazia");
        }

        return apiLogBoundary.save(level, message, requestObj);
    }
}
