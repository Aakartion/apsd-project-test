package com.zosh.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderItemId;

    private double quantity;

    @ManyToOne
    private Coins coin;

    private double buyPrice;
    private double sellPrice;
    @OneToOne
    @JsonIgnore
    private Order order;
}
