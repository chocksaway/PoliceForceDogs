package com.chocksaway.repository;

import com.chocksaway.domain.Dog;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface DogRepository extends CrudRepository<Dog, Long> {
	java.util.List<Dog> findByNameContainsIgnoreCaseOrBreedContainsIgnoreCaseOrSupplierContainsIgnoreCase(String name, String breed, String supplier);

	@io.micronaut.data.annotation.Query("SELECT d FROM com.chocksaway.domain.Dog d WHERE d.deleted = FALSE")
	java.util.List<Dog> findAllNotDeleted();

	@io.micronaut.data.annotation.Query(
			"SELECT d FROM com.chocksaway.domain.Dog d WHERE d.deleted = FALSE AND (LOWER(d.name) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(d.breed) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(d.supplier) LIKE LOWER(CONCAT('%', :q, '%')))"
	)
	java.util.List<Dog> searchNotDeleted(String q);  // This method searches for dogs that are not deleted and match the search query in name OR breed OR supplier.

}

