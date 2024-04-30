package id.ac.ui.cs.advprog.bepayment.service;

import id.ac.ui.cs.advprog.bepayment.enums.TopUpStatus;
import id.ac.ui.cs.advprog.bepayment.model.TopUp;
import id.ac.ui.cs.advprog.bepayment.model.TopUpBuilder;
import id.ac.ui.cs.advprog.bepayment.model.Wallet;
import id.ac.ui.cs.advprog.bepayment.pojos.WalletRequest;
import id.ac.ui.cs.advprog.bepayment.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService{
    @Autowired
    private WalletRepository walletRepository;

    @Override
    @Transactional
    public Wallet createWallet(WalletRequest walletRequest) {
        String walletId = String.valueOf(UUID.randomUUID());
        Wallet wallet = Wallet.builder()
                .id(walletId)
                .userId(walletRequest.userId)
                .amount(0)
                .build();
        return walletRepository.save(wallet);
    }

    @Override
    @Transactional
    public void addAmount(String walletId, double totalAmount) {
        walletRepository.addAmount(walletId, totalAmount);
    }

    @Override
    @Transactional
    public Wallet findById(String walletId) {
        return walletRepository.findById(walletId);
    }
}
