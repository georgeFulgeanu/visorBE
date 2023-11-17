package com.abac.visorbe2.repositories;

import com.abac.visorbe2.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
