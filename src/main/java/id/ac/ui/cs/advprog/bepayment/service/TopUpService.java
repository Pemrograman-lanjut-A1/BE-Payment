package id.ac.ui.cs.advprog.bepayment.service;

import id.ac.ui.cs.advprog.bepayment.model.TopUp;
import id.ac.ui.cs.advprog.bepayment.pojos.TopUpRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface TopUpService {
    public CompletableFuture<TopUp> createTopUp(TopUpRequest topUp);
    public CompletableFuture<Object> deleteAllTopUp();
    public CompletableFuture<Boolean> deleteTopUpById(String topUpId);
    public CompletableFuture<Boolean> cancelTopUp(String topUpId);
    public CompletableFuture<Boolean> confirmTopUp(String topUpId);
    public CompletableFuture<TopUp> findById(String topUpId);
    public CompletableFuture<List<TopUp>> findAll();
    public CompletableFuture<List<TopUp>> findAllWaiting();
    public CompletableFuture<List<TopUp>> findAllByUserId(String userId);
}
