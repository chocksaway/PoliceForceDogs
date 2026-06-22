package com.chocksaway.controller;

import com.chocksaway.command.DogSaveOrUpdateCommand;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static io.micronaut.http.HttpHeaders.LOCATION;
import static io.micronaut.http.HttpStatus.CREATED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class DogSaveTest {

    private BlockingHttpClient blockingClient;

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    DataSource dataSource;

    @BeforeEach
    void setup() {
        blockingClient = client.toBlocking();

        try (Connection conn = dataSource.getConnection()) {
            // Clean
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM dog_kennel_characteristics")) {
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM dogs")) {
                ps.executeUpdate();
            }
            // police_force id=1
            try (PreparedStatement ps = conn.prepareStatement(
                    "MERGE INTO police_force (id, name, code, created_at) KEY(id) VALUES (1, 'Somerset Police', 'SOM', CURRENT_TIMESTAMP)")) {
                ps.executeUpdate();
            }
            // kennel_characteristics ids 1 and 2
            try (PreparedStatement ps = conn.prepareStatement(
                    "MERGE INTO kennel_characteristics (id, name, created_at) KEY(id) VALUES (1, 'High energy', CURRENT_TIMESTAMP)")) {
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "MERGE INTO kennel_characteristics (id, name, created_at) KEY(id) VALUES (2, 'Low tolerance for other dogs', CURRENT_TIMESTAMP)")) {
                ps.executeUpdate();
            }
            // dog_status id=2 (In Service)
            try (PreparedStatement ps = conn.prepareStatement(
                    "MERGE INTO dog_status (id, name, code, description, created_at) KEY(id) VALUES (2, 'In Service', 'IN_SERVICE', 'Dog is actively deployed on duty', CURRENT_TIMESTAMP)")) {
                ps.executeUpdate();
            }
            // kennel id=1
            try (PreparedStatement ps = conn.prepareStatement(
                    "MERGE INTO kennel (id, name, location, created_at) KEY(id) VALUES (1, 'North Kennels', 'North District - Unit A', CURRENT_TIMESTAMP)")) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private Long entityId(HttpResponse<?> response) {
        String path = "/api/dogs/";
        String value = response.header(LOCATION);
        if (value == null) {
            return null;
        }

        int index = value.indexOf(path);
        if (index != -1) {
            return Long.valueOf(value.substring(index + path.length()));
        }

        return null;
    }

    @Test
    void testSaveValidDog() throws Exception {
        DogSaveOrUpdateCommand cmd = new DogSaveOrUpdateCommand();
        cmd.setName("Ragnar");
        cmd.setBreed("Belgian Malinois");
        cmd.setSupplier("North Kennels");
        cmd.setBadgeId("B-007");
        cmd.setGender("male");
        cmd.setBirthDate(java.time.LocalDate.of(2018,4,2));
        cmd.setDateAcquired(java.time.LocalDate.of(2018,10,10));

        // Set required fields so this test sends a valid command
        cmd.setCurrentStatusId(2L); // In Service from seed data

        cmd.setKennelId(1L); // North Kennels from seed
        cmd.setPoliceForceId(1L);
        cmd.getCharacteristicIds().add(1L);
        cmd.getCharacteristicIds().add(2L);

        HttpRequest<?> request = HttpRequest.POST("/api/dogs", cmd);
        HttpResponse<?> response = blockingClient.exchange(request);

        assertEquals(CREATED, response.getStatus());
        Long id = entityId(response);
        assertNotNull(id);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id, name, badge_id FROM dogs WHERE id = ?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Created dog row not found in DB");
                assertEquals("Ragnar", rs.getString("name"));
            }
        }
    }

    @Test
    void testSaveInValidDog() throws Exception {
        DogSaveOrUpdateCommand cmd = new DogSaveOrUpdateCommand();
        cmd.setName("Lewis");
        cmd.setBreed("Spaniel");
        cmd.setSupplier("North Kennels");
        cmd.setBadgeId("B-004");
        cmd.setGender("xxxxxxxx"); // invalid gender
        cmd.setBirthDate(java.time.LocalDate.of(2018,4,2));
        cmd.setDateAcquired(java.time.LocalDate.of(2018,10,10));

        jakarta.validation.ValidatorFactory vf = jakarta.validation.Validation.buildDefaultValidatorFactory();
        jakarta.validation.Validator v = vf.getValidator();
        java.util.Set<jakarta.validation.ConstraintViolation<DogSaveOrUpdateCommand>> violations = v.validate(cmd);
        assertFalse(violations.isEmpty(), "Expected validation violations for invalid gender");

        cmd.setCurrentStatusId(2L);

        cmd.setKennelId(1L);
        cmd.setPoliceForceId(1L);
        cmd.getCharacteristicIds().add(1L);
        cmd.getCharacteristicIds().add(2L);

        // Expect a 400 Bad Request due to invalid gender
        HttpRequest<?> request = HttpRequest.POST("/api/dogs", cmd);
        HttpClientResponseException ex =
                assertThrows(HttpClientResponseException.class, () -> blockingClient.exchange(request));

        assertEquals(io.micronaut.http.HttpStatus.BAD_REQUEST, ex.getStatus());
    }
}
