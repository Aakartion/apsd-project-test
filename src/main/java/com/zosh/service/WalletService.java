package com.zosh.service;

import com.zosh.model.Order;
import com.zosh.model.Users;
import com.zosh.model.Wallet;

public interface WalletService {

    Wallet getUserWallet(Users user);

    Wallet addBalance(Wallet wallet, Double balance);

    Wallet findWalletById(Long id) throws Exception;

    Wallet walletToWalletTransfer(Users user, Wallet receiverWallet, Long balance) throws Exception;

    Wallet payOrderPayment(Order order, Users user) throws Exception;
}
