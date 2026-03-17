package com.crud.repository;

import com.crud.entities.ApiLog;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;

@MongoRepository
public interface ApiLogRepository extends CrudRepository<ApiLog, String> {
}
