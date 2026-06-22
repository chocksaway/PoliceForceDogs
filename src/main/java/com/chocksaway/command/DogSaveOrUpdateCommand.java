package com.chocksaway.command;

import com.chocksaway.validation.ValidDogStatus;
import com.chocksaway.validation.ValidLeavingReason;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.core.annotation.Introspected;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Serdeable
@Introspected
@Schema(name = "DogCommand", description = "Payload for creating or updating a dog")
public class DogSaveOrUpdateCommand {

    @NotBlank
    private String name;

    @NotBlank
    private String breed;

    @NotBlank
    private String supplier;

    @NotBlank
    private String badgeId;

    @NotBlank
    @Pattern(regexp = "(?i)^(male|female)$", message = "gender must be 'male' or 'female'")
    private String gender;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    private LocalDate dateAcquired;

    @NotNull
    @ValidDogStatus
    private Long currentStatusId;

    private LocalDate leavingDate;

    @ValidLeavingReason
    private Long leavingReasonId;

    @NotNull
    private Long kennelId;

    @NotNull
    private Long policeForceId;

    private List<Long> characteristicIds = new ArrayList<>();

    public DogSaveOrUpdateCommand() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getBadgeId() {
        return badgeId;
    }

    public void setBadgeId(String badgeId) {
        this.badgeId = badgeId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getDateAcquired() {
        return dateAcquired;
    }

    public void setDateAcquired(LocalDate dateAcquired) {
        this.dateAcquired = dateAcquired;
    }

    public Long getCurrentStatusId() {
        return currentStatusId;
    }

    public void setCurrentStatusId(Long currentStatusId) {
        this.currentStatusId = currentStatusId;
    }

    public LocalDate getLeavingDate() {
        return leavingDate;
    }

    public void setLeavingDate(LocalDate leavingDate) {
        this.leavingDate = leavingDate;
    }

    public Long getLeavingReasonId() {
        return leavingReasonId;
    }

    public void setLeavingReasonId(Long leavingReasonId) {
        this.leavingReasonId = leavingReasonId;
    }

    public Long getKennelId() {
        return kennelId;
    }

    public void setKennelId(Long kennelId) {
        this.kennelId = kennelId;
    }

    public Long getPoliceForceId() {
        return policeForceId;
    }

    public void setPoliceForceId(Long policeForceId) {
        this.policeForceId = policeForceId;
    }

    public List<Long> getCharacteristicIds() {
        return characteristicIds;
    }

    public void setCharacteristicIds(List<Long> characteristicIds) {
        this.characteristicIds = characteristicIds;
    }
}

