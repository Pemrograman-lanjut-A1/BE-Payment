package id.ac.ui.cs.advprog.bepayment.repository;

import id.ac.ui.cs.advprog.bepayment.model.TopUp;

import java.util.List;

public interface TopUpRepository {
    public TopUp save(TopUp topUp);
    public void deleteAll();
    public void deleteTopUpById(String topUpId);
    public void cancelTopUp(String topUpId);
    public TopUp findById(String id);
    public List<TopUp> findAll();
}
