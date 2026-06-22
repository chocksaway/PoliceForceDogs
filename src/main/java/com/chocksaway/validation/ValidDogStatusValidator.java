package com.chocksaway.validation;

import com.chocksaway.repository.DogStatusRepository;
import com.chocksaway.domain.DogStatus;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class ValidDogStatusValidator implements ConstraintValidator<ValidDogStatus, Long> {

    @Inject
    DogStatusRepository dogStatusRepository;

    // Use "Training", "In Service", "Retired", "Left"  - I wish I could get from the database - far more portable
    private static final List<String> ALLOWED = Arrays.asList("Training", "In Service", "Retired", "Left");

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        DogStatus ds = dogStatusRepository.findById(value).orElse(null);

        if (ds == null) {
            return false;
        }

        String name = ds.getName() == null ? "" : ds.getName().trim();
        for (String a : ALLOWED) {
            if (name.equalsIgnoreCase(a)) return true;
        }

        return false;
    }
}

