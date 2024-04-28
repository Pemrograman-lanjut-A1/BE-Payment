package id.ac.ui.cs.advprog.bepayment.repository;

import id.ac.ui.cs.advprog.bepayment.model.TopUp;
import id.ac.ui.cs.advprog.bepayment.model.Wallet;


public interface WalletRepository {
    public Wallet save(Wallet wallet);
    public Wallet findById(String id);
}
