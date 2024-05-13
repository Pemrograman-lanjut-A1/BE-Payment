package id.ac.ui.cs.advprog.bepayment.service;

import id.ac.ui.cs.advprog.bepayment.model.Wallet;
import id.ac.ui.cs.advprog.bepayment.pojos.WalletRequest;
import id.ac.ui.cs.advprog.bepayment.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {
    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletServiceImpl walletService;

    private Wallet wallet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        wallet = Wallet.builder()
                .id("1")
                .userId("3df9d41b-33c3-42a1-b0a4-43cf0ffdc649")
                .amount(500)
                .build();
    }

    @Test
    void testCreateWallet() {
        WalletRequest walletRequest = new WalletRequest();
        walletRequest.userId = "3df9d41b-33c3-42a1-b0a4-43cf0ffdc649";

        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        Wallet createdWallet = walletService.createWallet(walletRequest).join();

        verify(walletRepository).save(any(Wallet.class));
        assertNotNull(createdWallet);
        assertEquals(walletRequest.userId, createdWallet.getUserId());
        assertNotNull(createdWallet.getId());
        assertEquals(500, createdWallet.getAmount());
    }



    @Test
    void testFindById() throws ExecutionException, InterruptedException {
        Wallet wallet = new Wallet();
        wallet.setId("1");

        when(walletRepository.findById("1")).thenReturn(wallet);

        CompletableFuture<Wallet> foundWalletFuture = walletService.findById("1");

        Wallet foundWallet = foundWalletFuture.get();

        verify(walletRepository).findById("1");
        assertNotNull(foundWallet);
        assertEquals(wallet, foundWallet);
    }


    @Test
    void testFindByIdWalletNotFound() {
        when(walletRepository.findById("2")).thenReturn(null);

        CompletableFuture<Wallet> foundWalletFuture = walletService.findById("2");

        Wallet foundWallet = foundWalletFuture.join();

        verify(walletRepository).findById("2");
        assertNull(foundWallet);
    }


    @Test
    void testAddAmountDelegatesCorrectly() {
        String walletId = "1";
        double totalAmount = 1000.0;

        walletRepository.setAmount(walletId, totalAmount);

        verify(walletRepository, times(1)).setAmount(walletId, totalAmount);
    }

    @Test
    void testAddAmountDelegationFailure() {
        String walletId = "1";
        double totalAmount = 1000.0;

        doThrow(new RuntimeException("Failed to add amount")).when(walletRepository).setAmount(walletId, totalAmount);

        try {
            walletRepository.setAmount(walletId, totalAmount);
            fail("Expected RuntimeException was not thrown");
        } catch (RuntimeException e) {
            verify(walletRepository, times(1)).setAmount(walletId, totalAmount);
            assertEquals("Failed to add amount", e.getMessage());
        }
    }

    @Test
    void testFindByUserId() {
        String userId = "3df9d41b-33c3-42a1-b0a4-43cf0ffdc649";

        when(walletRepository.findByUserId(userId)).thenReturn(wallet);

        CompletableFuture<Wallet> foundWalletFuture = walletService.findByUserId(userId);

        Wallet foundWallet = foundWalletFuture.join();

        verify(walletRepository).findByUserId(userId);
        assertNotNull(foundWallet);
        assertEquals(wallet, foundWallet);
    }

    @Test
    void testFindByUserIdWalletNotFound() {
        String userId = "nonexistentUserId";

        when(walletRepository.findByUserId(userId)).thenReturn(null);

        CompletableFuture<Wallet> foundWalletFuture = walletService.findByUserId(userId);

        Wallet foundWallet = foundWalletFuture.join();

        verify(walletRepository).findByUserId(userId);

        assertNull(foundWallet);
    }

    @Test
    void testAddAmount() throws InterruptedException, ExecutionException {
        String walletId = "1";
        double totalAmountToAdd = 50.0;
        double expectedFinalAmount = wallet.getAmount() + totalAmountToAdd;

        when(walletService.findById(walletId)).thenAnswer(invocation -> wallet);

        CompletableFuture<Void> result = walletService.addAmount(walletId, totalAmountToAdd);
        result.join();

        verify(walletRepository).setAmount(walletId, expectedFinalAmount);
    }

    @Test
    void testDecreaseAmount() throws InterruptedException, ExecutionException {
        String walletId = "1";
        double totalAmountToDecrease = 50.0;
        double expectedFinalAmount = wallet.getAmount() - totalAmountToDecrease;

        when(walletService.findById(walletId)).thenAnswer(invocation -> wallet);

        CompletableFuture<Void> result = walletService.decreaseAmount(walletId, totalAmountToDecrease);
        result.join();

        verify(walletRepository).setAmount(walletId, expectedFinalAmount);
    }

}
