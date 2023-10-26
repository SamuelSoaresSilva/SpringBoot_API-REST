package com.example.springboot.repositories;

import com.example.springboot.models.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<ImageModel, UUID> {

    Optional<ImageModel> findByName(String fileName);

    boolean existsByName(String fileName);
}
