package com.chocksaway.service;

import com.chocksaway.domain.Dog;
import com.chocksaway.mapper.DogMapper;
import com.chocksaway.repository.DogRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DogServiceSoftDeleteFilterTest {

    @Test
    void softDelete_thenGetAll_excludesDeleted() {
        DogRepository repo = Mockito.mock(DogRepository.class);
        DogMapper mapper = Mockito.mock(DogMapper.class);

        Dog existing = new Dog();
        existing.setId(42L);
        existing.setDeleted(false);

        Mockito.when(repo.findById(42L)).thenReturn(Optional.of(existing));
        Mockito.when(repo.save(existing)).thenAnswer(inv -> {
            // simulate persist toggling deleted
            existing.setDeleted(true);
            return existing;
        });

        // After deletion repository should return no non-deleted dogs
        Mockito.when(repo.findAllNotDeleted()).thenReturn(Collections.emptyList());

        DogService svc = new DogService(mapper, repo);

        Dog deleted = svc.softDelete(42L);
        assertTrue(deleted.isDeleted(), "Expected dog to be marked deleted");

        java.util.List<Dog> remaining = svc.getAllButExcludeDeleted();
        assertTrue(remaining.isEmpty(), "Expected no non-deleted dogs returned by getAll");

        Mockito.verify(repo, Mockito.times(1)).save(existing);
        Mockito.verify(repo, Mockito.times(1)).findAllNotDeleted();
    }
}

