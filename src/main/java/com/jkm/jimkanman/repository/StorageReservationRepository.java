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
            "WHERE m.id = :memberId " +
            "ORDER BY sr.createdAt ")
    List<StorageReservation> findAllByMemberId(Long memberId);

    @Query("SELECT sr FROM StorageReservation sr " +
            "JOIN FETCH sr.storage s " +
            "JOIN FETCH s.owner o " +
            "LEFT JOIN FETCH s.storageImages " +
            "WHERE o.id = :ownerId "+
            "ORDER BY sr.createdAt DESC")
    List<StorageReservation> findAllByStorageOwnerIdOrderByCreatedAtDesc(Long ownerId);

    List<StorageReservation> findAllByStorageId(Long storageId);
}
