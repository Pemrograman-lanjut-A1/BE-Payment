package id.ac.ui.cs.advprog.bepayment.service;

import id.ac.ui.cs.advprog.bepayment.enums.TopUpStatus;
import id.ac.ui.cs.advprog.bepayment.model.TopUp;
import id.ac.ui.cs.advprog.bepayment.model.TopUpBuilder;
import id.ac.ui.cs.advprog.bepayment.model.Wallet;
import id.ac.ui.cs.advprog.bepayment.pojos.TopUpRequest;
import id.ac.ui.cs.advprog.bepayment.repository.TopUpRepositoryImpl;
import id.ac.ui.cs.advprog.bepayment.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TopUpServiceTest {
    @InjectMocks
    TopUpServiceImpl topUpService;

    @Mock
    TopUpRepositoryImpl topUpRepository;


    @Mock
    private WalletRepository walletRepository;

    private TopUpBuilder topUpBuilder;
    private TopUp topUp;
    private Wallet wallet;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);

        wallet = Wallet.builder()
                .id("1")
                .userId("3df9d41b-33c3-42a1-b0a4-43cf0ffdc649")
                .amount(500)
                .build();
        topUpBuilder = new TopUpBuilder()
                .userId("3df9d41b-33c3-42a1-b0a4-43cf0ffdc649")
                .amount(500)
                .wallet(wallet);
        topUp = topUpBuilder.build();
    }

    @Test
    void createTopUpValidTopUpRequestReturnsTopUp() {
        String topUpId = "123";
        String userId = "3df9d41b-33c3-42a1-b0a4-43cf0ffdc649";
        String walletId = "1";
        double amount = 100.0;
        TopUpRequest topUpRequest = new TopUpRequest();
        topUpRequest.userId = userId;
        topUpRequest.walletId = walletId;
        topUpRequest.amount = amount;

        when(walletRepository.findById(anyString())).thenReturn(wallet);

        when(topUpRepository.save(any(TopUp.class))).thenReturn(new TopUp(topUpId, userId, wallet, amount, TopUpStatus.WAITING_APPROVAL));

        TopUp createdTopUp = topUpService.createTopUp(topUpRequest);


        assertNotNull(createdTopUp);
        assertEquals(topUpId, createdTopUp.getId());
        assertEquals(userId, createdTopUp.getUserId());
        assertEquals(amount, createdTopUp.getAmount());
        assertEquals(TopUpStatus.WAITING_APPROVAL, createdTopUp.getStatus());
    }

    @Test
    void deleteTopUpByIdExistingTopUpIdReturnsTrue() {
        String topUpId = "3df9d41b-33c3-42a1-b0a4-43cf0ffdc649";
        when(topUpRepository.findById(topUpId)).thenReturn(new TopUp());

        boolean result = topUpService.deleteTopUpById(topUpId);

        assertTrue(result);
        verify(topUpRepository, times(1)).deleteTopUpById(topUpId);
    }

    @Test
    void deleteTopUpByIdNonExistingTopUpIdReturnsFalse() {
        String nonExistingTopUpId = "3df9d41b-33c3-42a1-b0a4-43cf0ffdc6410";
        when(topUpRepository.findById(nonExistingTopUpId)).thenReturn(null);

        boolean result = topUpService.deleteTopUpById(nonExistingTopUpId);

        assertFalse(result);
        verify(topUpRepository, never()).deleteTopUpById(nonExistingTopUpId);
    }

    @Test
    void cancelTopUpExistingTopUpIdReturnsTrue() {
        String topUpId = "3df9d41b-33c3-42a1-b0a4-43cf0ffdc649";
        when(topUpRepository.findById(topUpId)).thenReturn(new TopUp());

        boolean result = topUpService.cancelTopUp(topUpId);

        assertTrue(result);
        verify(topUpRepository, times(1)).cancelTopUp(topUpId);
    }

    @Test
    void cancelTopUpNonExistingTopUpIdReturnsFalse() {
        String nonExistingTopUpId = "3df9d41b-33c3-42a1-b0a4-43cf0ffdc6410";
        when(topUpRepository.findById(nonExistingTopUpId)).thenReturn(null);

        boolean result = topUpService.cancelTopUp(nonExistingTopUpId);

        assertFalse(result);
        verify(topUpRepository, never()).cancelTopUp(nonExistingTopUpId);
    }

    @Test
    void findByIdExistingTopUpIdReturnsTopUp() {
        String topUpId = "3df9d41b-33c3-42a1-b0a4-43cf0ffdc649";
        TopUp expectedTopUp = new TopUp();
        when(topUpRepository.findById(topUpId)).thenReturn(expectedTopUp);

        TopUp foundTopUp = topUpService.findById(topUpId);

        assertNotNull(foundTopUp);
        assertEquals(expectedTopUp, foundTopUp);
    }

    @Test
    void findByIdNonExistingTopUpIdReturnsNull() {
        String nonExistingTopUpId = "3df9d41b-33c3-42a1-b0a4-43cf0ffdc6410";
        when(topUpRepository.findById(nonExistingTopUpId)).thenReturn(null);

        TopUp foundTopUp = topUpService.findById(nonExistingTopUpId);

        assertNull(foundTopUp);
    }

    @Test
    void findAllReturnsListOfTopUps() {
        List<TopUp> expectedTopUps = new ArrayList<>();
        expectedTopUps.add(new TopUp());
        expectedTopUps.add(new TopUp());
        expectedTopUps.add(new TopUp());
        when(topUpRepository.findAll()).thenReturn(expectedTopUps);

        List<TopUp> foundTopUps = topUpService.findAll();

        assertNotNull(foundTopUps);
        assertEquals(expectedTopUps.size(), foundTopUps.size());
        assertEquals(expectedTopUps, foundTopUps);
    }

    @Test
    void deleteAllTopUpCallsRepositoryDeleteAll() {
        topUpService.deleteAllTopUp();

        verify(topUpRepository, times(1)).deleteAll();
    }
}
