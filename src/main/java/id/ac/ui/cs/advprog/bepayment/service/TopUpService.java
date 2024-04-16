package id.ac.ui.cs.advprog.bepayment.service;

import id.ac.ui.cs.advprog.bepayment.model.TopUp;

import java.util.List;

public interface TopUpService {
    public TopUp createTopUp();
    public void deleteAllTopUp();
    public void deleteTopUpById(String topUpId);
    public void cancelTopUp(String topUpId);
    TopUp findById(String topUpId);
    public List<TopUp> findAll();
}
