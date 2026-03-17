package com.crud.boundary;

import com.crud.dto.request.UserRequestDTO;
import com.crud.dto.response.UserResponseDTO;
import com.crud.entities.User;
import com.crud.enums.Position;
import com.crud.enums.Function;
import com.crud.mapper.UserMapping;
import com.crud.repository.UserRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class UserBoundary {
    private final UserRepository userRepository;
    private final UserMapping userMapping;

    public UserResponseDTO save(UserRequestDTO userRequestDTO){
        User user = userMapping.toEntity(userRequestDTO);
        user = userRepository.save(user);
        return userMapping.toResponseDTO(user);
    }

    public UserResponseDTO viewUser(Long id){
        User user = userRepository.findById(id).get();
        return userMapping.toResponseDTO(user);
    }

//    public List<UsuarioResponseDTO> viewAll(){
//        List<Usuario> usuarios = usuarioRepository.findAll();
//        return usuarios.stream()
//                .map(userMapping::toResponseDTO).toList();
//    }

    public Page<UserResponseDTO> viewAllUsers(int page, int size, String cargo, String function){
        Position positionEnum = cargo.isEmpty() ? null : Position.fromName(cargo);
        Function functionEnum = function.isEmpty() ? null : Function.fromName(function);

        Pageable pageable = Pageable.from(page, size);

        if(positionEnum != null && functionEnum != null){
            return userRepository.findAllByPositionAndFunctionOrderByNameAsc(positionEnum, functionEnum, pageable).map(userMapping::toResponseDTO);
        } else if (positionEnum == null && functionEnum != null) {
            return userRepository.findAllByFunctionOrderByNameAsc(functionEnum, pageable).map(userMapping::toResponseDTO);
        } else if (positionEnum != null) {
            return userRepository.findAllByPositionOrderByNameAsc(positionEnum, pageable).map(userMapping::toResponseDTO);
        }else {
            return userRepository.findAllOrderByName(pageable).map(userMapping::toResponseDTO);
        }
    }

    public UserResponseDTO update(UserRequestDTO userRequestDTO, Long id){
        User user = userRepository.findById(id).get();

        user.setEmail(userRequestDTO.getEmail());
        user.setName(userRequestDTO.getName());
        user.setPosition(userRequestDTO.getPosition());
        user.setFunction(userRequestDTO.getFunction());
//        usuario.setAddress(usuarioRequestDTO.getAddress());
//        usuario.setPhoneNumber(usuarioRequestDTO.getPhoneNumber());
        userRepository.update(user);

        return userMapping.toResponseDTO(user);
    }

    public void delete(Long id){
        userRepository.deleteById(id);
    }

    public boolean existsById(Long id){
        return userRepository.existsById(id);
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

//    public boolean existsByPhoneNumber(String phoneNumber){
//        return usuarioRepository.existsByPhoneNumber(phoneNumber);
//    }
}
