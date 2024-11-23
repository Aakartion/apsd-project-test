package com.zosh.controller;

import com.zosh.domain.ORDER_TYPE;
import com.zosh.model.Coins;
import com.zosh.model.Order;
import com.zosh.model.Users;
import com.zosh.request.CreateOrderRequest;
import com.zosh.service.CoinsService;
import com.zosh.service.OrderService;
import com.zosh.service.UserService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private CoinsService coinsService;
//    @Autowired
//    private WalletTransactionService walletTransactionService;


    @PostMapping("/pay")
    public ResponseEntity<Order> payOrderPayment(@RequestHeader("Authorization") String jwtToken,
                                                 @RequestBody CreateOrderRequest createOrderRequest) throws Exception {
        Users user = userService.findUserByJwt(jwtToken);
        Coins coin = coinsService.findById(createOrderRequest.getCoinId());

        Order order = orderService.processOrder(coin, createOrderRequest.getQuantity(), createOrderRequest.getOrderType(), user);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@RequestHeader("Authorization") String jwtToken,
                                              @PathVariable Long orderId) throws Exception {
        if(jwtToken == null){
            throw new Exception("Token is Missing");
        }
        Users user = userService.findUserByJwt(jwtToken);
        Order order = orderService.getOrderByOrderId(orderId);
        if(order.getUser().getId().equals(user.getId())){
            return ResponseEntity.ok(order);
        }
        else {
            throw new Exception("Order Not Found");
        }
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrdersForUser(
            @RequestHeader("Authorization") String jwtToken,
            @RequestParam(required = false) ORDER_TYPE order_type,
            @RequestParam(required = false) String asset_symbol
    ) throws Exception {
        Long userId = userService.findUserByJwt(jwtToken).getId();
        List<Order> userOrders = orderService.getAllOrdersofUser(userId, order_type, asset_symbol);
        return ResponseEntity.ok(userOrders);
    }
}
