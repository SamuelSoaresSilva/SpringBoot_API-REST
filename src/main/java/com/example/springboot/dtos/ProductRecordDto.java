package com.example.springboot.dtos;
//Impede que os valores sejam nulos
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.HashSet;

public record ProductRecordDto(
        @NotBlank String name, @NotNull BigDecimal value,
        @NotBlank String description, @NotBlank String brand,
        @NotNull Integer quantity, @NotBlank String category,
        @NotNull HashSet<@URL String> productImages) {
}
