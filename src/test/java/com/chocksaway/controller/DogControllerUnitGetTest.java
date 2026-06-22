package com.chocksaway.controller;

import com.chocksaway.domain.Dog;
import com.chocksaway.dto.DogDto;
import com.chocksaway.service.DogService;
import io.micronaut.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DogControllerUnitGetTest {

    @Test
    void get_existingDog_returnsOkWithDto() {
        DogService svc = Mockito.mock(DogService.class);

        Dog dog = new Dog();
        dog.setId(10L);
        dog.setName("UnitTest");
        dog.setBreed("TestBreed");
        dog.setBirthDate(LocalDate.of(2020,1,1));

        DogDto dto = new DogDto();
        dto.setId(10L);
        dto.setName("UnitTest");
        dto.setBreed("TestBreed");

        Mockito.when(svc.getDto(10L)).thenReturn(dto);

        DogController controller = new DogController(svc);

        HttpResponse<DogDto> resp = controller.get(10L);

        assertTrue(resp.getStatus().getCode() >= 200 && resp.getStatus().getCode() < 300);
        assertTrue(resp.getBody().isPresent());
        assertEquals(dto, resp.getBody().get());
    }

    @Test
    void get_missingDog_returnsNotFound() {
        DogService svc = Mockito.mock(DogService.class);

        Mockito.when(svc.getDto(99L)).thenThrow(new IllegalArgumentException("Dog not found"));

        DogController controller = new DogController(svc);

        HttpResponse<DogDto> resp = controller.get(99L);

        assertEquals(404, resp.getStatus().getCode());
        assertFalse(resp.getBody().isPresent());
    }
}

