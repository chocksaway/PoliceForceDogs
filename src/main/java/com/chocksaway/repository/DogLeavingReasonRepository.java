package com.chocksaway.repository;

import com.chocksaway.domain.DogLeavingReason;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface DogLeavingReasonRepository extends CrudRepository<DogLeavingReason, Long> {

}

