package com.example.springboot.dtos;
//Impede que os valores sejam nulos
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRecordDto(@NotNull String image,@NotBlank String name, @NotNull BigDecimal value) {
}
