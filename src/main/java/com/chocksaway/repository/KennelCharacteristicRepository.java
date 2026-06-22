package com.chocksaway.repository;

import com.chocksaway.domain.KennelCharacteristic;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import java.util.Optional;

@Repository
public interface KennelCharacteristicRepository extends CrudRepository<KennelCharacteristic, Long> {

	Optional<KennelCharacteristic> findByName(String name);

}

