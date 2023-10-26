package com.example.springboot.controllers;

import com.example.springboot.models.ImageModel;
import com.example.springboot.repositories.ImageRepository;
import com.example.springboot.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/products/image")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ImageController {

    @Autowired
    ImageService imageService;

    @Autowired
    ImageRepository imageRepository;

    @PostMapping({"/",""})
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        String uploadImage = imageService.uploadImage(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName){
        if (fileName.isEmpty()||fileName.isBlank()||fileName.contains(" ")){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body("The file you are looking for must have a valid name");
        }
        if (!imageRepository.existsByName(fileName)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The file you are looking for doesn't exists");
        }
        byte[] imageData = imageService.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }
}
