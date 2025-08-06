package com.pointofsales.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shift_log")
public class ShiftLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "cashier_id")
    private Cashier cashier;

    @Column(name = "shift_date")
    private LocalDate shiftDate;

    @Column(name = "time_in")
    private LocalDateTime timeIn;

    @Column(name = "time_out")
    private LocalDateTime timeOut;

    @Column(name = "opening_balance")
    private double openingBalance;

    @Column(name = "closing_balance")
    private double closingBalance;

    @Column(name = "total_sales")
    private double totalSales;

    @Column(name = "transaction_count")
    private int transactionCount;
}
