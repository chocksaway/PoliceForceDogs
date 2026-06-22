package com.chocksaway.service;

import com.chocksaway.domain.Dog;
import com.chocksaway.mapper.DogMapper;
import com.chocksaway.repository.DogRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DogServiceDeleteTest {

    @Test
    void softDelete_notFound_throws() {
        DogRepository repo = Mockito.mock(DogRepository.class);
        DogMapper mapper = Mockito.mock(DogMapper.class);

        Mockito.when(repo.findById(1L)).thenReturn(Optional.empty());

        DogService svc = new DogService(mapper, repo);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> svc.softDelete(1L));
        assertTrue(ex.getMessage().contains("Dog not found"));
    }

    @Test
    void softDelete_success_setsDeletedAndSaves() {
        DogRepository repo = Mockito.mock(DogRepository.class);
        DogMapper mapper = Mockito.mock(DogMapper.class);

        Dog existing = new Dog();
        existing.setId(10L);
        existing.setDeleted(false);

        Mockito.when(repo.findById(10L)).thenReturn(Optional.of(existing));
        Mockito.when(repo.save(existing)).thenReturn(existing);

        DogService svc = new DogService(mapper, repo);

        Dog result = svc.softDelete(10L);

        assertTrue(result.isDeleted());
        Mockito.verify(repo, Mockito.times(1)).save(existing);
    }
}

