package com.zosh.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long assetId;
    private double assetQuantity;
    private double assetBuyPrice;
    @ManyToOne
    private Coins coins;
    @ManyToOne
    private Users user;
}
