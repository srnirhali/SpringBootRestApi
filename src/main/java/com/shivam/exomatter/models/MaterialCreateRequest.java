package com.shivam.exomatter.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MaterialCreateRequest {

    @NotBlank(message = "formula value is Missing")
    private String formula;

    @NotNull(message = "density value is Missing")
    @Positive(message = "density value cannot be negative or zero")
    private Double density;
}
