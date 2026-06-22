package com.chocksaway.service;

import com.chocksaway.command.DogSaveOrUpdateCommand;
import com.chocksaway.domain.Dog;
import com.chocksaway.mapper.DogMapper;
import com.chocksaway.repository.DogRepository;
import com.chocksaway.util.Page;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.chocksaway.dto.DogDto;

// Called by the controller - this is particular to the dog entity, mapper, and repository.

@Singleton
public class DogService {
    private final DogMapper dogMapper;
    private final DogRepository repository;

    @Inject
    public DogService(DogMapper dogMapper,  DogRepository repository) {
        this.dogMapper = dogMapper;
        this.repository = repository;
    }

    @Transactional
    public Dog save(DogSaveOrUpdateCommand cmd) {
        Dog dog = dogMapper.toEntity(cmd);
        repository.save(dog);
        return dog;
    }

    @Transactional
    public Dog update(Long id, DogSaveOrUpdateCommand cmd) {
        Optional<Dog> opt = repository.findById(id);
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("Dog not found for id: " + id);
        }
        Dog existing = opt.get();
        // Update fields in-place (MapStruct will resolve relationships)
        dogMapper.updateFromCommand(cmd, existing);
        repository.save(existing);
        return existing;
    }

    /**
     * Save an existing Dog entity
     */
    @Transactional
    public Dog update(Dog dog) {
        return repository.save(dog);
    }

    /**
     * Soft-delete: mark the dog deleted flag and persist via update(Dog).
     */
    @Transactional
    public Dog softDelete(Long id) {
        Optional<Dog> dog = repository.findById(id);
        if (dog.isEmpty()) {
            throw new IllegalArgumentException("Dog not found for id: " + id);
        }
        Dog existing = dog.get();
        existing.setDeleted(true);
        // reuse update()
        return update(existing);
    }

    public List<Dog> getAllButExcludeDeleted() {
        return repository.findAllNotDeleted();
    }

    /**
     *  database level filtered search by name, breed or supplier. If filter is null or blank,
     * returns all dogs - apart from deleted = true.
     */
    public List<Dog> getAllButExcludeDeleted(String filter) {
        if (filter == null) {
            return getAllButExcludeDeleted();
        }
        String query = filter.trim();
        if (query.isEmpty()) {
            return getAllButExcludeDeleted();
        }

        return repository.searchNotDeleted(query);
    }

    /**
     * Return DTOs for ALL dogs
     * Paging - this is a simple example, and should use a database offset (limit)
     */
    @Transactional
    public List<DogDto> getAllDogs(String filter, Integer pageNumber) {
        List<Dog> dogs = getAllButExcludeDeleted(filter);
        List<DogDto> dtos = new java.util.ArrayList<>();

        if (pageNumber != null) {
            Page page = new Page(pageNumber, 1, dogs.size());
            Map<String, Integer> pageInfo = page.offset();

            dogs.subList(pageInfo.get("start"), pageInfo.get("finish"))
                    .forEach(each -> dtos.add(dogMapper.toDto(each)));
        } else {
            dogs.forEach(each -> dtos.add(dogMapper.toDto(each)));
        }

        return dtos;
    }


    @Transactional
    public DogDto toDto(Dog d) {
        return dogMapper.toDto(d);
    }

    @Transactional
    public DogDto getDto(Long id) {
        Optional<Dog> opt = repository.findById(id);
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("Dog not found for id: " + id);
        }
        return dogMapper.toDto(opt.get());
    }

    public Dog get(Long id) {
        Optional<Dog> opt = repository.findById(id);
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("Dog not found for id: " + id);
        }

        return opt.get();

    }
}
