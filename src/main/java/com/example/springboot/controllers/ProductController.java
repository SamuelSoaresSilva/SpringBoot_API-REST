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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Tag(name =  "Products")
@RequestMapping(value = "/products")
@CrossOrigin(origins = "http://127.0.0.1:5500")
//Rest Controller é uma derivada da @Controller, mas direcionada a uma api Restful
public class ProductController {

    /*
     URL Local: http://localhost:8080/swagger-ui/index.html#/
     */


    @Autowired
    ProductRepository productRepository;
    //Logo após, criei um DTO Data Transfer Object

    @Operation(summary = "Add um produto no banco")
    @PostMapping
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto){
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
        //Metodo Post pronto porem sem customização de mensagem de erro ou de exceptions
    }

    @Operation(summary = "Busca todos os produtos presente no banco")
    @GetMapping()
    public ResponseEntity<List<ProductModel>> getAllProducts(){
        List<ProductModel> productsList = productRepository.findAll();
        if (!productsList.isEmpty()){
            //JAVA Streams map function
            productsList.forEach(product -> {
                UUID id = product.getIdProduct();
                product.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
            });
            return ResponseEntity.status(HttpStatus.OK).body(productsList);
        }else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(productsList);
        }
    }

    @Operation(summary = "Busca um produto no banco pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id){
        Optional<ProductModel> productO = productRepository.findById(id);
        if (productO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        productO.get().add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("Product List"));
        return ResponseEntity.status(HttpStatus.OK).body(productO.get());
    }

    @Operation(summary = "Atualiza um produto no banco pelo ID")
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

    @Operation(summary = "Deleta um produto no banco pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id){
        Optional<ProductModel> productO = productRepository.findById(id);
        if (productO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        productRepository.delete(productO.get());
        return ResponseEntity.status(HttpStatus.OK).body("Product has been deleted");
    }

    @Operation(summary = "Deleta todos os produtos do banco")
    @DeleteMapping()
    public ResponseEntity<Object> deleteAllProducts(){
        List<ProductModel> productsList = productRepository.findAll();
        if (productsList.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Product list is already empty");
        }else {
            productRepository.deleteAll();
            return ResponseEntity.status(HttpStatus.OK).body("All products has been deleted");
        }
    }
}
