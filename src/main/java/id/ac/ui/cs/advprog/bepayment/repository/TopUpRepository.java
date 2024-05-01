package id.ac.ui.cs.advprog.bepayment.repository;

import id.ac.ui.cs.advprog.bepayment.model.TopUp;

import java.util.List;

public interface TopUpRepository {
    public TopUp save(TopUp topUp);
    public void deleteAll();
    public boolean deleteTopUpById(String topUpId);
    public boolean cancelTopUp(String topUpId);
    public boolean confirmTopUp(String topUpId);
    public TopUp findById(String id);
    public List<TopUp> findAll();

    public List<TopUp> findAllWaiting();
}
