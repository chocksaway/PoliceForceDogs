package com.chocksaway.validation;

import com.chocksaway.domain.DogStatus;
import com.chocksaway.repository.DogStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidDogStatusValidatorTest {

    private ValidDogStatusValidator validator;
    private DogStatusRepository repo;

    @BeforeEach
    void setup() throws Exception {
        validator = new ValidDogStatusValidator();
        repo = Mockito.mock(DogStatusRepository.class);
        Field f = ValidDogStatusValidator.class.getDeclaredField("dogStatusRepository");
        f.setAccessible(true);
        f.set(validator, repo);
    }

    @Test
    void returnsTrueForNull() {
        assertTrue(validator.isValid(null, null));
    }

    @Test
    void returnsFalseWhenMissing() {
        Mockito.when(repo.findById(1L)).thenReturn(Optional.empty());
        assertFalse(validator.isValid(1L, null));
    }

    @Test
    void matchesIgnoringCaseAndWhitespace() {
        DogStatus ds = new DogStatus();
        ds.setId(2L);
        ds.setName("  retired ");
        Mockito.when(repo.findById(2L)).thenReturn(Optional.of(ds));
        assertTrue(validator.isValid(2L, null));
    }

    @Test
    void rejectsNonValid() {
        DogStatus ds = new DogStatus();
        ds.setName("In Training - probation");
        Mockito.when(repo.findById(3L)).thenReturn(Optional.of(ds));
        assertFalse(validator.isValid(3L, null));
    }
}

