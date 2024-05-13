package id.ac.ui.cs.advprog.bepayment.service;

import id.ac.ui.cs.advprog.bepayment.enums.TopUpMethod;
import id.ac.ui.cs.advprog.bepayment.enums.TopUpStatus;
import id.ac.ui.cs.advprog.bepayment.model.TopUp;
import id.ac.ui.cs.advprog.bepayment.model.TopUpBuilder;
import id.ac.ui.cs.advprog.bepayment.model.Wallet;
import id.ac.ui.cs.advprog.bepayment.pojos.TopUpRequest;
import id.ac.ui.cs.advprog.bepayment.repository.TopUpRepository;
import id.ac.ui.cs.advprog.bepayment.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.logging.Logger;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
public class TopUpServiceImpl implements TopUpService {

    Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    private TopUpRepository topUpRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private WalletService walletService;
    @Autowired
    @Qualifier("asyncExecutor")
    private Executor executor = Executors.newFixedThreadPool(3);
    @Override
    @Transactional
    @Async
    public CompletableFuture<TopUp> createTopUp(TopUpRequest topUpRequest) {
        return CompletableFuture.supplyAsync(() -> {
            Wallet wallet = walletRepository.findById(topUpRequest.getWalletId());
            String topUpId = String.valueOf(UUID.randomUUID());
            TopUp topUp = new TopUpBuilder()
                    .id(topUpId)
                    .userId(topUpRequest.getUserId())
                    .wallet(wallet)
                    .amount(topUpRequest.getAmount())
                    .status(TopUpStatus.WAITING_APPROVAL)
                    .topUpMethod(TopUpMethod.valueOf(topUpRequest.getTopUpMethod()))
                    .dateAdded(new Date())
                    .build();

            return topUpRepository.save(topUp);
        });
    }

    @Override
    @Async
    @Transactional
    public CompletableFuture<Object> deleteAllTopUp() {
        topUpRepository.deleteAll();
        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Transactional
    public CompletableFuture<Boolean> deleteTopUpById(String topUpId) {
        return CompletableFuture.supplyAsync(() -> {
            TopUp topUp = topUpRepository.findById(topUpId);
            if (topUp == null) {
                return false;
            }
            try {
                topUpRepository.deleteTopUpById(topUpId);
                return true;
            } catch (EmptyResultDataAccessException e) {
                return false;
            }
        });
    }


    @Override
    @Async
    @Transactional
    public CompletableFuture<Boolean> cancelTopUp(String topUpId) {
        TopUp topUp = topUpRepository.findById(topUpId);
        if (topUp == null) {
            return CompletableFuture.completedFuture(false);
        }
        try {
            topUpRepository.cancelTopUp(topUpId);
            return CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            return CompletableFuture.completedFuture(false);
        }
    }


    @Override
    @Transactional
    @Async
    public CompletableFuture<Boolean> confirmTopUp(String topUpId) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        try {
            TopUp topUp = topUpRepository.findById(topUpId);
            if (topUp == null) {
                future.complete(false);
                return future;
            }

            double finalAmount = topUp.getAmount();
            topUpRepository.confirmTopUp(topUpId);
            walletService.addAmount(topUp.getWallet().getId(), finalAmount);

            future.complete(true);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            future.completeExceptionally(e);
        } catch (Exception e) {
            logger.info("Error in confirmTopUp: " + e.getMessage());
            future.complete(false);
        }
        return future;
    }




    @Override
    @Transactional
    @Async
    public CompletableFuture<TopUp> findById(String topUpId) {
        return CompletableFuture.supplyAsync(() -> {
            return topUpRepository.findById(topUpId);
        }, executor);
    }



    @Override
    @Transactional
    @Async("asyncExecutor")
    public CompletableFuture<List<TopUp>> findAll() {
        return CompletableFuture.supplyAsync(() -> {
            return topUpRepository.findAll();
        });
    }


    @Override
    @Async("asyncExecutor")
    public List<TopUp> findAllWaiting(){
        return topUpRepository.findAllWaiting();
    }

    @Override
    @Transactional
    @Async("asyncExecutor")
    public CompletableFuture<List<TopUp>> findAllByUserId(String userId) {
        return CompletableFuture.supplyAsync(() -> topUpRepository.findAllByUserId(userId));
    }

}
