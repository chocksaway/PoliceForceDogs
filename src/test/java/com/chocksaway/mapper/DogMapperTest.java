package com.chocksaway.mapper;

import com.chocksaway.command.DogSaveOrUpdateCommand;
import com.chocksaway.domain.Dog;
import com.chocksaway.domain.DogStatus;
import com.chocksaway.domain.Kennel;
import com.chocksaway.domain.KennelCharacteristic;
import com.chocksaway.exception.MissingReferenceException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DogMapperTest {

    @Test
    void mapDogStatusValid() {
        EntityManager em = Mockito.mock(EntityManager.class);
        DogStatus ds = new DogStatus(); ds.setId(1L); ds.setName("ACTIVE");
        Mockito.when(em.find(DogStatus.class, 1L)).thenReturn(ds);

        DogMapper mapper = new DogMapper() {
            @Override
            public Dog toEntity(DogSaveOrUpdateCommand cmd) {
                return null;
            }
            @Override
            public com.chocksaway.dto.DogDto toDto(Dog dog) {
                return null;
            }
            @Override
            public void updateFromCommand(DogSaveOrUpdateCommand cmd, Dog dog) { }
        };
        mapper.em = em;

        DogStatus result = mapper.map(1L);
        assertSame(ds, result);
    }

    @Test
    void mapKennelNotFoundThrows() {
        EntityManager em = Mockito.mock(EntityManager.class);
        Mockito.when(em.find(Kennel.class, 99L)).thenReturn(null);

        DogMapper mapper = new DogMapper() {
            @Override
            public Dog toEntity(DogSaveOrUpdateCommand cmd) {
                return null;
            }
            @Override
            public com.chocksaway.dto.DogDto toDto(Dog dog) {
                return null;
            }
            @Override
            public void updateFromCommand(DogSaveOrUpdateCommand cmd, Dog dog) { }
        };
        mapper.em = em;

        MissingReferenceException ex = assertThrows(MissingReferenceException.class, () -> mapper.mapToKennel(99L));
        assertTrue(ex.getMessage().contains("Kennel not found"));
    }

    @Test
    void mapCharacteristicsValid() {
        EntityManager em = Mockito.mock(EntityManager.class);
        KennelCharacteristic a = new KennelCharacteristic(); a.setId(1L); a.setName("a");
        KennelCharacteristic b = new KennelCharacteristic(); b.setId(2L); b.setName("b");
        Mockito.when(em.find(KennelCharacteristic.class, 1L)).thenReturn(a);
        Mockito.when(em.find(KennelCharacteristic.class, 2L)).thenReturn(b);

        DogMapper mapper = new DogMapper() {
            @Override
            public Dog toEntity(DogSaveOrUpdateCommand cmd) {
                return null;
            }
            @Override
            public com.chocksaway.dto.DogDto toDto(Dog dog) {
                return null;
            }
            @Override
            public void updateFromCommand(DogSaveOrUpdateCommand cmd, Dog dog) { }
        };
        mapper.em = em;

        Set<KennelCharacteristic> set = mapper.map(List.of(1L, 2L));
        assertEquals(2, set.size());
        assertTrue(set.contains(a));
        assertTrue(set.contains(b));
    }

    @Test
    void mapCharacteristicsInvalidThrows() {
        EntityManager em = Mockito.mock(EntityManager.class);
        KennelCharacteristic a = new KennelCharacteristic(); a.setId(1L); a.setName("a");
        Mockito.when(em.find(KennelCharacteristic.class, 1L)).thenReturn(a);
        Mockito.when(em.find(KennelCharacteristic.class, 2L)).thenReturn(null);

        DogMapper mapper = new DogMapper() {
            @Override
            public Dog toEntity(DogSaveOrUpdateCommand cmd) {
                return null;
            }
            @Override
            public com.chocksaway.dto.DogDto toDto(Dog dog) {
                return null;
            }
            @Override
            public void updateFromCommand(DogSaveOrUpdateCommand cmd, Dog dog) { }
        };
        mapper.em = em;

        assertThrows(MissingReferenceException.class, () -> mapper.map(List.of(1L, 2L)));
    }
}

