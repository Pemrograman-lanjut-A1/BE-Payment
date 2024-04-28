package id.ac.ui.cs.advprog.bepayment.service;

import id.ac.ui.cs.advprog.bepayment.model.TopUp;
import id.ac.ui.cs.advprog.bepayment.model.Wallet;
import id.ac.ui.cs.advprog.bepayment.pojos.TopUpRequest;
import id.ac.ui.cs.advprog.bepayment.pojos.WalletRequest;

public interface WalletService {
    public Wallet createWallet(WalletRequest walletRequest);
    public Wallet findById(String walletId);
}
