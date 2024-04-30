package id.ac.ui.cs.advprog.bepayment.repository;


import id.ac.ui.cs.advprog.bepayment.model.Wallet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class WalletRepository {
    List<Wallet> wallets = new ArrayList<>();

    public void createWallet(Wallet newWallet){
        wallets.add(newWallet);
    }

    public void addAmount(String id, int amount){
        for(Wallet wallet : wallets){
            if (wallet.getId().equals(id)){
                wallet.addAmount(amount);
            }
        }
    }

    public void subtractAmount(String id, int amount){
        for(Wallet wallet : wallets){
            if (wallet.getId().equals(id)){
                wallet.subtractAmount(amount);
            }
        }
    }
}

