package id.ac.ui.cs.advprog.bepayment.service;

import id.ac.ui.cs.advprog.bepayment.model.Wallet;
import id.ac.ui.cs.advprog.bepayment.pojos.WalletRequest;
import id.ac.ui.cs.advprog.bepayment.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
                .userId(walletRequest.getUserId())
                .amount(0)
                .build();
        return CompletableFuture.completedFuture(walletRepository.save(wallet));
    }

    @Override
    @Async
    @Transactional
    public CompletableFuture<Void> addAmount(String walletId, double totalAmount) {
        CompletableFuture<Wallet> walletFuture = findById(walletId);
        return walletFuture.thenCompose(wallet -> {
            double finalAmount = wallet.getAmount() + totalAmount;
            walletRepository.setAmount(walletId, finalAmount);
            return CompletableFuture.completedFuture(null);
        });
    }


    @Override
    @Async
    @Transactional
    public CompletableFuture<Void> decreaseAmount(String walletId, double totalAmount) throws ExecutionException, InterruptedException {
        CompletableFuture<Wallet> walletFuture = findById(walletId);
        return walletFuture.thenCompose(wallet -> {
            double finalAmount = wallet.getAmount() - totalAmount;
            if (finalAmount < 0) {
                return CompletableFuture.failedFuture(new IllegalArgumentException("Final amount cannot be negative"));
            }
            walletRepository.setAmount(walletId, finalAmount);
            return CompletableFuture.completedFuture(null);
        });
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
