package id.ac.ui.cs.advprog.bepayment.service;
import id.ac.ui.cs.advprog.bepayment.model.Wallet;
import id.ac.ui.cs.advprog.bepayment.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceImpl {

    @Autowired
    WalletRepository walletRepository;

    public void createWallet(String id){
        Wallet newWallet = new Wallet(id);
        walletRepository.createWallet(newWallet);
    }

    public void addBalance(String id, int amount){
        walletRepository.addBalance(id, amount);
    }

    public void subtractBalance(String id, int amount){
        walletRepository.subtractBalance(id, amount);
    }
}
