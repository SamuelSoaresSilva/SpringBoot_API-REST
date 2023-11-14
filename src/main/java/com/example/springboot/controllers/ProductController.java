package com.example.springboot.controllers;

import com.example.springboot.dtos.ProductRecordDto;

import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Tag(name =  "Products")
@RequestMapping(value = "/api/products")
@CrossOrigin(origins = {"http://127.0.0.1:5500","http://localhost:3000","http://localhost:5173"})

public class ProductController {

    /*
     URL Local: http://localhost:8080/swagger-ui/index.html#/
     */

    @Autowired
    ProductRepository productRepository;

    @Operation(summary = "Posts a product to the database and then returns it")
    @PostMapping({"","/"})
    public ResponseEntity<?> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto){
        var productModel = new ProductModel();
        if (productRepository.existsByName(productRecordDto.name())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A product with this name already exists");
        }
        BeanUtils.copyProperties(productRecordDto, productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
    }

    @Operation(summary = "Calls all products in the bank and returns the list of products with their respective individual URLs")
    @GetMapping({"","/"})
    public ResponseEntity<?> getAllProductsByCategory(){
        List<ProductModel> productsList = productRepository.findAll();

        if (!productsList.isEmpty()) {

            Map<String, List<ProductModel>> productsByCategory = new HashMap<>();
            productsList.forEach(product -> {
                String category = product.getCategory();
                if (!productsByCategory.containsKey(category)) {
                    productsByCategory.put(category, new ArrayList<>());
                }
                productsByCategory.get(category).add(product);
                UUID id = product.getIdProduct();
                product.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
            });

            return ResponseEntity.status(HttpStatus.OK).body(productsByCategory);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no products yet");
        }
    }

    @Operation(summary = "Calls a product referenced by its ID and returns the product and the URL where all products are located")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id) {
            Optional<ProductModel> productO = productRepository.findById(id);
            if (productO.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
            }
            productO.get().add(linkTo(methodOn(ProductController.class).getAllProductsByCategory()).withRel("Product List"));
            return ResponseEntity.status(HttpStatus.OK).body(productO.get());
    }

    @Operation(summary = "Updates a product in the database referenced by ID and returns it already updated")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid ProductRecordDto productRecordDto) {
        Optional<ProductModel> productO = productRepository.findById(id);
        if (productO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        var productModel = productO.get();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel));
    }

    @Operation(summary = "Deletes a product referenced by ID and returns whether it was deleted or if the product was not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id){
        Optional<ProductModel> productO = productRepository.findById(id);
        if (productO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        productRepository.delete(productO.get());
        return ResponseEntity.status(HttpStatus.OK).body("Product have been deleted");
    }

    @Operation(summary = "Clean the database (Used for educational purposes only)")
    @DeleteMapping({"","/"})
    public ResponseEntity<Object> deleteAllProducts(){
        List<ProductModel> productsList = productRepository.findAll();
        if (productsList.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Product list is already empty");
        }else {
            productRepository.deleteAll();
            return ResponseEntity.status(HttpStatus.OK).body("All products have been deleted");
        }
    }
}
