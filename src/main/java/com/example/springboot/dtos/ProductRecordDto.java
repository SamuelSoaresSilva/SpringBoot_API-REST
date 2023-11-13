package com.example.springboot.dtos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.HashSet;

public record ProductRecordDto(
        @NotBlank String name, @NotNull BigDecimal value,
        @NotBlank String description, @NotBlank String brand,
        @NotNull Integer quantity, @NotBlank String category,
        @NotBlank @URL String thumbnail,
        @NotNull HashSet<@NotBlank @URL String> productImages) {
}
