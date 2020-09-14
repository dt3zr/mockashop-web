package io.dt3zr.mockashopweb.viewmodel;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItem {
    private String productCode;
    private String name;
    private String description;
    private Long quantity;
    private BigDecimal price;
}
