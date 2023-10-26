package com.example.springboot.services;

import com.example.springboot.models.ImageModel;
import com.example.springboot.repositories.ImageRepository;
import com.example.springboot.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Value("${images.upload.path}")
    private String FOLDER_PATH;

    public ImageService() {
    }

    public String uploadImage(MultipartFile file) throws IOException {


            ImageModel imageModel = imageRepository.save(ImageModel.builder()
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .imgByte(ImageUtils.compressImage(file.getBytes())).build());

            if (imageModel != null) {
                return "file uploaded successfully : " + file.getOriginalFilename()
                        + "\n http://localhost:8080/api/products/image/" + file.getOriginalFilename();
            }
            return null;
        }


        public byte[] downloadImage (String fileName){
            Optional<ImageModel> dbImageData = imageRepository.findByName(fileName);
            byte[] images = ImageUtils.decompressImage(dbImageData.get().getImgByte());
            return images;
        }

        public boolean doesImageExist (String imageName){
            return imageRepository.existsByName(imageName);
        }
}
