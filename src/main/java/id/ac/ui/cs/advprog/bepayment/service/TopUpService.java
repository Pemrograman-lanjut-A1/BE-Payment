package id.ac.ui.cs.advprog.bepayment.service;

import id.ac.ui.cs.advprog.bepayment.model.TopUp;
import id.ac.ui.cs.advprog.bepayment.pojos.TopUpRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface TopUpService {
    public CompletableFuture<TopUp> createTopUp(TopUpRequest topUp);
    public void deleteAllTopUp();
    public boolean deleteTopUpById(String topUpId);
    public CompletableFuture<Boolean> cancelTopUp(String topUpId);
    public CompletableFuture<Boolean> confirmTopUp(String topUpId);
    public TopUp findById(String topUpId);
    public List<TopUp> findAll();
    public List<TopUp> findAllWaiting();
    public List<TopUp> findAllByUserId(String userId);
}
