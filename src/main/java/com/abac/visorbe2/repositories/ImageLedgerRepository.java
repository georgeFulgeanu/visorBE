package com.abac.visorbe2.repositories;

import com.abac.visorbe2.dto.response.CurrentImageDto;
import com.abac.visorbe2.entities.ImageLedger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageLedgerRepository extends JpaRepository<ImageLedger, Long> {
    Optional<ImageLedger> findTopByOrderByIdDesc();
}
