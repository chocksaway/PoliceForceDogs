package com.chocksaway.controller;

import com.chocksaway.command.DogSaveOrUpdateCommand;
import com.chocksaway.domain.Dog;
import com.chocksaway.dto.DogDto;
import com.chocksaway.service.DogService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import io.micronaut.validation.Validated;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import com.chocksaway.exception.MissingReferenceException;

@Controller("/api/dogs")
@Validated
@Tag(name = "dogs", description = "Operations for dogs")
public class DogController {

    private final DogService dogService;

    @Inject
    public DogController(DogService dogService) {
        this.dogService = dogService;
    }

    @Post
    @Operation(summary = "Create a dog", description = "Create a new dog record")
    @ApiResponse(responseCode = "201", description = "Dog created")
    public HttpResponse<DogDto> create(@Body @Valid DogSaveOrUpdateCommand cmd) {
        Dog saved = dogService.save(cmd);
        DogDto dto = dogService.toDto(saved);
        URI location = URI.create("/api/dogs/" + saved.getId());
        return HttpResponse.created(location).body(dto);
    }

    @Put("/{id}")
    @Operation(summary = "Update a dog", description = "Update an existing dog by id")
    public HttpResponse<DogDto> update(@PathVariable Long id, @Body @Valid DogSaveOrUpdateCommand cmd) {
        try {
            Dog updated = dogService.update(id, cmd);
            DogDto dto = dogService.toDto(updated);
            return HttpResponse.ok(dto);
        } catch (MissingReferenceException e) {
            return HttpResponse.badRequest();
        } catch (IllegalArgumentException e) {
            return HttpResponse.notFound();
        }
    }

    @Get("/{id}")
    @Operation(summary = "Get a dog", description = "Get a single dog by id")
    public HttpResponse<DogDto> get(@PathVariable Long id) {
        try {
            DogDto dto = dogService.getDto(id);
            return HttpResponse.ok(dto);
        } catch (IllegalArgumentException e) {
            return HttpResponse.notFound();
        }
    }

    @Delete("/{id}")
    @Operation(summary = "Delete a dog", description = "Soft-delete a dog by id")
    public HttpResponse<Void> delete(@PathVariable Long id) {
        try {
            dogService.softDelete(id);
            return HttpResponse.noContent();
        } catch (IllegalArgumentException e) {
            return HttpResponse.notFound();
        }
    }

    @Get
    public HttpResponse<List<DogDto>> getAllButExcludeDeleted(@QueryValue Optional<Integer> page, @QueryValue Optional<String> filter) {
        List<DogDto> dtos = dogService.getAllDogs(filter.orElse(null), page.orElse(null));
        if (page.isPresent() && dtos.isEmpty()) {
            return HttpResponse.notFound();
        }
        return HttpResponse.ok(dtos);
    }


}

