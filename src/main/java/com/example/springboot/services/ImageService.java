package com.example.springboot.services;


import com.example.springboot.models.ImageModel;

import com.example.springboot.models.InternalImageModel;
import com.example.springboot.repositories.ImageRepository;
import com.example.springboot.repositories.InternalImageRepository;
import com.example.springboot.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private InternalImageRepository internalImageRepository;

   // @Value("${images.upload.path}")
   //Path pc samuel "C:\\Users\\samip\\ProjetosSpringBoot\\SpringBoot_API-REST\\src\\main\\resources\\static\\Images\\";
    private final String FOLDER_PATH = "C:\\Users\\samip\\ProjetosSpringBoot\\SpringBoot_API-REST\\src\\main\\resources\\static\\Images\\";

    public String getFOLDER_PATH() {
        return FOLDER_PATH;
    }

    public String uploadImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return "You need to select a file to upload";
        }
        if (file.getOriginalFilename().contains(" ")) {
            return "File name cannot have spaces";
        }
        if (imageRepository.existsByName(file.getOriginalFilename())) {
            return "A file with this name already exists";
        }

        ImageModel imageModel = imageRepository.save(ImageModel.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imgByte(ImageUtils.compressImage(file.getBytes()))
                .build());

        if (imageModel != null) {
            return "File uploaded successfully: " + file.getOriginalFilename();
        }
        return null;
    }

    public byte[] downloadImage (String fileName){
            Optional<ImageModel> dbImageData = imageRepository.findByName(fileName);
            byte[] images = ImageUtils.decompressImage(dbImageData.get().getImgByte());
            return images;
        }

    public ResponseEntity<String> uploadInternalImage(MultipartFile file) throws IOException{
        String filePath = FOLDER_PATH+file.getOriginalFilename();
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You need to select a file to upload");
        }
        if (file.getOriginalFilename().contains(" ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File name cannot have spaces");
        }
        if (internalImageRepository.existsByName(file.getOriginalFilename())) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("A file with this name already exists");
        }

        InternalImageModel internalImageModel = internalImageRepository.save(InternalImageModel.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .filePath(filePath).build());

        file.transferTo(new File(filePath));

        if (internalImageModel != null){
         return ResponseEntity.status(HttpStatus.OK).body("file uploaded successfully: \n"+filePath);
        }
        return null;
    }

    public byte[] downloadImageFromInternal(String fileName) throws IOException {
        Optional<InternalImageModel> dbInternalImage = internalImageRepository.findByName(fileName);

        if (dbInternalImage.isPresent()) {
            String filePath = FOLDER_PATH + fileName;
            Path path = Paths.get(filePath);

            if (Files.exists(path)) {
                return Files.readAllBytes(path);
            } else {
                throw new IOException("File not found: " + fileName);
            }
        } else {
            throw new IOException("File not found in the database: " + fileName);
        }
    }

    public ResponseEntity deleteImage(String fileName) throws IOException {
        Optional<InternalImageModel> image = internalImageRepository.findByName(fileName);
        if (image.isPresent()) {
            String filePath = FOLDER_PATH + fileName;
            File file = new File(filePath);
            if (file.exists() && file.delete()) {
                internalImageRepository.delete(image.get());
                return ResponseEntity.status(HttpStatus.OK).body("Image " + fileName + " has been deleted");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to delete " + fileName + " or it does not exist");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found in the repository: "+fileName);
        }
    }

}
