package com.jkm.jimkanman.repository;

import com.jkm.jimkanman.domain.Storage;
import com.jkm.jimkanman.domain.StorageReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Long> {
    List<Storage> findByLatitudeBetweenAndLongitudeBetween(double minLat, double maxLat, double minLng, double maxLng);
    List<Storage> findByNameContaining(String name);

    List<Storage> findAllByOwnerId(Long memberId);

    @Query("SELECT sr FROM StorageReservation sr " +
            "JOIN FETCH sr.storage s " +
            "JOIN FETCH s.owner o " +
            "LEFT JOIN FETCH s.storageImages " +
            "WHERE o.id = :ownerId")
    List<StorageReservation> findAllReservationsByOwnerId(Long ownerId);
}
