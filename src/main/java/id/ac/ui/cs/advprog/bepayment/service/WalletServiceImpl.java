package id.ac.ui.cs.advprog.bepayment.service;

import id.ac.ui.cs.advprog.bepayment.enums.TopUpStatus;
import id.ac.ui.cs.advprog.bepayment.model.TopUp;
import id.ac.ui.cs.advprog.bepayment.model.TopUpBuilder;
import id.ac.ui.cs.advprog.bepayment.model.Wallet;
import id.ac.ui.cs.advprog.bepayment.pojos.WalletRequest;
import id.ac.ui.cs.advprog.bepayment.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class WalletServiceImpl implements WalletService{
    @Autowired
    private WalletRepository walletRepository;

    @Override
    @Async
    @Transactional
    public CompletableFuture<Wallet> createWallet(WalletRequest walletRequest) {
        String walletId = String.valueOf(UUID.randomUUID());
        Wallet wallet = Wallet.builder()
                .id(walletId)
                .userId(walletRequest.userId)
                .amount(0)
                .build();
        return CompletableFuture.completedFuture(walletRepository.save(wallet));
    }

    @Override
    @Async
    @Transactional
    public CompletableFuture<Void> addAmount(String walletId, double totalAmount) {
        walletRepository.addAmount(walletId, totalAmount);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    @Transactional
    public CompletableFuture<Wallet> findById(String walletId) {
        return CompletableFuture.completedFuture(walletRepository.findById(walletId));
    }

    @Override
    @Async
    @Transactional
    public CompletableFuture<Wallet> findByUserId(String userId) {
        return CompletableFuture.completedFuture(walletRepository.findByUserId(userId));
    }
}
