package com.crud.dto.request;

import com.crud.enums.Position;
import com.crud.enums.Function;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Serdeable
public class UserRequestDTO {

    @NotBlank(message = "O usuario precisa de name")
    @Size(max = 50, message = "O username não pode passar de 50 caracteres")
    @Schema(description = "Nome do usuário", example = "Paulo Asis")
    private String name;

    @Email(message = "É necessário seguir um padrão de formato de email")
    @NotNull(message = "O usuario precisa de email")
    @Size(max = 100, message = "O email não pode passar de 100 caracteres")
    @Schema(description = "Email do usuário", example = "paulo@gmail.com", format = "email")
    private String email;

    @NotNull(message = "O usuario precisa de um cargo")
    @Schema(description = "Cargo do usuário", example = "ESTÁGIARIO")
    private Position position;

    @NotNull(message = "O usuario precisa de uma função")
    @Schema(description = "Função do usuário", example = "PRODUTO")
    private Function function;
}