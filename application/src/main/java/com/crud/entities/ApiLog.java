package com.crud.entities;

import com.crud.enums.CriticidadeLog;
import com.crud.enums.LogStatus;
import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedEntity("logs")
@Serdeable
public class ApiLog {

    @Id
    private String id;

    private Instant instante; // momento que foi criado
    private CriticidadeLog logLevel; // criticidade do log
    private LogStatus logStatus; // status do log
    private String message; // explicação sobre o log
    private String request; // payload, passar o dto
}
