package id.ac.ui.cs.advprog.bepayment.repository;

import id.ac.ui.cs.advprog.bepayment.model.TopUp;
import id.ac.ui.cs.advprog.bepayment.model.Wallet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class WalletRepositoryImpl implements WalletRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Wallet save(Wallet wallet) {
        entityManager.merge(wallet);
        return wallet;
    }

    @Override
    public Wallet findById(String id){
        return entityManager.find(Wallet.class, id);
    }
}