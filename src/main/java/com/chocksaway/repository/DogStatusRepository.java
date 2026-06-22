package com.chocksaway.repository;

import com.chocksaway.domain.DogStatus;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface DogStatusRepository extends CrudRepository<DogStatus, Long> {

}

