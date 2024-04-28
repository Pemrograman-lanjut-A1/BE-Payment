package id.ac.ui.cs.advprog.bepayment.service;

import id.ac.ui.cs.advprog.bepayment.model.TopUp;
import id.ac.ui.cs.advprog.bepayment.model.Wallet;
import id.ac.ui.cs.advprog.bepayment.pojos.TopUpRequest;

import java.util.List;

public interface TopUpService {
    public TopUp createTopUp(TopUpRequest topUp);
    public void deleteAllTopUp();
    public boolean deleteTopUpById(String topUpId);
    public boolean cancelTopUp(String topUpId);
    public boolean confirmTopUp(String topUpId);
    public TopUp findById(String topUpId);
    public List<TopUp> findAll();
}
