package id.ac.ui.cs.advprog.bepayment.repository;

import id.ac.ui.cs.advprog.bepayment.enums.TopUpStatus;
import id.ac.ui.cs.advprog.bepayment.model.TopUp;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class TopUpRepositoryImpl implements TopUpRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public TopUp save(TopUp topUp) {
        entityManager.merge(topUp);
        return topUp;
    }
    @Override
    @Transactional
    public void deleteAll(){
        entityManager.createQuery("DELETE FROM topup").executeUpdate();
    }

    @Override
    @Transactional
    public boolean deleteTopUpById(String topUpId) {
        try {
            entityManager.createQuery("DELETE FROM topup t WHERE t.id = :topUpId")
                    .setParameter("topUpId", topUpId)
                    .executeUpdate();
            return true;
        }catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean cancelTopUp(String topUpId){
        try {
            entityManager.createQuery("UPDATE topup t SET t.status = :status WHERE t.id = :topUpId")
                    .setParameter("status", TopUpStatus.CANCELLED)
                    .setParameter("topUpId", topUpId)
                    .executeUpdate();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean confirmTopUp(String topUpId) {
        return false;
    }

    @Override
    @Transactional
    public TopUp findById(String id){
        return entityManager.find(TopUp.class, id);
    }

    @Override
    @Transactional
    public List<TopUp> findAll() {
        return entityManager.createQuery("SELECT t FROM topup t", TopUp.class)
                .getResultList();
    }
}
