package io.dt3zr.mockashopweb.viewmodel;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class Order {
    private String username;
    private String status;
    private BigDecimal total;
    private String transactionId;
    private Date transactionDate;
    private Set<OrderItem> orderItems = new HashSet<>();
}
