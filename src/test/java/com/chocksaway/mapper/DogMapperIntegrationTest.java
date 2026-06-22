package com.chocksaway.mapper;

import com.chocksaway.command.DogSaveOrUpdateCommand;
import com.chocksaway.domain.*;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class DogMapperIntegrationTest {

    @Inject
    DogMapper dogMapper;

    @Inject
    jakarta.persistence.EntityManager em;

    @Test
    void toEntityCreatesFullDog() {
        // create
        DogStatus status = new DogStatus();
        status.setName("ACTIVE");
        em.persist(status);

        DogLeavingReason lr = new DogLeavingReason();
        lr.setName("RETIREMENT");
        em.persist(lr);

        Kennel kennel = new Kennel();
        kennel.setName("Central Kennel");
        em.persist(kennel);

        PoliceForce pf = new PoliceForce();
        pf.setName("Somerset Police");
        em.persist(pf);

        KennelCharacteristic kc1 = new KennelCharacteristic(); kc1.setName("friendly");
        KennelCharacteristic kc2 = new KennelCharacteristic(); kc2.setName("alert");
        em.persist(kc1);
        em.persist(kc2);

        em.flush();

        // build command
        DogSaveOrUpdateCommand cmd = new DogSaveOrUpdateCommand();
        cmd.setName("Rex");
        cmd.setBreed("German Shepherd");
        cmd.setBirthDate(LocalDate.of(2018,1,1));
        cmd.setCurrentStatusId(status.getId());
        cmd.setLeavingReasonId(lr.getId());
        cmd.setKennelId(kennel.getId());
        cmd.setPoliceForceId(pf.getId());
        cmd.setCharacteristicIds(List.of(kc1.getId(), kc2.getId()));

        Dog d = dogMapper.toEntity(cmd); // mapper

        assertNotNull(d);
        assertEquals("Rex", d.getName());
        assertEquals("German Shepherd", d.getBreed());

        assertNotNull(d.getCurrentStatus());
        assertEquals(status.getId(), d.getCurrentStatus().getId());

        assertNotNull(d.getLeavingReason());
        assertEquals(lr.getId(), d.getLeavingReason().getId());

        assertNotNull(d.getKennel());
        assertEquals(kennel.getId(), d.getKennel().getId());

        assertNotNull(d.getPoliceForce());
        assertEquals(pf.getId(), d.getPoliceForce().getId());

        assertNotNull(d.getKennelCharacteristics());
        assertEquals(2, d.getKennelCharacteristics().size());
    }
}

