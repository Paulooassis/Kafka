package com.crud.repository;

import com.crud.entities.User;
import com.crud.enums.Position;
import com.crud.enums.Function;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Page<User> findAllOrderByName(Pageable pageable);
    Page<User> findAllByPositionOrderByNameAsc(Position position, Pageable pageable);
    Page<User> findAllByFunctionOrderByNameAsc(Function function, Pageable pageable);
    Page<User> findAllByPositionAndFunctionOrderByNameAsc(Position position, Function function, Pageable pageable);
    //    boolean existsByPhoneNumber(String phoneNumber);
}
