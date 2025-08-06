package com.pointofsales.entity;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class Total {

    private String payment = "Payment Amount: ";

    private String paymentAmount;

    private String totalItems;

    private Double totalPrice;
}
