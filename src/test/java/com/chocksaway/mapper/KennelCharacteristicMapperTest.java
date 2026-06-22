package com.chocksaway.mapper;

import com.chocksaway.domain.KennelCharacteristic;
import com.chocksaway.exception.InvalidCharacteristicException;
import com.chocksaway.repository.KennelCharacteristicRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class KennelCharacteristicMapperTest {

    @Test
    void fromNameValid() {
        KennelCharacteristicRepository repo = Mockito.mock(KennelCharacteristicRepository.class);
        KennelCharacteristic kc = new KennelCharacteristic();
        kc.setId(42L);
        kc.setName("friendly");
        Mockito.when(repo.findByName("friendly")).thenReturn(Optional.of(kc));

        KennelCharacteristicMapper mapper = new KennelCharacteristicMapper() {};
        mapper.kennelCharacteristicRepository = repo;

        KennelCharacteristic result = mapper.fromName("friendly");
        assertSame(kc, result);
    }

    @Test
    void fromNameInvalidThrows() {
        KennelCharacteristicRepository repo = Mockito.mock(KennelCharacteristicRepository.class);
        Mockito.when(repo.findByName("unknown")).thenReturn(Optional.empty());

        KennelCharacteristicMapper mapper = new KennelCharacteristicMapper() {};
        mapper.kennelCharacteristicRepository = repo;

        assertThrows(InvalidCharacteristicException.class, () -> mapper.fromName("unknown"));
    }

    @Test
    void toNamesConvertsSet() {
        KennelCharacteristicMapper mapper = new KennelCharacteristicMapper() {};
        KennelCharacteristic a = new KennelCharacteristic();
        a.setName("a");
        KennelCharacteristic b = new KennelCharacteristic();
        b.setName("b");

        Set<String> names = mapper.toNames(Set.of(a, b));
        assertEquals(Set.of("a", "b"), names);
    }

    @Test
    void fromNamesConvertsAll() {
        KennelCharacteristicRepository repo = Mockito.mock(KennelCharacteristicRepository.class);
        KennelCharacteristic a = new KennelCharacteristic();
        a.setId(1L); a.setName("a");
        KennelCharacteristic b = new KennelCharacteristic();
        b.setId(2L); b.setName("b");
        Mockito.when(repo.findByName("a")).thenReturn(Optional.of(a));
        Mockito.when(repo.findByName("b")).thenReturn(Optional.of(b));

        KennelCharacteristicMapper mapper = new KennelCharacteristicMapper() {};
        mapper.kennelCharacteristicRepository = repo;

        Set<KennelCharacteristic> set = mapper.fromNames(Set.of("a", "b"));
        assertEquals(2, set.size());
        assertTrue(set.contains(a));
        assertTrue(set.contains(b));
    }
}

