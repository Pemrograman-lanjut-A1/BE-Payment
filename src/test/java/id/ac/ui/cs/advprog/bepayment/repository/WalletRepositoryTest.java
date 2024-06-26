package id.ac.ui.cs.advprog.bepayment.repository;


import id.ac.ui.cs.advprog.bepayment.model.Wallet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private WalletRepositoryImpl walletRepository;

    @Mock
    private Query query;

    private Wallet wallet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        wallet = Wallet.builder()
                .id("1")
                .userId("3df9d41b-33c3-42a1-b0a4-43cf0ffdc649")
                .amount(500)
                .build();
        when(entityManager.createQuery(anyString())).thenReturn(query);
    }

    @Test
    void testSave() {
        when(entityManager.merge(wallet)).thenReturn(wallet);

        Wallet savedWallet = walletRepository.save(wallet);

        verify(entityManager).merge(wallet);
        assertEquals(wallet, savedWallet);
    }

    @Test
    void testFindById() {
        when(entityManager.find(Wallet.class, "1")).thenReturn(wallet);

        Wallet foundWallet = walletRepository.findById("1");

        verify(entityManager).find(Wallet.class, "1");
        assertEquals(wallet, foundWallet);
    }

    @Test
    void testFindByIdDifferentId() {
        when(entityManager.find(Wallet.class, "999")).thenReturn(null);

        Wallet foundWallet = walletRepository.findById("999");

        verify(entityManager).find(Wallet.class, "999");

        assertNull(foundWallet);
    }

    @Test
    void testAddAmount() {
        String walletId = "1";
        double totalAmount = 1000.0;

        when(query.setParameter(anyString(), any())).thenReturn(query);

        walletRepository.setAmount(walletId, totalAmount);

        verify(entityManager, times(1))
                .createQuery("UPDATE wallet w SET w.amount = :totalAmount WHERE w.id = :walletId");
        verify(query, times(1)).setParameter("totalAmount", totalAmount);
        verify(query, times(1)).setParameter("walletId", walletId);
        verify(query, times(1)).executeUpdate();

    }

    @Test
    void testAddAmountUnsuccessfulUpdate() {
        String walletId = "1";
        double totalAmount = 1000.0;

        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(0);

        walletRepository.setAmount(walletId, totalAmount);

        verify(entityManager, times(1))
                .createQuery("UPDATE wallet w SET w.amount = :totalAmount WHERE w.id = :walletId");
        verify(query, times(1)).setParameter("totalAmount", totalAmount);
        verify(query, times(1)).setParameter("walletId", walletId);
        verify(query, times(1)).executeUpdate();

        assertEquals(500, wallet.getAmount());
    }

    @Test
    void testFindByUserId() {
        String userId = "3df9d41b-33c3-42a1-b0a4-43cf0ffdc649";
        List<Wallet> wallets = new ArrayList<>();
        wallets.add(wallet);

        TypedQuery<Wallet> walletQuery = mock(TypedQuery.class);

        when(entityManager.createQuery(anyString(), eq(Wallet.class))).thenReturn(walletQuery);
        when(walletQuery.setParameter("userId", userId)).thenReturn(walletQuery);
        when(walletQuery.getResultList()).thenReturn(wallets);

        Wallet foundWallet = walletRepository.findByUserId(userId);

        assertEquals(wallet, foundWallet);
    }


    @Test
    void testFindByUserIdNotFound() {
        String userId = "nonexistentUserId";
        List<Wallet> wallets = new ArrayList<>();

        TypedQuery<Wallet> walletQuery = mock(TypedQuery.class);

        when(entityManager.createQuery(anyString(), eq(Wallet.class))).thenReturn(walletQuery);
        when(walletQuery.setParameter("userId", userId)).thenReturn(walletQuery);
        when(walletQuery.getResultList()).thenReturn(wallets);

        Wallet foundWallet = walletRepository.findByUserId(userId);

        assertNull(foundWallet);
    }


}
