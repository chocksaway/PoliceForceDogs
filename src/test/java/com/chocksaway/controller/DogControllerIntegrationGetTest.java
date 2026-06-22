package com.chocksaway.controller;

import com.chocksaway.domain.Dog;
import com.chocksaway.domain.DogStatus;
import com.chocksaway.domain.Kennel;
import com.chocksaway.domain.PoliceForce;
import com.chocksaway.dto.DogDto;
import io.micronaut.http.HttpRequest;

import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.micronaut.http.HttpStatus.NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest
public class DogControllerIntegrationGetTest {

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

    @Inject
    com.chocksaway.repository.DogRepository dogRepository;

    private Long createdId;

    @BeforeEach
    void setup() {
        blockingClient = client.toBlocking();

        // Ensure lookup rows exist using repositories
        PoliceForce pf = new PoliceForce();
        pf.setName("Somerset Police");
        pf = policeForceRepository.save(pf);

        Kennel kennel = new Kennel();
        kennel.setName("North Kennels");
        kennel.setLocation("North District - Unit A");
        kennel = kennelRepository.save(kennel);

        DogStatus status = new DogStatus();
        status.setName("In Service");
        status.setCode("IN_SERVICE");
        status.setDescription("Dog is actively deployed on duty");
        status = dogStatusRepository.save(status);

        // Create a Dog entity directly via repository
        Dog dog = new Dog();
        dog.setName("IntegrationDog");
        dog.setBreed("Beagle");
        dog.setSupplier("Supplier X");
        dog.setBadgeId("INT-" + System.nanoTime());
        dog.setGender("male");
        dog.setBirthDate(java.time.LocalDate.of(2019,5,5));
        dog.setDateAcquired(java.time.LocalDate.of(2019,6,1));
        dog.setCurrentStatus(status);
        dog.setKennel(kennel);
        dog.setPoliceForce(pf);
        dog = dogRepository.save(dog);
        createdId = dog.getId();
    }

    @Test
    void testGetById_returnsDogDto() {
        DogDto dto = blockingClient.retrieve(HttpRequest.GET("/api/dogs/" + createdId), DogDto.class);
        assertNotNull(dto);
        assertEquals(createdId, dto.getId());
        assertEquals("IntegrationDog", dto.getName());
    }

    @Test
    void testGetById_notFound_returns404() {
        // pick an id that is unlikely to exist
        long missing = 99999L;
        HttpClientResponseException ex =
                assertThrows(HttpClientResponseException.class,
                        () -> blockingClient.exchange(HttpRequest.GET("/api/dogs/" + missing)));
        assertEquals(NOT_FOUND, ex.getStatus());
    }
}

