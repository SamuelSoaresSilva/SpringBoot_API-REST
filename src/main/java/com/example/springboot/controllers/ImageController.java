package com.example.springboot.controllers;

import com.example.springboot.models.ImageModel;
import com.example.springboot.models.InternalImageModel;
import com.example.springboot.repositories.ImageRepository;
import com.example.springboot.repositories.InternalImageRepository;
import com.example.springboot.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/products")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ImageController {

    @Autowired
    ImageService imageService;

    @Autowired
    InternalImageRepository internalImageRepository;
    @Autowired
    ImageRepository imageRepository;

    @PostMapping({"/image","/image/"})
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        String uploadImage = imageService.uploadImage(file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(uploadImage);
    }

    @GetMapping("/image/{fileName}")
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
    @DeleteMapping({"/image/","/image"})
    public ResponseEntity<String> clearImageRepository(){
        List<ImageModel> imageList = imageRepository.findAll();
        if (imageList.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Product list is already empty");
        }
        imageRepository.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).body("All images has been deleted");
    }

    @PostMapping({"/internalImage/","/internalImage"})
    public ResponseEntity uploadImageToInternal(@RequestParam("image")MultipartFile file) throws IOException{
        var uploadImage = imageService.uploadInternalImage(file);
        return uploadImage;
    }

    @GetMapping("/internalImage/{fileName}")
    public ResponseEntity<?> downloadInternalImage(@PathVariable String fileName) {
        try {
            byte[] imageData = imageService.downloadImageFromInternal(fileName);
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.valueOf("image/png"))
                    .body(imageData);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Image not found: " + fileName);
        }
    }

    @DeleteMapping("/internalImage/{fileName}")
    public ResponseEntity deleteImage(@PathVariable String fileName) throws IOException {

        return imageService.deleteImage(fileName);
    }

    @DeleteMapping({"/internalImage/","/internalImage"})
    public ResponseEntity clearAllImages() {
        List<InternalImageModel> images = internalImageRepository.findAll();
        if (!images.isEmpty()) {
            for (InternalImageModel image : images) {
                String filePath = imageService.getFOLDER_PATH() + image.getName();
                File file = new File(filePath);
                if (file.exists() && file.delete()) {
                    internalImageRepository.delete(image);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to delete files or files do not exist");
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body("All images have been deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Repository is already empty");
        }
    }


}
