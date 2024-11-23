package com.zosh.controller;

import com.zosh.model.Order;
import com.zosh.model.Users;
import com.zosh.model.Wallet;
import com.zosh.model.WalletTransaction;
import com.zosh.service.OrderService;
import com.zosh.service.UserService;
import com.zosh.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;
    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorization")String jwt) throws Exception {
        Users user = userService.findUserByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);

        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{walletId}/transfer")
    public ResponseEntity<Wallet> walletToWalletTransfer(
            @RequestHeader("Authorization")String jwt,
            @PathVariable Long walletId,
            @RequestBody WalletTransaction walletTransaction) throws Exception {
        Users senderUser = userService.findUserByJwt(jwt);
        Wallet receiverWallet = walletService.findWalletById(walletId);
        Wallet wallet = walletService.walletToWalletTransfer(senderUser, receiverWallet, walletTransaction.getAmount());

        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }


    @PutMapping("/order/{orderId}/pay")
    public ResponseEntity<Wallet> payOrderPayment(
            @RequestHeader("Authorization")String jwt,
            @PathVariable Long orderId) throws Exception {
        Users senderUser = userService.findUserByJwt(jwt);
        Order order = orderService.getOrderByOrderId(orderId);
        Wallet wallet = walletService.payOrderPayment(order, senderUser);
        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

}
