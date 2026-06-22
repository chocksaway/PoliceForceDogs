package com.chocksaway.domain;

import jakarta.persistence.*;
import io.micronaut.serde.annotation.Serdeable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Serdeable
@Table(name = "dogs")
public class Dog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String breed;

    @Column(nullable = false)
    private String supplier;


    @Column(name = "badge_id", unique = true, nullable = false)
    private String badgeId;

    @Column(nullable = false)
    private String gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "date_acquired")
    private LocalDate dateAcquired;

    // many to one
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_status_id")
    private DogStatus currentStatus;

    @Column(name = "leaving_date")
    private LocalDate leavingDate;

    // many to one
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leaving_reason_id")
    private DogLeavingReason leavingReason;

    // many to many join
    @ManyToMany
    @JoinTable(name = "dog_kennel_characteristics",
            joinColumns = @JoinColumn(name = "dog_id"),
            inverseJoinColumns = @JoinColumn(name = "kennel_characteristic_id"))
    private Set<KennelCharacteristic> kennelCharacteristics = new HashSet<>();

    // many to one
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kennel_id")
    private Kennel kennel;

    // many to one
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "police_force_id")
    private PoliceForce policeForce;

    // list endpoint only returns deleted which are false
    private boolean deleted = false;

    // auditing
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Dog() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public DogStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(DogStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public LocalDate getLeavingDate() {
        return leavingDate;
    }

    public void setLeavingDate(LocalDate leavingDate) {
        this.leavingDate = leavingDate;
    }

    public DogLeavingReason getLeavingReason() {
        return leavingReason;
    }

    public void setLeavingReason(DogLeavingReason leavingReason) {
        this.leavingReason = leavingReason;
    }

    public Set<KennelCharacteristic> getKennelCharacteristics() {
        return kennelCharacteristics;
    }

    public void setKennelCharacteristics(Set<KennelCharacteristic> kennelCharacteristics) {
        this.kennelCharacteristics = kennelCharacteristics;
    }

    public Kennel getKennel() {
        return kennel;
    }

    public void setKennel(Kennel kennel) {
        this.kennel = kennel;
    }

    public PoliceForce getPoliceForce() {
        return policeForce;
    }

    public void setPoliceForce(PoliceForce policeForce) {
        this.policeForce = policeForce;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

