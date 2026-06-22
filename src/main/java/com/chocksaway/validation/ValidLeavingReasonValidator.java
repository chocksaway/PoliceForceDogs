package com.chocksaway.validation;

import com.chocksaway.repository.DogLeavingReasonRepository;
import com.chocksaway.domain.DogLeavingReason;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class ValidLeavingReasonValidator implements ConstraintValidator<ValidLeavingReason, Long> {

    @Inject
    DogLeavingReasonRepository repo;

    // allowing these values - I wish I could get from the database - far more portable
    private static final List<String> ALLOWED = Arrays.asList(
        "Transferred",
        "Retired (Put Down)",
        "KIA",
        "Rejected",
        "Retired (Re-housed)",
        "Died"
    );

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        DogLeavingReason lr = repo.findById(value).orElse(null);
        if (lr == null) {
            return false;
        }

        String name = lr.getName() == null ? "" : lr.getName().trim();

        for (String a : ALLOWED) {
            if (name.equalsIgnoreCase(a)) return true;
        }

        return false;
    }
}

