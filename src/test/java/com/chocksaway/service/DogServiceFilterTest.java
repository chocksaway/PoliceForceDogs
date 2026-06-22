package com.chocksaway.service;

import com.chocksaway.domain.Dog;
import com.chocksaway.mapper.DogMapper;
import com.chocksaway.repository.DogRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DogServiceFilterTest {

    @Test
    void getAll_withFilter_usesRepositorySearch() {
        DogRepository repo = Mockito.mock(DogRepository.class);
        DogMapper mapper = Mockito.mock(DogMapper.class);

        Dog alpha = new Dog();
        alpha.setName("Alpha");
        alpha.setBreed("Labrador");
        alpha.setSupplier("Supplier A");

        List<Dog> expected = Arrays.asList(alpha);

        Mockito.when(repo.searchNotDeleted("lab"))
                .thenReturn(expected);

        DogService svc = new DogService(mapper, repo);

        List<Dog> results = svc.getAllButExcludeDeleted("lab");
        assertEquals(1, results.size());
        assertEquals("Alpha", results.get(0).getName());
    }

    @Test
    void getAll_withNullOrBlank_returnsAll() {
        DogRepository repo = Mockito.mock(DogRepository.class);
        DogMapper mapper = Mockito.mock(DogMapper.class);

        Dog a = new Dog(); a.setName("A");
        Dog b = new Dog(); b.setName("B");

        List<Dog> all = Arrays.asList(a, b);
        Mockito.when(repo.findAllNotDeleted()).thenReturn(all);

        DogService svc = new DogService(mapper, repo);

        List<Dog> r1 = svc.getAllButExcludeDeleted((String) null);
        List<Dog> r2 = svc.getAllButExcludeDeleted("");

        assertEquals(2, r1.size());
        assertEquals(2, r2.size());
    }
}

