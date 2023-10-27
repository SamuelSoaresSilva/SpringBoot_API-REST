package com.example.springboot.repositories;

import com.example.springboot.models.ImageModel;
import com.example.springboot.models.InternalImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InternalImageRepository extends JpaRepository<InternalImageModel, UUID> {

    Optional<InternalImageModel> findByName(String fileName);

    boolean existsByName(String originalFilename);
}
