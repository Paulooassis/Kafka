package com.crud.controllers;

import com.crud.dto.request.UserRequestDTO;
import com.crud.dto.response.UserResponseDTO;
import com.crud.service.UserService;
import io.micronaut.data.model.Page;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller("/user")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Operações CRUD para gerenciamento de usuários")
public class UserController {

    private final UserService userService;

    @Post
    @Operation(
            summary = "Criar usuário",
            description = "Cria um novo usuário no sistema. Valida email e telefone únicos."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Usuário criado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos fornecidos - validação de constraints"
    )
    @ApiResponse(
            responseCode = "409",
            description = "Conflito - Email ou telefone já cadastrados"
    )
    public HttpResponse<UserResponseDTO> createUser(@Body @Valid UserRequestDTO createUserDTO) {
        return HttpResponse.created(userService.createUsuario(createUserDTO));
    }

    @Get("/{id}")
    @Operation(
            summary = "Buscar usuário por ID",
            description = "Recupera os dados de um usuário específico pelo seu ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Usuário encontrado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Usuário não encontrado"
    )
    public HttpResponse<UserResponseDTO> getUser(@Parameter(description = "ID do usuário", example = "1") @PathVariable Long id) {
        return HttpResponse.ok(userService.viewUsuario(id));
    }

    @Get
    @Operation(
            summary = "Listar todos os usuários",
            description = "Recupera uma lista com todos os usuários cadastrados no sistema"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de usuários recuperada com sucesso",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserResponseDTO.class)))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Nenhum usuário encontrado no sistema"
    )
    public HttpResponse<Page<UserResponseDTO>> getAllUsersPaginado(
            @QueryValue(defaultValue = "0") int page,
            @QueryValue(defaultValue = "10") int size,
            @QueryValue(defaultValue = "") String position,
            @QueryValue(defaultValue = "") String function) {

        return HttpResponse.ok(userService.viewAllUsers(page, size, position, function));
    }

//    @Get
//    @Operation(
//            summary = "Listar todos os usuários",
//            description = "Recupera uma lista com todos os usuários cadastrados no sistema"
//    )
//    @ApiResponse(
//            responseCode = "200",
//            description = "Lista de usuários recuperada com sucesso",
//            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDTO.class)))
//    )
//    @ApiResponse(
//            responseCode = "404",
//            description = "Nenhum usuário encontrado no sistema"
//    )
//    public HttpResponse<List<UsuarioResponseDTO>> getAllUsers() {
//        return HttpResponse.ok(usuarioService.viewAllUsuarios());
//    }

    @Put("/{id}")
    @Operation(
            summary = "Atualizar usuário",
            description = "Atualiza os dados de um usuário existente. Valida unicidade do telefone."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Usuário atualizado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos fornecidos - validação de constraints"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Usuário não encontrado"
    )
    @ApiResponse(
            responseCode = "409",
            description = "Conflito - Telefone já cadastrado em outro usuário"
    )
    public HttpResponse<UserResponseDTO> updateUser(
            @Parameter(description = "ID do usuário", example = "1") @PathVariable Long id,
            @Body @Valid UserRequestDTO userRequestDTO) {
        return HttpResponse.ok(userService.updateUsuario(id, userRequestDTO));
    }

    @Delete("/{id}")
    @Operation(
            summary = "Excluir usuário",
            description = "Remove um usuário do sistema pelo seu ID"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Usuário excluído com sucesso"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Usuário não encontrado"
    )
    public HttpResponse<Void> deleteUser(@Parameter(description = "ID do usuário", example = "1") @PathVariable Long id) {
        userService.deleteUsuario(id);
        return HttpResponse.noContent();
    }
}