package id.ac.ui.cs.advprog.bepayment.service;

import id.ac.ui.cs.advprog.bepayment.enums.TopUpStatus;
import id.ac.ui.cs.advprog.bepayment.model.TopUp;
import id.ac.ui.cs.advprog.bepayment.model.TopUpBuilder;
import id.ac.ui.cs.advprog.bepayment.model.Wallet;
import id.ac.ui.cs.advprog.bepayment.pojos.TopUpRequest;
import id.ac.ui.cs.advprog.bepayment.repository.TopUpRepository;
import id.ac.ui.cs.advprog.bepayment.repository.TopUpRepositoryImpl;
import id.ac.ui.cs.advprog.bepayment.repository.WalletRepository;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TopUpServiceImpl implements TopUpService {

    @Autowired
    private TopUpRepository topUpRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Override
    @Transactional
    public TopUp createTopUp(TopUpRequest topUpRequest) {
        Wallet wallet = walletRepository.findById(topUpRequest.walletId);
        String topUpId = String.valueOf(UUID.randomUUID());
        TopUp topUp = new TopUpBuilder()
                .id(topUpId)
                .userId(topUpRequest.userId)
                .wallet(wallet)
                .amount(topUpRequest.amount)
                .status(TopUpStatus.WAITING_APPROVAL)
                .build();

        return topUpRepository.save(topUp);
    }

    @Override
    @Transactional
    public void deleteAllTopUp() {
        topUpRepository.deleteAll();
    }

    @Override
    @Transactional
    public boolean deleteTopUpById(String topUpId) {
        if (topUpRepository.findById(topUpId) == null){
            return false;
        }
        try {
            topUpRepository.deleteTopUpById(topUpId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean cancelTopUp(String topUpId) {
        if (topUpRepository.findById(topUpId) == null){
            return false;
        }
        try {
            topUpRepository.cancelTopUp(topUpId);
            return true;
        } catch (Exception e) {
            System.err.println("Error in confirmTopUp: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean confirmTopUp(String topUpId) {
        TopUp topUp = topUpRepository.findById(topUpId);
        if (topUp == null){
            return false;
        }
        try {
            double totalAmount = topUp.getAmount() + topUp.getWallet().getAmount();
            topUpRepository.confirmTopUp(topUpId);
            walletRepository.addAmount(topUp.getWallet().getId(), totalAmount);
            return true;
        } catch (Exception e) {
            System.err.println("Error in confirmTopUp: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public TopUp findById(String topUpId) {
        return topUpRepository.findById(topUpId);
    }

    @Override
    @Transactional
    public List<TopUp> findAll() {
        return topUpRepository.findAll();
    }

    @Override
    public List<TopUp> findAllWaiting(){
        return topUpRepository.findAllWaiting();
    }
}
