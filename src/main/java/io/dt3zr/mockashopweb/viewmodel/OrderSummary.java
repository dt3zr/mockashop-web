package io.dt3zr.mockashopweb.viewmodel;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderSummary {
    private String status;
    private BigDecimal total;
    private String transactionId;
    private Date transactionDate;
}
