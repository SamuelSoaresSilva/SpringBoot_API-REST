package com.example.springboot.models;

import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "TB_PRODUCTS")
public class ProductModel extends RepresentationModel<ProductModel> implements Serializable {

    private static final long serialVersionUID = 1L;

    //Cada atributo aqui exceto o ID, deve ser adicionado aos parametros do Objeto de transferencia de dados "DTO"
    //com a notacao @NotNull

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private UUID idProduct;

    //Em uma aplicação mais especifica, o certo seria declarar uma entidade separada indicando o endereço
    //de cada uma das imagens de um deivido produto
    private String image;
    private String name;
    private BigDecimal value;
    private String description;
    private String brand;
    private Integer quantity;
    private String category;


    public UUID getIdProduct() {
        return idProduct;
    }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getBrand() { return brand; }

    public void setBrand(String brand) { this.brand = brand; }

    public Integer getQuantity() { return quantity; }

    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getCategory(){ return category; }

    public void setCategory(String category) { this.category = category; }
}
