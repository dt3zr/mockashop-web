package io.dt3zr.mockashopweb.viewmodel;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class ShoppingCartItems {
    private String username;
    private Set<ShoppingCartItem> shoppingCartItems = new HashSet<>();
}
