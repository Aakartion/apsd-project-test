package com.zosh.model;

import com.zosh.domain.ORDER_STATUS;
import com.zosh.domain.ORDER_TYPE;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne    // One user has many order
    private Users user;
    @Column(nullable = false)
    private ORDER_TYPE orderType;
    @Column(nullable = false)
    private BigDecimal orderPrice;
    private LocalDateTime orderTimeStamp = LocalDateTime.now();
    @Column(nullable = false)
    private ORDER_STATUS orderStatus;
    @OneToOne(mappedBy = "oder", cascade = CascadeType.ALL)
    private OrderItem orderItem;
}
