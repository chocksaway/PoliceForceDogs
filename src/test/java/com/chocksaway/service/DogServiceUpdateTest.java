package com.chocksaway.service;

import com.chocksaway.command.DogSaveOrUpdateCommand;
import com.chocksaway.domain.Dog;
import com.chocksaway.mapper.DogMapper;
import com.chocksaway.repository.DogRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DogServiceUpdateTest {

    @Test
    void update_notFound_throws() {
        DogRepository repo = Mockito.mock(DogRepository.class);
        DogMapper mapper = Mockito.mock(DogMapper.class);

        Mockito.when(repo.findById(1L)).thenReturn(Optional.empty());

        DogService svc = new DogService(mapper, repo);

        DogSaveOrUpdateCommand cmd = new DogSaveOrUpdateCommand();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> svc.update(1L, cmd));
        assertTrue(ex.getMessage().contains("Dog not found"));
    }

    @Test
    void update_success_callsMapperAndSaves() {
        DogRepository repo = Mockito.mock(DogRepository.class);
        DogMapper mapper = Mockito.mock(DogMapper.class);

        Dog existing = new Dog();
        existing.setId(5L);
        existing.setName("Before");

        Mockito.when(repo.findById(5L)).thenReturn(Optional.of(existing));
        Mockito.when(repo.save(existing)).thenReturn(existing);

        DogService svc = new DogService(mapper, repo);

        DogSaveOrUpdateCommand cmd = new DogSaveOrUpdateCommand();
        cmd.setName("After");

        Dog result = svc.update(5L, cmd);

        // verify mapper was asked to apply updates and repository saved the entity
        Mockito.verify(mapper, Mockito.times(1)).updateFromCommand(cmd, existing);
        Mockito.verify(repo, Mockito.times(1)).save(existing);

        assertSame(existing, result);
    }
}

