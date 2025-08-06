package com.pointofsales.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Receipt {

    private String name;

    private Integer quantity;

    private Double amount;

    private String category;

}
