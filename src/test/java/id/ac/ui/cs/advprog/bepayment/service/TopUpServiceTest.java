package id.ac.ui.cs.advprog.bepayment.service;

import id.ac.ui.cs.advprog.bepayment.enums.TopUpMethod;
import id.ac.ui.cs.advprog.bepayment.enums.TopUpStatus;
import id.ac.ui.cs.advprog.bepayment.model.TopUp;
import id.ac.ui.cs.advprog.bepayment.model.TopUpBuilder;
import id.ac.ui.cs.advprog.bepayment.model.Wallet;
import id.ac.ui.cs.advprog.bepayment.pojos.TopUpRequest;
import id.ac.ui.cs.advprog.bepayment.repository.TopUpRepositoryImpl;
import id.ac.ui.cs.advprog.bepayment.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TopUpServiceTest {
    @InjectMocks
    TopUpServiceImpl topUpService;
    @Mock
    WalletServiceImpl walletService;

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
                .wallet(wallet)
                .topUpMethod(TopUpMethod.E_WALLET);
        topUp = topUpBuilder.build();
    }

    @Test
    void createTopUpValidTopUpRequestReturnsTopUp() throws ExecutionException, InterruptedException {
        TopUpRequest topUpRequest = new TopUpRequest();
        topUpRequest.userId = topUp.getUserId();
        topUpRequest.walletId = topUp.getWallet().getId();
        topUpRequest.amount = topUp.getAmount();
        topUpRequest.topUpMethod = topUp.getTopUpMethod().getValue();


        when(walletRepository.findById(anyString())).thenReturn(wallet);
        when(topUpRepository.save(any(TopUp.class))).thenReturn(topUp);

        CompletableFuture<TopUp> createdTopUpFuture = topUpService.createTopUp(topUpRequest);

        TopUp createdTopUp = createdTopUpFuture.get();

        assertNotNull(createdTopUp);
        assertEquals(topUp.getId(), createdTopUp.getId());
        assertEquals(topUp.getUserId(), createdTopUp.getUserId());
        assertEquals(topUp.getAmount(), createdTopUp.getAmount());
        assertEquals(topUp.getStatus(), createdTopUp.getStatus());
        assertEquals(topUp.getTopUpMethod(), createdTopUp.getTopUpMethod());
    }

    @Test
    void deleteTopUpByIdExistingTopUpIdReturnsTrue() throws ExecutionException, InterruptedException {
        String topUpId = "3df9d41b-33c3-42a1-b0a4-43cf0ffdc649";
        TopUp topUp = new TopUp();
        when(topUpRepository.findById(topUpId)).thenReturn(topUp);

        when(topUpRepository.deleteTopUpById(topUpId)).thenReturn(true);

        boolean result = topUpService.deleteTopUpById(topUpId).get();

        assertTrue(result);
        verify(topUpRepository, times(1)).deleteTopUpById(topUpId);
    }




    @Test
    void cancelTopUpExistingTopUpIdReturnsTrue() throws ExecutionException, InterruptedException {
        String topUpId = "3df9d41b-33c3-42a1-b0a4-43cf0ffdc649";
        TopUp topUp = new TopUp();
        when(topUpRepository.findById(topUpId)).thenReturn(topUp);
        when(topUpRepository.cancelTopUp(topUpId)).thenReturn(true);

        CompletableFuture<Boolean> resultFuture = topUpService.cancelTopUp(topUpId);

        assertTrue(resultFuture.get());
        verify(topUpRepository, times(1)).cancelTopUp(topUpId);
    }




    @Test
    void cancelTopUpNonExistingTopUpIdReturnsFalse() throws ExecutionException, InterruptedException {
        String nonExistingTopUpId = "3df9d41b-33c3-42a1-b0a4-43cf0ffdc6410";
        TopUp topUp = null;
        when(topUpRepository.findById(nonExistingTopUpId)).thenReturn(topUp);

        CompletableFuture<Boolean> resultFuture = topUpService.cancelTopUp(nonExistingTopUpId);

        assertFalse(resultFuture.get());
        verify(topUpRepository, never()).cancelTopUp(nonExistingTopUpId);
        verify(topUpRepository, times(1)).findById(nonExistingTopUpId);
    }

    @Test
    @Transactional
    public void testConfirmTopUp_WithValidTopUpId() throws ExecutionException, InterruptedException {
        String validTopUpId = "validTopUpId";

        TopUp topUp = new TopUp();
        topUp.setAmount(50.0);
        Wallet wallet = new Wallet();
        wallet.setAmount(100.0);
        topUp.setWallet(wallet);
        when(topUpRepository.findById(validTopUpId)).thenReturn(topUp);

        CompletableFuture<Boolean> result = topUpService.confirmTopUp(validTopUpId);
        verify(topUpRepository, times(1)).confirmTopUp(validTopUpId);
        verify(walletService, times(1)).addAmount(wallet.getId(), 150.0);
    }

    @Test
    void testConfirmTopUpInvalidTopUpIdFailure() throws ExecutionException, InterruptedException {
        String topUpId = "invalid-top-up-id";

        when(topUpRepository.findById(anyString())).thenReturn(null);

        CompletableFuture<Boolean> resultFuture = topUpService.confirmTopUp(topUpId);
        boolean result = resultFuture.get();

        assertFalse(result, "confirmTopUp should return false for an invalid top up ID");
        verify(topUpRepository, times(1)).findById(topUpId);
    }


    @Test
    void findByIdExistingTopUpIdReturnsTopUp() {
        String topUpId = "3df9d41b-33c3-42a1-b0a4-43cf0ffdc649";
        TopUp expectedTopUp = new TopUp();
        when(topUpRepository.findById(topUpId)).thenReturn(expectedTopUp);

        CompletableFuture<TopUp> futureTopUp = topUpService.findById(topUpId);
        TopUp foundTopUp = futureTopUp.join();

        assertNotNull(foundTopUp);
        assertEquals(expectedTopUp, foundTopUp);
    }

    @Test
    void findByIdNonExistingTopUpIdReturnsNull() {
        String nonExistingTopUpId = "3df9d41b-33c3-42a1-b0a4-43cf0ffdc6410";
        when(topUpRepository.findById(anyString())).thenReturn(null);

        CompletableFuture<TopUp> futureTopUp = topUpService.findById(nonExistingTopUpId);
        TopUp foundTopUp = futureTopUp.join();

        assertNull(foundTopUp);
    }

    @Test
    void findAllReturnsListOfTopUps() throws InterruptedException, ExecutionException {
        List<TopUp> expectedTopUps = new ArrayList<>();
        expectedTopUps.add(new TopUp());
        expectedTopUps.add(new TopUp());
        expectedTopUps.add(new TopUp());
        CompletableFuture<List<TopUp>> completedFuture = CompletableFuture.completedFuture(expectedTopUps);
        when(topUpRepository.findAll()).thenReturn(expectedTopUps);

        CompletableFuture<List<TopUp>> foundTopUpsFuture = topUpService.findAll();

        List<TopUp> foundTopUps = foundTopUpsFuture.get();

        assertNotNull(foundTopUps);
        assertEquals(expectedTopUps.size(), foundTopUps.size());
        assertEquals(expectedTopUps, foundTopUps);
    }


    @Test
    void findAllWaitingReturnsListOfTopUps() {
        List<TopUp> expectedTopUps = new ArrayList<>();
        expectedTopUps.add(new TopUp());
        expectedTopUps.add(new TopUp());
        expectedTopUps.add(new TopUp());
        when(topUpRepository.findAllWaiting()).thenReturn(expectedTopUps);

        List<TopUp> foundTopUps = topUpService.findAllWaiting();

        assertNotNull(foundTopUps);
        assertEquals(expectedTopUps.size(), foundTopUps.size());
        assertEquals(expectedTopUps, foundTopUps);
    }

    @Test
    void deleteAllTopUpCallsRepositoryDeleteAll() {
        topUpService.deleteAllTopUp();

        verify(topUpRepository, times(1)).deleteAll();
    }
    @Test
    @Transactional
    public void testFindAllByUserId_WithValidUserIdAndTopUpsExist() {
        String validUserId = "validUserId";
        List<TopUp> topUps = new ArrayList<>();
        topUps.add(new TopUp());
        when(topUpRepository.findAllByUserId(validUserId)).thenReturn(topUps);

        CompletableFuture<List<TopUp>> result = topUpService.findAllByUserId(validUserId);

        assertEquals(topUps, result.join());
    }

    @Test
    @Transactional
    public void testFindAllByUserId_WithValidUserIdAndNoTopUpsExist() {
        String validUserId = "validUserId";
        List<TopUp> emptyList = new ArrayList<>();
        when(topUpRepository.findAllByUserId(validUserId)).thenReturn(emptyList);

        CompletableFuture<List<TopUp>> result = topUpService.findAllByUserId(validUserId);

        assertEquals(emptyList, result.join());
    }

    @Test
    @Transactional
    public void testFindAllByUserId_WithInvalidUserId() {
        String invalidUserId = "invalidUserId";
        when(topUpRepository.findAllByUserId(invalidUserId)).thenReturn(null);

        CompletableFuture<List<TopUp>> result = topUpService.findAllByUserId(invalidUserId);

        assertNull(result.join());
    }

}
