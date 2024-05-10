package id.ac.ui.cs.advprog.bepayment.repository;

import id.ac.ui.cs.advprog.bepayment.enums.TopUpStatus;
import id.ac.ui.cs.advprog.bepayment.model.TopUp;
import id.ac.ui.cs.advprog.bepayment.model.Wallet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public Wallet findByUserId(String userId) {
        TypedQuery<Wallet> query = entityManager.createQuery(
                "SELECT w FROM wallet w WHERE w.userId = :userId", Wallet.class);
        query.setParameter("userId", userId);
        List<Wallet> wallets = query.getResultList();
        return wallets.isEmpty() ? null : wallets.getFirst();
    }

    @Override
    @Transactional
    public void addAmount(String id, double totalAmount) {
        entityManager.createQuery("UPDATE wallet w SET w.amount = :totalAmount WHERE w.id = :walletId")
                .setParameter("totalAmount", totalAmount)
                .setParameter("walletId", id)
                .executeUpdate();
    }
}
