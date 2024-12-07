package com.jkm.jimkanman.repository;

import com.jkm.jimkanman.domain.StorageReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageReservationRepository extends JpaRepository<StorageReservation, Long> {

    List<StorageReservation> findAllByStorageIdAndMemberIdOrderByEndDateTimeAsc(Long storageId, Long memberId);

    @Query("SELECT sr FROM StorageReservation sr " +
            "JOIN FETCH sr.member m " +
            "JOIN FETCH sr.storage s " +
            "LEFT JOIN FETCH s.storageImages " +
            "WHERE m.id = :memberId")
    List<StorageReservation> findAllByMemberId(Long memberId);

    List<StorageReservation> findAllByStorageId(Long storageId);
}
