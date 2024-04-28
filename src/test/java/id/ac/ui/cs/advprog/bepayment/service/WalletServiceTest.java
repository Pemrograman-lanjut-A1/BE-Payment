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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {
    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletServiceImpl walletService;

    private Wallet wallet;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        wallet = Wallet.builder()
                .id("1")
                .userId("3df9d41b-33c3-42a1-b0a4-43cf0ffdc649")
                .amount(500)
                .build();
    }

    @Test
    public void testCreateWallet() {
        WalletRequest walletRequest = new WalletRequest();
        walletRequest.userId = "3df9d41b-33c3-42a1-b0a4-43cf0ffdc649";

        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        Wallet createdWallet = walletService.createWallet(walletRequest);

        verify(walletRepository).save(any(Wallet.class));
        assertNotNull(createdWallet);
        assertEquals(walletRequest.userId, createdWallet.getUserId());
        assertNotNull(createdWallet.getId());
        assertEquals(500, createdWallet.getAmount());
    }

    @Test
    public void testFindById() {
        when(walletRepository.findById("1")).thenReturn(wallet);

        Wallet foundWallet = walletService.findById("1");

        verify(walletRepository).findById("1");
        assertNotNull(foundWallet);
        assertEquals(wallet, foundWallet);
    }

    @Test
    public void testFindByIdWalletNotFound() {
        when(walletRepository.findById("2")).thenReturn(null);

        Wallet foundWallet = walletService.findById("2");

        verify(walletRepository).findById("2");

        assertNull(foundWallet);
    }
}
