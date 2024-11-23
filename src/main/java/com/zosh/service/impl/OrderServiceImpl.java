package com.zosh.service.impl;

import com.zosh.domain.ORDER_STATUS;
import com.zosh.domain.ORDER_TYPE;
import com.zosh.model.*;
import com.zosh.repository.OrderItemRepository;
import com.zosh.repository.OrderRepository;
import com.zosh.repository.WalletRepository;
import com.zosh.service.AssetService;
import com.zosh.service.OrderService;
import com.zosh.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {


    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final WalletRepository walletRepository;

    @Autowired
    private final OrderItemRepository orderItemRepository;
    @Autowired
    private WalletService walletService;

    @Autowired
    private AssetService assetService;

    @Override
    public Order createOrder(Users user, OrderItem orderItem, ORDER_TYPE orderType) {
        double price = orderItem.getCoin().getCurrentPrice() * orderItem.getQuantity();
        Order order = new Order();
        order.setUser(user);
        order.setOrderType(orderType);
        order.setOrderPrice(BigDecimal.valueOf(price));
        order.setOrderItem(orderItem);
        order.setOrderTimeStamp(LocalDateTime.now());
        order.setOrderStatus(ORDER_STATUS.PENDING);
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderByOrderId(Long orderId) throws Exception {
        return orderRepository.findById(orderId).orElseThrow(()-> new Exception("Order not found"));
    }

    @Override
    public List<Order> getAllOrdersofUser(Long userId, ORDER_TYPE orderType, String assetSymbol) {
        return orderRepository.findOrdersByUserId(userId);
    }

    private OrderItem createOrderItem(Coins coin,
                                      double quantity,
                                      double buyPrice,
                                      double sellPrice) {
        OrderItem orderItem = new OrderItem();
        orderItem.setCoin(coin);
        orderItem.setBuyPrice(buyPrice);
        orderItem.setSellPrice(sellPrice);
        orderItem.setQuantity(quantity);
        return orderItemRepository.save(orderItem);
    }

    @Transactional
    public Order buyAsset(Coins coin, double quantity, Users user) throws Exception {
        if(quantity <= 0){
            throw new Exception("Quantity should be > 0");
        }
        double buyPrice = coin.getCurrentPrice() * quantity;
        OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, 0);
        Order order = createOrder(user, orderItem, ORDER_TYPE.BUY);
        orderItem.setOrder(order);
        walletService.payOrderPayment(order, user);
        order.setOrderStatus(ORDER_STATUS.SUCCESS);
        order.setOrderType(ORDER_TYPE.BUY);
        Order savedOrder = orderRepository.save(order);

//        Create Asset

        Asset oldAsset = assetService.findAssetByUserIdAndCoinId(order.getUser().getId(), order.getOrderItem().getCoin().getId());
        if(oldAsset == null){
            assetService.createAsset(user, orderItem.getCoin(), orderItem.getQuantity());
        }else {
            assetService.updateAsset(oldAsset.getAssetId(), quantity);
        }

        return savedOrder;
    }

    @Transactional
    public Order sellAsset(Coins coin, double quantity, Users user) throws Exception {
        if(quantity <= 0){
            throw new Exception("Quantity should be > 0");
        }
        double sellPrice = coin.getCurrentPrice() * quantity;

        Asset assetToSell = assetService.findAssetByUserIdAndCoinId(user.getId(), coin.getId());
        double buyPrice = assetToSell.getAssetBuyPrice();

        if(assetToSell != null){
            OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, sellPrice);
            Order order = createOrder(user, orderItem, ORDER_TYPE.SELL);
            orderItem.setOrder(order);

            if(assetToSell.getAssetQuantity() >= quantity){
                order.setOrderStatus(ORDER_STATUS.SUCCESS);
                order.setOrderType(ORDER_TYPE.SELL);
                Order savedOrder = orderRepository.save(order);

                walletService.payOrderPayment(order, user);



                Asset updatedAsset = assetService.updateAsset(assetToSell.getAssetId(), -quantity);
                if(updatedAsset.getAssetQuantity() * coin.getCurrentPrice() <= 1){
                    assetService.deleteAsset(updatedAsset.getAssetId());
                }
                return savedOrder;
            }
            throw new Exception("Insufficient Quantity to sell");
        }
        throw new Exception("Asset not found");

    }

    @Override
    @Transactional
    public Order processOrder(Coins coin, double quantity, ORDER_TYPE orderType, Users user) throws Exception {
        if(orderType.equals(ORDER_TYPE.BUY)){
            return buyAsset(coin, quantity, user);
        } else if (orderType.equals(ORDER_TYPE.SELL)) {
            return sellAsset(coin, quantity, user);
        }
        throw new Exception("Invalid Order Type");
    }
}
