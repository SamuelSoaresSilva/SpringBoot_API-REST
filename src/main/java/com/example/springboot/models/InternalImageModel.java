package com.example.springboot.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Table(name = "TB_INTERNAL_IMAGE")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InternalImageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idInternalImage;

    private String name;
    private String type;

    private String filePath;

}
