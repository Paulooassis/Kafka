package com.crud.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.crud.boundary.ApiLogBoundary;
import com.crud.entities.ApiLog;
import com.crud.enums.CriticidadeLog;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@MicronautTest(startApplication = false, transactional = false)
class ApiLogServiceTest {

    @Inject
    ApiLogService apiLogService;

    @Inject
    ApiLogBoundary apiLogBoundary;

    @MockBean(ApiLogBoundary.class)
    ApiLogBoundary apiLogBoundary() {
        return Mockito.mock(ApiLogBoundary.class);
    }

    @BeforeEach
    void setUp() {
        Mockito.reset(apiLogBoundary);
    }

    @Test
    void save_sucesso() {
        // given
        CriticidadeLog level = CriticidadeLog.INFO;
        String message = "Test message";
        Object requestObj = new TestRequest("test", 123);
        ApiLog expectedLog = Mockito.mock(ApiLog.class);

        when(apiLogBoundary.save(level, message, requestObj)).thenReturn(expectedLog);

        ApiLog result = apiLogService.save(level, message, requestObj);

        assertSame(expectedLog, result);
        verify(apiLogBoundary).save(level, message, requestObj);
        verifyNoMoreInteractions(apiLogBoundary);
    }

    @Test
    void save_sucesso_comRequestNull() {
        // given
        CriticidadeLog level = CriticidadeLog.WARN;
        String message = "Warning message";
        ApiLog expectedLog = Mockito.mock(ApiLog.class);

        when(apiLogBoundary.save(level, message, null)).thenReturn(expectedLog);

        ApiLog result = apiLogService.save(level, message, null);

        assertSame(expectedLog, result);
        verify(apiLogBoundary).save(level, message, null);
    }

    @Test
    void save_sucesso_comRequestString() {
        CriticidadeLog level = CriticidadeLog.ERROR;
        String message = "Error message";
        String requestString = "raw request string";
        ApiLog expectedLog = Mockito.mock(ApiLog.class);

        when(apiLogBoundary.save(level, message, requestString)).thenReturn(expectedLog);

        ApiLog result = apiLogService.save(level, message, requestString);

        assertSame(expectedLog, result);
        verify(apiLogBoundary).save(level, message, requestString);
    }

    @Test
    void save_levelNull_deveLancarIllegalArgumentException() {
        String message = "Valid message";
        Object requestObj = new Object();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> apiLogService.save(null, message, requestObj));

        assertEquals("CriticidadeLog não pode ser nulo", ex.getMessage());
        verifyNoInteractions(apiLogBoundary);
    }

    @Test
    void save_messageNull_deveLancarIllegalArgumentException() {
        CriticidadeLog level = CriticidadeLog.INFO;
        Object requestObj = new Object();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> apiLogService.save(level, null, requestObj));

        assertEquals("Mensagem de log não pode ser nula ou vazia", ex.getMessage());
        verifyNoInteractions(apiLogBoundary);
    }

    @Test
    void save_messageVazia_deveLancarIllegalArgumentException() {
        CriticidadeLog level = CriticidadeLog.INFO;
        Object requestObj = new Object();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> apiLogService.save(level, "   ", requestObj));

        assertEquals("Mensagem de log não pode ser nula ou vazia", ex.getMessage());
        verifyNoInteractions(apiLogBoundary);
    }

    @Test
    void save_todosNiveisCriticidade_sucesso() {
        for (CriticidadeLog level : CriticidadeLog.values()) {
            // given
            String message = "Message for " + level;
            Object requestObj = new Object();
            ApiLog expectedLog = Mockito.mock(ApiLog.class);

            when(apiLogBoundary.save(level, message, requestObj)).thenReturn(expectedLog);

            ApiLog result = apiLogService.save(level, message, requestObj);

            assertSame(expectedLog, result);
            verify(apiLogBoundary).save(level, message, requestObj);

            Mockito.reset(apiLogBoundary);
        }
    }

    private static class TestRequest {
        private String name;
        private int value;

        public TestRequest(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() { return name; }
        public int getValue() { return value; }
    }
}