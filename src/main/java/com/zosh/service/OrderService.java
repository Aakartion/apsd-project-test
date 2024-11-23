package com.zosh.service;

import com.zosh.domain.ORDER_TYPE;
import com.zosh.model.Coins;
import com.zosh.model.Order;
import com.zosh.model.OrderItem;
import com.zosh.model.Users;

import java.util.List;

public interface OrderService {

    Order createOrder(Users user, OrderItem orderItem, ORDER_TYPE orderType);

    Order getOrderByOrderId(Long orderId) throws Exception;

    List<Order> getAllOrdersofUser(Long userId, ORDER_TYPE orderType, String assetSymbol);

    Order processOrder(Coins coin, double quantity, ORDER_TYPE orderType, Users user) throws Exception;
}
