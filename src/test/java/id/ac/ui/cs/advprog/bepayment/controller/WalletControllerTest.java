package id.ac.ui.cs.advprog.bepayment.controller;

import id.ac.ui.cs.advprog.bepayment.model.TopUp;
import id.ac.ui.cs.advprog.bepayment.model.Wallet;
import id.ac.ui.cs.advprog.bepayment.pojos.WalletRequest;
import id.ac.ui.cs.advprog.bepayment.service.WalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletControllerTest {
    @InjectMocks
    private WalletController walletController;

    @Mock
    private WalletService walletService;

    @Test
    public void testCreateWalletSuccess() {
        WalletRequest walletRequest = new WalletRequest();
        Wallet createdWallet = new Wallet();

        when(walletService.createWallet(walletRequest)).thenReturn(createdWallet);

        ResponseEntity<?> responseEntity = walletController.createWallet(walletRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertInstanceOf(Map.class, responseEntity.getBody());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody.get("wallet"));
    }

    @Test
    public void testCreateWallet_InternalServerError() {
        when(walletService.createWallet(any())).thenThrow(new RuntimeException("Internal Server Error"));

        ResponseEntity<?> responseEntity = walletController.createWallet(new WalletRequest());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertInstanceOf(Map.class, responseEntity.getBody());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody.get("error"));
    }

    @Test
    void testGetTopUpByIdSuccessful() {
        String walletId = "123";
        Wallet expectedWallet = new Wallet();
        when(walletService.findById(walletId)).thenReturn(expectedWallet);

        ResponseEntity<?> responseEntity = walletController.getTopUpById(walletId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedWallet, responseEntity.getBody());
    }

    @Test
    void testGetTopUpByIdTopUpNotFound() {
        String walletId = "789";
        when(walletService.findById(walletId)).thenReturn(null);

        ResponseEntity<?> responseEntity = walletController.getTopUpById(walletId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().toString().contains("Wallet with ID " + walletId + " not found"));
    }

    @Test
    void testGetTopUpByIdInternalServerError() {
        String walletId = "456";
        when(walletService.findById(walletId)).thenThrow(new RuntimeException("Internal Server Error"));

        ResponseEntity<?> responseEntity = walletController.getTopUpById(walletId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().toString().contains("Something Wrong With Server"));
    }
}
