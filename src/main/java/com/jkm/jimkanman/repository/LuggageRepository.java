package com.jkm.jimkanman.repository;

import com.jkm.jimkanman.domain.Luggage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LuggageRepository extends JpaRepository<Luggage, Long> {

}
