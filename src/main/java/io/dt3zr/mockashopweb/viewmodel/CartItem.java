package io.dt3zr.mockashopweb.viewmodel;

import lombok.Data;

@Data
public class CartItem {
    private String username;
    private String productCode;
    private long quantity;
}
