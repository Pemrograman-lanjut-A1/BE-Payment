package id.ac.ui.cs.advprog.bepayment.repository;


import id.ac.ui.cs.advprog.bepayment.model.Wallet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WalletRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private WalletRepositoryImpl walletRepository;

    @Mock
    private Query query;

    private Wallet wallet;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        wallet = Wallet.builder()
                .id("1")
                .userId("3df9d41b-33c3-42a1-b0a4-43cf0ffdc649")
                .amount(500)
                .build();
        when(entityManager.createQuery(anyString())).thenReturn(query);
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
    public void testFindByIdDifferentId() {
        when(entityManager.find(Wallet.class, "999")).thenReturn(null);

        Wallet foundWallet = walletRepository.findById("999");

        verify(entityManager).find(Wallet.class, "999");

        assertNull(foundWallet);
    }

    @Test
    public void testAddAmount() {
        String walletId = "1";
        double totalAmount = 1000.0;

        when(query.setParameter(anyString(), any())).thenReturn(query);

        walletRepository.addAmount(walletId, totalAmount);

        verify(entityManager, times(1))
                .createQuery("UPDATE wallet w SET w.amount = :totalAmount WHERE w.id = :walletId");
        verify(query, times(1)).setParameter("totalAmount", totalAmount);
        verify(query, times(1)).setParameter("walletId", walletId);
        verify(query, times(1)).executeUpdate();

    }

    @Test
    public void testAddAmountUnsuccessfulUpdate() {
        String walletId = "1";
        double totalAmount = 1000.0;

        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(0);

        walletRepository.addAmount(walletId, totalAmount);

        verify(entityManager, times(1))
                .createQuery("UPDATE wallet w SET w.amount = :totalAmount WHERE w.id = :walletId");
        verify(query, times(1)).setParameter("totalAmount", totalAmount);
        verify(query, times(1)).setParameter("walletId", walletId);
        verify(query, times(1)).executeUpdate();

        assertEquals(500, wallet.getAmount());
    }

}
