package com.crud.dto.response;

import com.crud.enums.Position;
import com.crud.enums.Function;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Serdeable
public class UserResponseDTO {
    @Schema(description = "ID do usuário", example = "12")
    private Long id;

    @Schema(description = "Nome do usuário", example = "Paulo Asis")
    private String name;

    @Schema(description = "Email do usuário", example = "paulo@gmail.com", format = "email")
    private String email;

    @Schema(description = "Cargo do usuário", example = "ESTAGIARIO")
    private Position position;

    @Schema(description = "Função do usuário", example = "PRODUTO")
    private Function function;
}
