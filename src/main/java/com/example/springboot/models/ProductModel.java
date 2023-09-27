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
    private String image;
    private String name;
    private BigDecimal value;


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
}
