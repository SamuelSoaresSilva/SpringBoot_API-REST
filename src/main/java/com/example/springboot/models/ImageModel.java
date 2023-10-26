package com.example.springboot.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@Table(name = "TB_IMAGES")
public class ImageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idImage;
    private String name;
    private String type;

    @Column(length = 5000000)
    private byte[] imgByte;

    public ImageModel(){

    }
}
