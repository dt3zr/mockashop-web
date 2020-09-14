package io.dt3zr.mockashopweb.viewmodel;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ProductForm {

    @NotEmpty(message = "Product code must not be empty")
    private String code;
    @NotEmpty(message = "Product type must not be empty")
    private String type;
    @NotEmpty(message = "Product name must not be empty")
    private String name;
    @NotEmpty(message = "Product description must not be empty")
    private String description;
    @NotNull(message = "Product unit price must not be empty")
    @DecimalMin(value = "0.00", message = "Price must equals to or larger than 0.00")
    private BigDecimal price;
}
