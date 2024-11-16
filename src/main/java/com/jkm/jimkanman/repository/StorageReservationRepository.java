package com.jkm.jimkanman.repository;

import com.jkm.jimkanman.domain.StorageReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageReservationRepository extends JpaRepository<StorageReservation, Long> {

    List<StorageReservation> findByStorageIdAndMemberIdOrderByEndDateTimeAsc(Long storageId, Long memberId);
}
