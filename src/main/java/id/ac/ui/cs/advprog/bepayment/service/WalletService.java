package id.ac.ui.cs.advprog.bepayment.service;

import id.ac.ui.cs.advprog.bepayment.model.TopUp;
import id.ac.ui.cs.advprog.bepayment.model.Wallet;
import id.ac.ui.cs.advprog.bepayment.pojos.TopUpRequest;
import id.ac.ui.cs.advprog.bepayment.pojos.WalletRequest;

import java.util.concurrent.CompletableFuture;

public interface WalletService {
    public CompletableFuture<Wallet> createWallet(WalletRequest walletRequest);
    public CompletableFuture<Void> addAmount(String walletId, double totalAmount);
    public CompletableFuture<Wallet> findById(String walletId);
    public CompletableFuture<Wallet> findByUserId(String userId);
}
