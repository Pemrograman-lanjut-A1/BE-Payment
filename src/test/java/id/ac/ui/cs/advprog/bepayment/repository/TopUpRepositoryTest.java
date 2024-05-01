package id.ac.ui.cs.advprog.bepayment.repository;

import id.ac.ui.cs.advprog.bepayment.enums.TopUpStatus;
import id.ac.ui.cs.advprog.bepayment.model.TopUp;
import id.ac.ui.cs.advprog.bepayment.model.TopUpBuilder;
import id.ac.ui.cs.advprog.bepayment.model.Wallet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TopUpRepositoryTest {
    @Mock
    TopUpRepository repository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private TopUpRepositoryImpl topUpRepository;
    private TopUpBuilder topUpBuilder;
    private TopUp topUp;
    private Wallet wallet;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
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
    public void testSave() {
        when(entityManager.merge(any())).thenReturn(topUp);

        TopUp savedTopUp = topUpRepository.save(topUp);

        assertEquals(topUp, savedTopUp);
        assertEquals(topUp.getAmount(), savedTopUp.getAmount());
        assertEquals(topUp.getWallet(), savedTopUp.getWallet());
        assertEquals(topUp.getId(), savedTopUp.getId());
        assertEquals(topUp.getUserId(), savedTopUp.getUserId());
        assertEquals(topUp.getStatus(), TopUpStatus.WAITING_APPROVAL);
        verify(entityManager, times(1)).merge(any());
    }

    @Test
    public void testDeleteAll() {
        Query query = mock(Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(query);

        topUpRepository.deleteAll();

        verify(entityManager, times(1)).createQuery(anyString());
        verify(query, times(1)).executeUpdate();
    }

    @Test
    public void testDeleteAllNoQuery() {
        when(entityManager.createQuery(anyString())).thenReturn(null);

        assertThrows(NullPointerException.class, () -> topUpRepository.deleteAll());
    }

    @Test
    public void testDeleteTopUpByIdQuery() {
        Query query = mock(Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(query);
        doReturn(query).when(query).setParameter(anyString(), any());
        when(query.executeUpdate()).thenReturn(1);

        topUpRepository.deleteTopUpById("123");

        verify(entityManager, times(1)).createQuery(anyString());
        verify(query, times(1)).setParameter(anyString(), any());
        verify(query, times(1)).executeUpdate();
    }

    @Test
    public void testDeleteTopUpById() {
        String topUpId = topUp.getId();

        repository.deleteTopUpById(topUpId);

        assertNull(repository.findById(topUpId));
    }

    @Test
    public void testDeleteTopUpByIdNoQuery() {
        when(entityManager.createQuery(anyString())).thenReturn(null);

        assertThrows(NullPointerException.class, () -> topUpRepository.deleteTopUpById("123"));
    }

    @Test
    public void testDeleteTopUpByIdIfIdNotFound(){
        String topUpIdFalse = topUp.getId() + "a";
        assertThrows(NullPointerException.class, () -> topUpRepository.deleteTopUpById(topUpIdFalse));
    }

    @Test
    public void testCancelTopUp() {
        Query query = mock(Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("status"), any())).thenReturn(query);
        when(query.setParameter(eq("topUpId"), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        topUpRepository.cancelTopUp("123");

        verify(entityManager, times(1)).createQuery(anyString());
        verify(query, times(1)).setParameter(eq("status"), any());
        verify(query, times(1)).setParameter(eq("topUpId"), any());
        verify(query, times(1)).executeUpdate();
    }


    @Test
    public void testCancelTopUpNoQuery() {
        when(entityManager.createQuery(anyString())).thenReturn(null);

        assertThrows(NullPointerException.class, () -> topUpRepository.cancelTopUp("123"));
    }

    @Test
    public void testConfirmTopUpSuccessfulUpdate() {
        String topUpId = "valid-top-up-id";
        Query query = mock(Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("status"), any())).thenReturn(query);
        when(query.setParameter(eq("topUpId"), eq(topUpId))).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        boolean result = topUpRepository.confirmTopUp(topUpId);

        assertTrue(result, "confirmTopUp should return true for a successful update");
        verify(entityManager, times(1)).createQuery(anyString());
        verify(query, times(1)).setParameter(eq("status"), any());
        verify(query, times(1)).setParameter(eq("topUpId"), eq(topUpId));
        verify(query, times(1)).executeUpdate();
    }

    @Test
    public void testConfirmTopUpNoResultException() {
        String topUpId = "invalid-top-up-id";
        when(entityManager.createQuery(anyString())).thenThrow(new NoResultException());

        boolean result = topUpRepository.confirmTopUp(topUpId);

        assertFalse(result, "confirmTopUp should return false for an invalid top up ID");
        verify(entityManager, times(1)).createQuery(anyString());
    }



    @Test
    public void testFindById() {
        TopUp expectedTopUp = new TopUp();
        when(entityManager.find(eq(TopUp.class), any())).thenReturn(expectedTopUp);

        TopUp foundTopUp = topUpRepository.findById("123");

        assertEquals(expectedTopUp, foundTopUp);
        verify(entityManager, times(1)).find(eq(TopUp.class), any());
    }

    @Test
    public void testFindByIdNotFound() {
        when(entityManager.find(eq(TopUp.class), any())).thenReturn(null);

        assertNull(topUpRepository.findById("123"));
    }

    @Test
    public void testFindAll() {
        List<TopUp> expectedTopUps = Collections.emptyList();
        TypedQuery typedQuery = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(TopUp.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedTopUps);

        List<TopUp> foundTopUps = topUpRepository.findAll();

        assertEquals(expectedTopUps, foundTopUps);
        verify(entityManager, times(1)).createQuery(anyString(), eq(TopUp.class));
        verify(typedQuery, times(1)).getResultList();
    }

    @Test
    public void testFindAllWaiting() {

        TypedQuery typedQuery = mock(TypedQuery.class);
        when(typedQuery.setParameter(eq("WAITING_APPROVAL"), any())).thenReturn(typedQuery);
        when(typedQuery.executeUpdate()).thenReturn(1);
        List<TopUp> expectedTopUps = Collections.emptyList();
        when(entityManager.createQuery(anyString(), eq(TopUp.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedTopUps);

        List<TopUp> foundTopUps = topUpRepository.findAllWaiting();

        assertEquals(expectedTopUps, foundTopUps);
        verify(entityManager, times(1)).createQuery(anyString(), eq(TopUp.class));
        verify(typedQuery, times(1)).getResultList();
    }

    @Test
    public void testFindAllEmptyResult() {
        List<TopUp> expectedTopUps = Collections.emptyList();
        TypedQuery<TopUp> typedQuery = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(TopUp.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedTopUps);

        List<TopUp> foundTopUps = topUpRepository.findAll();

        assertEquals(expectedTopUps, foundTopUps);
        verify(entityManager, times(1)).createQuery(anyString(), eq(TopUp.class));
        verify(typedQuery, times(1)).getResultList();
    }
}

