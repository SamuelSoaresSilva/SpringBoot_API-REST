package com.example.springboot.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.UUID;
@Data
@Entity
@Table(name = "TB_PRODUCTS")
public class ProductModel extends RepresentationModel<ProductModel> implements Serializable {

    private static final long serialVersionUID = 1L;
    //Cada atributo aqui exceto o ID, deve ser adicionado aos parametros do Objeto de transferencia de dados "DTO"
    //com a notacao @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idProduct;
    //Em uma aplicação mais especifica, o certo seria declarar uma entidade separada indicando o endereço
    //de cada uma das imagens de um deivido produto
    private String name;
    private BigDecimal value;
    private String description;
    private String brand;
    private Integer quantity;
    private String category;
    private String thumbnail;

    private HashSet<String> productImages;

}
