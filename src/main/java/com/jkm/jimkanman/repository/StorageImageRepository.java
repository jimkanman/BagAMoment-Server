package com.jkm.jimkanman.repository;

import com.jkm.jimkanman.domain.StorageImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageImageRepository extends JpaRepository<StorageImage, Long> {
}
