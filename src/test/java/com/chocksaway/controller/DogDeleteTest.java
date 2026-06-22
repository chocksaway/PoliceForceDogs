package com.chocksaway.controller;

import com.chocksaway.command.DogSaveOrUpdateCommand;
import io.micronaut.http.HttpRequest;
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
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class DogDeleteTest {

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
    void testDeleteMarksDeleted() throws Exception {
        DogSaveOrUpdateCommand cmd = new DogSaveOrUpdateCommand();
        cmd.setName("ToDelete");
        cmd.setBreed("TestBreed");
        cmd.setSupplier("Supplier");
        cmd.setBadgeId("BD-1");
        cmd.setGender("male");
        cmd.setBirthDate(java.time.LocalDate.of(2020,1,1));
        cmd.setDateAcquired(java.time.LocalDate.of(2020,2,2));
        cmd.setCurrentStatusId(2L);
        cmd.setKennelId(1L);
        cmd.setPoliceForceId(1L);

        io.micronaut.http.HttpRequest<?> request = HttpRequest.POST("/api/dogs", cmd);
        io.micronaut.http.HttpResponse<?> response = blockingClient.exchange(request);
        assertEquals(io.micronaut.http.HttpStatus.CREATED, response.getStatus());
        Long id = entityId(response);
        assertNotNull(id);

        io.micronaut.http.HttpRequest<?> del = HttpRequest.DELETE("/api/dogs/" + id);
        io.micronaut.http.HttpResponse<?> delResp = blockingClient.exchange(del);
        assertEquals(io.micronaut.http.HttpStatus.NO_CONTENT, delResp.getStatus());

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT deleted FROM dogs WHERE id = ?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next());
                assertTrue(rs.getBoolean("deleted"));
            }
        }
    }
}

