package org.example.pojo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProjectPrices {
    private Long workerId;
    private BigDecimal price;
}
