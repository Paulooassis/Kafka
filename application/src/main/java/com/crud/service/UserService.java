package com.crud.service;

import com.crud.boundary.UserBoundary;
import com.crud.dto.request.UserRequestDTO;
import com.crud.dto.response.UserResponseDTO;
import com.crud.enums.CriticidadeLog;
import io.micronaut.data.model.Page;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class UserService {

    private final Validator validator;
    private final ApiLogService apiLogService;
    private final UserBoundary userBoundary;

    public UserResponseDTO createUsuario(UserRequestDTO userRequestDTO) {
        this.userValidate(userRequestDTO);

        apiLogService.save(CriticidadeLog.INFO, "Usuario criado com sucesso", userRequestDTO);
        return userBoundary.save(userRequestDTO);
    }

    public UserResponseDTO viewUsuario(long id){
        this.userNotFoundValidation(id);
        apiLogService.save(CriticidadeLog.INFO, "Usuario com id " + id + " visualizado com sucesso.", id);
        return userBoundary.viewUser(id);
    }

//    public List<UsuarioResponseDTO> viewAllUsuarios(){
//        if(userBoundary.viewAll().isEmpty()) {
//            apiLogService.save(CriticidadeLog.INFO, "Nenhum usuario encontrado ao listar todos os usuarios.", "viewAllUsuarios");
//            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Nenhum usuario encontrado");
//        }
//        apiLogService.save(CriticidadeLog.INFO, "Lista de usuarios visualizada com sucesso. Total: " + userBoundary.viewAll().size(), "viewAllUsuarios");
//        return userBoundary.viewAll();
//    }

    public Page<UserResponseDTO> viewAllUsers(int page, int size, String position, String function){
        if(userBoundary.viewAllUsers(page, size, position, function) == null){
            apiLogService.save(CriticidadeLog.INFO, "Nenhum usuario encontrado ao listar todos os usuarios.", "viewAllUsuarios");
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Nenhum usuario encontrado");
        }
        apiLogService.save(CriticidadeLog.INFO, "Lista de usuarios visualizada com sucesso. Total: " + userBoundary.viewAllUsers(page, size, position, function).getSize(), "viewAllUsuarios");
        return userBoundary.viewAllUsers(page, size, position, function);
    }

    public UserResponseDTO updateUsuario(long id, UserRequestDTO userRequestDTO) {
        userNotFoundValidation(id);

        apiLogService.save(CriticidadeLog.INFO, "Usuario com id " + id + " atualizado com sucesso.", userRequestDTO);
        return userBoundary.update(userRequestDTO, id);
    }

    public void deleteUsuario(long id) {
        userNotFoundValidation(id);

        userBoundary.delete(id);
        apiLogService.save(CriticidadeLog.INFO, "Usuario com id " + id + " excluido com sucesso.", id);
    }

    private void userValidate(UserRequestDTO userRequestDTO) {
        emailUniqueValidation(userRequestDTO);
        //phoneNumberUniqueValidation(usuarioRequestDTO);
        throwIfInvalid(userRequestDTO);
    }

    private void userNotFoundValidation(long id) {
        if(!userBoundary.existsById(id)) {
            apiLogService.save(CriticidadeLog.WARN, "Usuario com id " + id + " nao encontrado no sistema.", id);
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Nenhum usuario encontrado");
        }
    }

    private void throwIfInvalid(UserRequestDTO userRequestDTO) {
        var violations = validator.validate(userRequestDTO);
        if (!violations.isEmpty()) {
            apiLogService.save(CriticidadeLog.ERROR, "Dados inválidos durante a criação do usuário.", userRequestDTO);
            throw new ConstraintViolationException(violations);
        }
    }

    private void emailUniqueValidation(UserRequestDTO userRequestDTO) {
        if(userBoundary.existsByEmail(userRequestDTO.getEmail())) {
            apiLogService.save(CriticidadeLog.WARN, "O email " + userRequestDTO.getEmail() + " já esta cadastrado no sistema.", userRequestDTO);
            throw new HttpStatusException(HttpStatus.CONFLICT, "Email já cadastrado");
        }
    }

//    private void phoneNumberUniqueValidation(UsuarioRequestDTO usuarioRequestDTO) {
//        if(userBoundary.existsByPhoneNumber(usuarioRequestDTO.getPhoneNumber())) {
//            apiLogService.save(CriticidadeLog.WARN, "O telefone " + usuarioRequestDTO.getPhoneNumber() + " já esta cadastrado no sistema.", usuarioRequestDTO);
//            throw new HttpStatusException(HttpStatus.CONFLICT, "Telefone já cadastrado");
//        }
//    }
}