package com.crud.service;

import com.crud.boundary.UserBoundary;
import com.crud.dto.request.UserRequestDTO;
import com.crud.dto.response.UserResponseDTO;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@MicronautTest(startApplication = false)
class UserServiceTest {

    @Inject
    UserService userService;

    @Inject
    UserBoundary userBoundary; // mockado por @MockBean

    @MockBean(UserBoundary.class)
    UserBoundary userBoundary() {
        return Mockito.mock(UserBoundary.class);
    }

    @BeforeEach
    void setUp() {
        Mockito.reset(userBoundary);
    }

    @Test
    void createUsuario_sucesso() {
        // given
        UserRequestDTO request = validRequest("a@b.com", "Rua 1", "user1", "31998728066");
        UserResponseDTO response = Mockito.mock(UserResponseDTO.class);

        when(userBoundary.existsByEmail("a@b.com")).thenReturn(false);
        when(userBoundary.save(request)).thenReturn(response);

        // when
        UserResponseDTO out = userService.createUsuario(request);

        // then
        assertSame(response, out);
        verify(userBoundary).existsByEmail("a@b.com");
        verify(userBoundary).save(request);
        verifyNoMoreInteractions(userBoundary);
    }

    @Test
    void createUsuario_invalido_deveLancarConstraintViolationException() {
        // given: DTO inválido para acionar Bean Validation real
        UserRequestDTO request = invalidRequest();

        // when / then
        assertThrows(ConstraintViolationException.class, () -> userService.createUsuario(request));
        verifyNoInteractions(userBoundary);
    }

    @Test
    void createUsuario_emailJaCadastrado_deveLancarHttpStatusConflict() {
        // given
        UserRequestDTO request = validRequest("a@b.com", "Rua 1", "user1", "31998728066");
        when(userBoundary.existsByEmail("a@b.com")).thenReturn(true);

        // when
        HttpStatusException ex = assertThrows(HttpStatusException.class, () -> userService.createUsuario(request));

        // then
        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        verify(userBoundary).existsByEmail("a@b.com");
        verify(userBoundary, never()).save(any());
    }

    @Test
    void viewUsuario_sucesso() {
        // given
        long id = 10L;
        UserResponseDTO response = Mockito.mock(UserResponseDTO.class);

        when(userBoundary.existsById(id)).thenReturn(true);
        when(userBoundary.viewUser(id)).thenReturn(response);

        // when
        UserResponseDTO out = userService.viewUsuario(id);

        // then
        assertSame(response, out);
        verify(userBoundary).existsById(id);
        verify(userBoundary).viewUser(id);
    }

    @Test
    void viewUsuario_naoEncontrado() {
        long id = 10L;
        when(userBoundary.existsById(id)).thenReturn(false);

        HttpStatusException ex = assertThrows(HttpStatusException.class, () -> userService.viewUsuario(id));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());

        verify(userBoundary).existsById(id);
        verify(userBoundary, never()).viewUser(anyLong());
    }

    @Test
    void viewAllUsuarios_vazio_deveLancarNotFound() {
        when(userBoundary.viewAll()).thenReturn(List.of());

        HttpStatusException ex = assertThrows(HttpStatusException.class, () -> userService.viewAllUsuarios());
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());

        verify(userBoundary).viewAll();
    }

    @Test
    void viewAllUsuarios_sucesso() {
        UserResponseDTO r1 = Mockito.mock(UserResponseDTO.class);
        UserResponseDTO r2 = Mockito.mock(UserResponseDTO.class);
        when(userBoundary.viewAll()).thenReturn(List.of(r1, r2));

        List<UserResponseDTO> out = userService.viewAllUsuarios();

        assertEquals(2, out.size());
        assertSame(r1, out.get(0));
        assertSame(r2, out.get(1));
        verify(userBoundary, times(2)).viewAll();
    }

    @Test
    void updateUsuario_naoEncontrado() {
        long id = 7L;
        UserRequestDTO request = validRequest("x@y.com", "Rua 1", "u", "31998728066");
        when(userBoundary.existsById(id)).thenReturn(false);

        HttpStatusException ex = assertThrows(HttpStatusException.class, () -> userService.updateUsuario(id, request));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());

        verify(userBoundary).existsById(id);
        verify(userBoundary, never()).update(any(), anyLong());
    }

    @Test
    void updateUsuario_invalido_deveLancarConstraintViolationException() {
        long id = 7L;
        UserRequestDTO request = invalidRequest();
        when(userBoundary.existsById(id)).thenReturn(true);

        assertThrows(ConstraintViolationException.class, () -> userService.updateUsuario(id, request));

        verify(userBoundary).existsById(id);
        verify(userBoundary, never()).update(any(), anyLong());
    }

    @Test
    void updateUsuario_sucesso() {
        long id = 7L;
        UserRequestDTO request = validRequest("x@y.com", "Rua 1", "u", "31998728066");
        UserResponseDTO response = Mockito.mock(UserResponseDTO.class);

        when(userBoundary.existsById(id)).thenReturn(true);
        when(userBoundary.update(request, id)).thenReturn(response);

        UserResponseDTO out = userService.updateUsuario(id, request);

        assertSame(response, out);
        verify(userBoundary).existsById(id);
        verify(userBoundary).update(request, id);
    }

    @Test
    void deleteUsuario_naoEncontrado() {
        long id = 3L;
        when(userBoundary.existsById(id)).thenReturn(false);

        HttpStatusException ex = assertThrows(HttpStatusException.class, () -> userService.deleteUsuario(id));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());

        verify(userBoundary).existsById(id);
        verify(userBoundary, never()).delete(anyLong());
    }

    @Test
    void deleteUsuario_sucesso() {
        long id = 3L;
        when(userBoundary.existsById(id)).thenReturn(true);
        doNothing().when(userBoundary).delete(id);

        assertDoesNotThrow(() -> userService.deleteUsuario(id));

        verify(userBoundary).existsById(id);
        verify(userBoundary).delete(id);
    }

    // Helpers: construir DTO real (não mock) para acionar Bean Validation
    private UserRequestDTO validRequest(String email, String address, String username, String phone) {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail(email);
        dto.setAddress(address);
        dto.setUsername(username);
        dto.setPhoneNumber(phone);
        return dto;
    }

    private UserRequestDTO invalidRequest() {
        // Valores típicos inválidos para acionar NotBlank/NotNull/@Email, etc.
        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail("");       // vazio
        dto.setAddress("");     // vazio
        dto.setUsername("");    // vazio
        dto.setPhoneNumber(""); // vazio
        return dto;
    }
}