package com.chocksaway.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Serdeable
@Introspected
@Schema(name = "Dog", description = "Dog data transfer object")
public class DogDto {
	private Long id;
	private String name;
	private String breed;
	private String supplier;
	private String badgeId;
	private String gender;
	private LocalDate birthDate;
	private LocalDate dateAcquired;
	private Long currentStatusId;
	private Long kennelId;
	private Long policeForceId;
	private List<Long> characteristicIds;
	private boolean deleted;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public DogDto() {
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getBreed() { return breed; }
	public void setBreed(String breed) { this.breed = breed; }

	public String getSupplier() { return supplier; }
	public void setSupplier(String supplier) { this.supplier = supplier; }

	public String getBadgeId() { return badgeId; }
	public void setBadgeId(String badgeId) { this.badgeId = badgeId; }

	public String getGender() { return gender; }
	public void setGender(String gender) { this.gender = gender; }

	public LocalDate getBirthDate() { return birthDate; }
	public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

	public LocalDate getDateAcquired() { return dateAcquired; }
	public void setDateAcquired(LocalDate dateAcquired) { this.dateAcquired = dateAcquired; }

	public Long getCurrentStatusId() { return currentStatusId; }
	public void setCurrentStatusId(Long currentStatusId) { this.currentStatusId = currentStatusId; }

	public Long getKennelId() { return kennelId; }
	public void setKennelId(Long kennelId) { this.kennelId = kennelId; }

	public Long getPoliceForceId() { return policeForceId; }
	public void setPoliceForceId(Long policeForceId) { this.policeForceId = policeForceId; }

	public List<Long> getCharacteristicIds() { return characteristicIds; }
	public void setCharacteristicIds(List<Long> characteristicIds) { this.characteristicIds = characteristicIds; }

	public boolean isDeleted() { return deleted; }
	public void setDeleted(boolean deleted) { this.deleted = deleted; }

	public LocalDateTime getCreatedAt() { return createdAt; }
	public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

	public LocalDateTime getUpdatedAt() { return updatedAt; }
	public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}


