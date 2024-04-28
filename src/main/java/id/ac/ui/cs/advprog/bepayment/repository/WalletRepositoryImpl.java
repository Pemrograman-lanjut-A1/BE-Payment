package id.ac.ui.cs.advprog.bepayment.repository;

import id.ac.ui.cs.advprog.bepayment.enums.TopUpStatus;
import id.ac.ui.cs.advprog.bepayment.model.TopUp;
import id.ac.ui.cs.advprog.bepayment.model.Wallet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class WalletRepositoryImpl implements WalletRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Wallet save(Wallet wallet) {
        entityManager.merge(wallet);
        return wallet;
    }

    @Override
    @Transactional
    public Wallet findById(String id){
        return entityManager.find(Wallet.class, id);
    }

    @Override
    @Transactional
    public void addAmount(String id, double totalAmount) {

    }
}
