package com.chocksaway.mapper;

import com.chocksaway.command.DogSaveOrUpdateCommand;
import com.chocksaway.domain.Dog;
import com.chocksaway.domain.DogLeavingReason;
import com.chocksaway.domain.DogStatus;
import com.chocksaway.domain.Kennel;
import com.chocksaway.domain.KennelCharacteristic;
import com.chocksaway.domain.PoliceForce;
import jakarta.persistence.EntityManager;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.chocksaway.dto.DogDto;

import jakarta.inject.Inject;
import com.chocksaway.exception.MissingReferenceException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


// Creates DogMapperImpl
@Mapper(componentModel = "jsr330")
public abstract class DogMapper {

    @Mapping(target = "currentStatus", source = "currentStatusId")
    @Mapping(target = "leavingReason", source = "leavingReasonId")
    @Mapping(target = "kennel", source = "kennelId")
    @Mapping(target = "policeForce", source = "policeForceId")
    @Mapping(target = "kennelCharacteristics", source = "characteristicIds")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)

    public abstract Dog toEntity(DogSaveOrUpdateCommand cmd);

    @Mapping(target = "currentStatus", source = "currentStatusId")
    @Mapping(target = "leavingReason", source = "leavingReasonId")
    @Mapping(target = "kennel", source = "kennelId")
    @Mapping(target = "policeForce", source = "policeForceId")
    @Mapping(target = "kennelCharacteristics", source = "characteristicIds")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)

    public abstract void updateFromCommand(DogSaveOrUpdateCommand cmd, @MappingTarget Dog dog);

    @Mapping(target = "currentStatusId", source = "currentStatus")
    @Mapping(target = "kennelId", source = "kennel")
    @Mapping(target = "policeForceId", source = "policeForce")
    @Mapping(target = "characteristicIds", source = "kennelCharacteristics")
    public abstract DogDto toDto(Dog dog);

    @Inject
    protected EntityManager em;

    protected DogStatus map(Long id) {
        if (id == null) return null;
        DogStatus ds = em.find(DogStatus.class, id);
        if (ds == null) throw new MissingReferenceException("DogStatus not found for id: " + id);
        return ds;
    }

    protected DogLeavingReason mapToDogLeavingReason(Long id) {
        if (id == null) return null;
        DogLeavingReason lr = em.find(DogLeavingReason.class, id);
        if (lr == null) throw new MissingReferenceException("DogLeavingReason not found for id: " + id);
        return lr;
    }

    protected Kennel mapToKennel(Long id) {
        if (id == null) return null;
        Kennel k = em.find(Kennel.class, id);
        if (k == null) throw new MissingReferenceException("Kennel not found for id: " + id);
        return k;
    }

    protected PoliceForce mapToPoliceForce(Long id) {
        if (id == null) return null;
        PoliceForce pf = em.find(PoliceForce.class, id);
        if (pf == null) throw new MissingReferenceException("PoliceForce not found for id: " + id);
        return pf;
    }

    protected Set<KennelCharacteristic> map(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return new HashSet<>();
        Set<KennelCharacteristic> set = new HashSet<>();
        for (Long id : ids) {
            if (id == null) continue;
            KennelCharacteristic kc = em.find(KennelCharacteristic.class, id);
            if (kc == null) throw new MissingReferenceException("KennelCharacteristic not found for id: " + id);
            set.add(kc);
        }
        return set;
    }

    protected Long map(DogStatus ds) {
        return ds == null ? null : ds.getId();
    }

    protected Long mapToKennel(Kennel k) {
        return k == null ? null : k.getId();
    }

    protected Long mapToPoliceForce(PoliceForce pf) {
        return pf == null ? null : pf.getId();
    }

    protected java.util.List<Long> map(Set<KennelCharacteristic> set) {
        if (set == null || set.isEmpty()) return java.util.Collections.emptyList();
        java.util.List<Long> ids = new java.util.ArrayList<>();
        for (KennelCharacteristic kc : set) {
            if (kc != null) ids.add(kc.getId());
        }
        return ids;
    }

}
