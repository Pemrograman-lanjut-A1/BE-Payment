package id.ac.ui.cs.advprog.bepayment.repository;


import id.ac.ui.cs.advprog.bepayment.model.Wallet;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WalletRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private WalletRepositoryImpl walletRepository;

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
    public void testSave() {
        when(entityManager.merge(wallet)).thenReturn(wallet);

        Wallet savedWallet = walletRepository.save(wallet);

        verify(entityManager).merge(wallet);
        assertEquals(wallet, savedWallet);
    }

    @Test
    public void testFindById() {
        when(entityManager.find(Wallet.class, "1")).thenReturn(wallet);

        Wallet foundWallet = walletRepository.findById("1");

        verify(entityManager).find(Wallet.class, "1");
        assertEquals(wallet, foundWallet);
    }

    @Test
    public void testFindByIdUnhappyPathDifferentId() {
        when(entityManager.find(Wallet.class, "999")).thenReturn(null);

        Wallet foundWallet = walletRepository.findById("999");

        verify(entityManager).find(Wallet.class, "999");

        assertNull(foundWallet);
    }

}
