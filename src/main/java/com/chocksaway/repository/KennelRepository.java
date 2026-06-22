package com.chocksaway.repository;

import com.chocksaway.domain.Kennel;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface KennelRepository extends CrudRepository<Kennel, Long> {

}

