package com.chocksaway.repository;

import com.chocksaway.domain.PoliceForce;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface PoliceForceRepository extends CrudRepository<PoliceForce, Long> {

}

