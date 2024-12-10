package com.jkm.jimkanman.repository;

import com.jkm.jimkanman.domain.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Long> {
    List<Storage> findByLatitudeBetweenAndLongitudeBetween(double minLat, double maxLat, double minLng, double maxLng);
    List<Storage> findByNameContaining(String name);

    List<Storage> findAllByOwnerIdOrderByCreatedAtDesc(Long memberId);


}
