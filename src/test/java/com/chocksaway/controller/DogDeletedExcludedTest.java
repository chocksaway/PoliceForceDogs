package com.chocksaway.controller;

import java.util.Map;
import java.util.HashMap;
import com.chocksaway.dto.DogDto;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class DogDeletedExcludedTest {

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
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM dog_kennel_characteristics")) { ps.executeUpdate(); }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM dogs")) { ps.executeUpdate(); }

            try (PreparedStatement ps = conn.prepareStatement(
                    "MERGE INTO police_force (id, name, code, created_at) KEY(id) VALUES (1, 'Somerset Police', 'SOM', CURRENT_TIMESTAMP)")) { ps.executeUpdate(); }
            try (PreparedStatement ps = conn.prepareStatement(
                    "MERGE INTO kennel_characteristics (id, name, created_at) KEY(id) VALUES (1, 'High energy', CURRENT_TIMESTAMP)")) { ps.executeUpdate(); }
            try (PreparedStatement ps = conn.prepareStatement(
                    "MERGE INTO dog_status (id, name, code, description, created_at) KEY(id) VALUES (2, 'In Service', 'IN_SERVICE', 'Dog is actively deployed on duty', CURRENT_TIMESTAMP)")) { ps.executeUpdate(); }
            try (PreparedStatement ps = conn.prepareStatement(
                    "MERGE INTO kennel (id, name, location, created_at) KEY(id) VALUES (1, 'North Kennels', 'North District - Unit A', CURRENT_TIMESTAMP)")) { ps.executeUpdate(); }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Long entityId(io.micronaut.http.HttpResponse<?> response) {
        String path = "/api/dogs/";
        String value = response.header(io.micronaut.http.HttpHeaders.LOCATION);
        if (value == null) return null;
        int index = value.indexOf(path);
        if (index != -1) return Long.valueOf(value.substring(index + path.length()));
        return null;
    }

    @Test
    void deletedDogIsExcludedFromGetAllAndFilter() throws Exception {
        // create Alpha (will be deleted) and Bravo
        Map<String,Object> alpha = new HashMap<>();
        alpha.put("name", "Alpha");
        alpha.put("breed", "Labrador"); // filter
        alpha.put("supplier", "S1");
        alpha.put("badgeId", "A1");
        alpha.put("gender", "male");
        alpha.put("birthDate", "2019-01-01");
        alpha.put("dateAcquired", "2019-02-02");
        alpha.put("currentStatusId", 2);
        alpha.put("kennelId", 1);
        alpha.put("policeForceId", 1);

        Map<String,Object> bravo = new HashMap<>();
        bravo.put("name", "Bravo");
        bravo.put("breed", "Spaniel");
        bravo.put("supplier", "S2");
        bravo.put("badgeId", "B1");
        bravo.put("gender", "male");
        bravo.put("birthDate", "2019-03-03");
        bravo.put("dateAcquired", "2019-04-04");
        bravo.put("currentStatusId", 2);
        bravo.put("kennelId", 1);
        bravo.put("policeForceId", 1);

        HttpResponse<?> r1 = blockingClient.exchange(HttpRequest.POST("/api/dogs", alpha));
        HttpResponse<?> r2 = blockingClient.exchange(HttpRequest.POST("/api/dogs", bravo));

        // The POST endpoint returns the created DTO in the response body; verify deleted flag is false
        Optional<DogDto> b1 = r1.getBody(DogDto.class);
        Optional<DogDto> b2 = r2.getBody(DogDto.class);
        assertTrue(b1.isPresent(), "Expected response body for first created dog");
        assertTrue(b2.isPresent(), "Expected response body for second created dog");
        assertFalse(b1.get().isDeleted(), "Newly created dog should not be deleted");
        assertFalse(b2.get().isDeleted(), "Newly created dog should not be deleted");

        Long idAlpha = entityId(r1);
        Long idBravo = entityId(r2);
        assertNotNull(idAlpha);
        assertNotNull(idBravo);

        // delete alpha
        blockingClient.exchange(HttpRequest.DELETE("/api/dogs/" + idAlpha));

        // GET should return bravo
        List<DogDto> all = blockingClient.retrieve(HttpRequest.GET("/api/dogs"), Argument.listOf(DogDto.class));
        assertEquals(1, all.size());
        assertEquals("Bravo", all.getFirst().getName());

        // GET filter for Labrador should return none - // filter
        List<DogDto> filtered = blockingClient.retrieve(HttpRequest.GET("/api/dogs?filter=labrador"), Argument.listOf(DogDto.class));
        assertTrue(filtered.isEmpty());
    }
}

