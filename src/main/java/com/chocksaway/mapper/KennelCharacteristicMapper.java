package com.chocksaway.mapper;

import com.chocksaway.domain.KennelCharacteristic;
import com.chocksaway.repository.KennelCharacteristicRepository;
import jakarta.inject.Inject;
import org.mapstruct.Mapper;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import com.chocksaway.exception.InvalidCharacteristicException;

@Mapper(componentModel = "jsr330")
public abstract class KennelCharacteristicMapper {

    @Inject
    protected KennelCharacteristicRepository kennelCharacteristicRepository;

    /**
     * Map a name (String) to an existing KennelCharacteristic entity. Throws
     * InvalidCharacteristicException when the name does not exist in the database.
     */
    public KennelCharacteristic fromName(String name) {
        if (name == null) return null;
        return kennelCharacteristicRepository.findByName(name)
                .orElseThrow(() -> new InvalidCharacteristicException("Unknown kennel characteristic name: " + name));
    }

    public Set<KennelCharacteristic> fromNames(Set<String> names) {
        if (names == null)
            return Collections.emptySet();

        return names.stream()
                .filter(Objects::nonNull)
                .map(this::fromName)
                .collect(Collectors.toSet());
    }

    public String toName(KennelCharacteristic kc) {
        return kc == null ? null : kc.getName();
    }

    public Set<String> toNames(Set<KennelCharacteristic> kcs) {
        if (kcs == null) return Collections.emptySet();
        return kcs.stream()
                .filter(Objects::nonNull)
                .map(KennelCharacteristic::getName)
                .collect(Collectors.toSet());
    }
}

