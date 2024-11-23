package com.zosh.service.impl;

import com.zosh.domain.ORDER_TYPE;
import com.zosh.model.Order;
import com.zosh.model.Users;
import com.zosh.model.Wallet;
import com.zosh.repository.WalletRepository;
import com.zosh.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    @Autowired
    private final WalletRepository walletRepository;


    @Override
    public Wallet getUserWallet(Users user) {
        Wallet wallet = walletRepository.findByUserId(user.getId());
        if(wallet == null){
            wallet = new Wallet();
            wallet.setUser(user);
            walletRepository.save(wallet);
        }
        return wallet;
    }

    @Override
    public Wallet addBalance(Wallet wallet, Double balance) {
        BigDecimal amount = wallet.getBalance();
        BigDecimal newBalance = amount.add(BigDecimal.valueOf(balance));
        wallet.setBalance(newBalance);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findWalletById(Long id) throws Exception {
        Optional<Wallet> wallet = walletRepository.findById(id);
        if(wallet.isPresent()){
            return wallet.get();
        }
        throw new Exception("Wallet not found");
    }

    @Override
    public Wallet walletToWalletTransfer(Users user, Wallet receiverWallet, Long balance) throws Exception {
        Wallet senderWallet = getUserWallet(user);
        if(senderWallet.getBalance().compareTo(BigDecimal.valueOf(balance)) < 0){
            throw new Exception("Insufficient Balance");
        }
        BigDecimal senderBalance = senderWallet.getBalance().subtract(BigDecimal.valueOf(balance));
        senderWallet.setBalance(senderBalance);
        walletRepository.save(senderWallet);

        BigDecimal receiverBalance = receiverWallet.getBalance().add(BigDecimal.valueOf(balance));
        receiverWallet.setBalance(receiverBalance);
        walletRepository.save(receiverWallet);
        return senderWallet;
    }

    @Override
    public Wallet payOrderPayment(Order order, Users user) throws Exception {
        Wallet wallet = getUserWallet(user);
        if(order.getOrderType().equals(ORDER_TYPE.BUY)){
            BigDecimal newBalance = wallet.getBalance().subtract(order.getOrderPrice());
            if(newBalance.compareTo(BigDecimal.ZERO) < 0){
                throw new Exception("Insufficient fund for this transaction");
            }
            wallet.setBalance(newBalance);
        }
        else {
            BigDecimal newBalance = wallet.getBalance().add(order.getOrderPrice());
            wallet.setBalance(newBalance);
        }
        walletRepository.save(wallet);
        return wallet;
    }
}
