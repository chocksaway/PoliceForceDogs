package com.chocksaway.controller;

import com.chocksaway.command.DogSaveOrUpdateCommand;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.core.type.Argument;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import com.chocksaway.dto.DogDto;

@MicronautTest(transactional = false)
public class DogGetTest {

    private BlockingHttpClient blockingClient;

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    com.chocksaway.repository.PoliceForceRepository policeForceRepository;

    @Inject
    com.chocksaway.repository.KennelRepository kennelRepository;

    @Inject
    com.chocksaway.repository.DogStatusRepository dogStatusRepository;

    @BeforeEach
    void setup() {
        blockingClient = client.toBlocking();

        // Ensure lookup rows exist (police_force, kennel, dog_status) using repositories
        com.chocksaway.domain.PoliceForce pf = new com.chocksaway.domain.PoliceForce();
        pf.setName("Somerset Police");
        pf = policeForceRepository.save(pf);

        com.chocksaway.domain.Kennel kennel = new com.chocksaway.domain.Kennel();
        kennel.setName("North Kennels");
        kennel.setLocation("North District - Unit A");
        kennel = kennelRepository.save(kennel);

        com.chocksaway.domain.DogStatus status = new com.chocksaway.domain.DogStatus();
        status.setName("In Service");
        status.setCode("IN_SERVICE");
        status.setDescription("Dog is actively deployed on duty");
        status = dogStatusRepository.save(status);

        // Create two dogs via the API
        DogSaveOrUpdateCommand d1 = new DogSaveOrUpdateCommand();
        d1.setName("Alpha");
        d1.setBreed("Labrador");
        d1.setSupplier("Supplier A");
        d1.setBadgeId("TG-" + System.currentTimeMillis() + "-1");
        d1.setGender("male");
        d1.setBirthDate(java.time.LocalDate.of(2019,1,1));
        d1.setDateAcquired(java.time.LocalDate.of(2019,6,1));
        d1.setCurrentStatusId(status.getId());
        d1.setKennelId(kennel.getId());
        d1.setPoliceForceId(pf.getId());

        DogSaveOrUpdateCommand d2 = new DogSaveOrUpdateCommand();
        d2.setName("Bravo");
        d2.setBreed("Spaniel");
        d2.setSupplier("Supplier B");
        d2.setBadgeId("TG-" + System.currentTimeMillis() + "-2");
        d2.setGender("female");
        d2.setBirthDate(java.time.LocalDate.of(2020,2,2));
        d2.setDateAcquired(java.time.LocalDate.of(2020,8,1));
        d2.setCurrentStatusId(status.getId());
        d2.setKennelId(kennel.getId());
        d2.setPoliceForceId(pf.getId());

        blockingClient.exchange(HttpRequest.POST("/api/dogs", d1));
        blockingClient.exchange(HttpRequest.POST("/api/dogs", d2));
    }

    @Inject
    com.chocksaway.service.DogService dogService;

    @Test
    void testGetAllDogs() {
        java.util.List<com.chocksaway.domain.Dog> dogs = dogService.getAllButExcludeDeleted();
        // There should be at least two dogs we created in setup
        boolean foundAlpha = dogs.stream().anyMatch(d -> "Alpha".equals(d.getName()));
        boolean foundBravo = dogs.stream().anyMatch(d -> "Bravo".equals(d.getName()));
        assertTrue(foundAlpha, "Expected service to return Alpha");
        assertTrue(foundBravo, "Expected service to return Bravo");
    }

    @Test
    void testGetAllDogs_withFilter() {
        // Call the HTTP GET endpoint with a filter that should match "Labrador" (Alpha)
        java.util.List<DogDto> dtos = blockingClient.retrieve(HttpRequest.GET("/api/dogs?filter=labrador"), Argument.listOf(DogDto.class));

        boolean foundAlpha = dtos.stream().anyMatch(d -> "Alpha".equals(d.getName()));
        boolean foundBravo = dtos.stream().anyMatch(d -> "Bravo".equals(d.getName()));

        assertTrue(foundAlpha, "Expected filtered results to include Alpha");
        assertFalse(foundBravo, "Expected filtered results NOT to include Bravo");
    }
}

