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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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

        when(walletService.createWallet(walletRequest)).thenReturn(CompletableFuture.completedFuture(createdWallet));

        CompletableFuture<ResponseEntity<Map<String, Object>>> responseEntityFuture = walletController.createWallet(walletRequest);

        ResponseEntity<?> responseEntity = responseEntityFuture.join();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody.get("wallet"));
    }

    @Test
    public void testCreateWallet_InternalServerError() {
        when(walletService.createWallet(any())).thenReturn(CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("Internal Server Error");
        }));

        CompletableFuture<ResponseEntity<Map<String, Object>>> responseEntityFuture = walletController.createWallet(new WalletRequest());
        ResponseEntity<?> responseEntity = responseEntityFuture.join();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody.get("error"));
    }


    @Test
    void testGetTopUpByIdSuccessful() {
        String walletId = "123";
        Wallet expectedWallet = new Wallet();

        when(walletService.findById(walletId)).thenReturn(CompletableFuture.completedFuture(expectedWallet));

        CompletableFuture<ResponseEntity<?>> responseEntityFuture = walletController.getTopUpById(walletId);

        ResponseEntity<?> responseEntity = responseEntityFuture.join();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedWallet, responseEntity.getBody());
    }

    @Test
    void testGetTopUpByIdTopUpNotFound() {
        String walletId = "789";

        when(walletService.findById(walletId)).thenReturn(CompletableFuture.completedFuture(null));

        CompletableFuture<ResponseEntity<?>> responseEntityFuture = walletController.getTopUpById(walletId);

        ResponseEntity<?> responseEntity = responseEntityFuture.join();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().toString().contains("Wallet with ID " + walletId + " not found"));
    }


    @Test
    void testGetTopUpByIdInternalServerError() {
        String walletId = "456";

        when(walletService.findById(walletId)).thenReturn(CompletableFuture.failedFuture(new RuntimeException("Internal Server Error")));

        CompletableFuture<ResponseEntity<?>> responseEntityFuture = walletController.getTopUpById(walletId);

        ResponseEntity<?> responseEntity = responseEntityFuture.join();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().toString().contains("Something Wrong With Server"));
    }

    @Test
    public void testGetWalletByUserIdSuccess() {
        String userId = "123";
        Wallet expectedWallet = new Wallet();

        when(walletService.findByUserId(userId)).thenReturn(CompletableFuture.completedFuture(expectedWallet));

        CompletableFuture<ResponseEntity<?>> responseEntityFuture = walletController.getWalletByUserId(userId);

        ResponseEntity<?> responseEntity = responseEntityFuture.join();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedWallet, responseEntity.getBody());
    }

    @Test
    public void testGetWalletByUserIdInternalServerError() {
        String userId = "789";

        when(walletService.findByUserId(userId)).thenReturn(CompletableFuture.failedFuture(new RuntimeException("Internal Server Error")));

        CompletableFuture<ResponseEntity<?>> responseEntityFuture = walletController.getWalletByUserId(userId);

        ResponseEntity<?> responseEntity = responseEntityFuture.join();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        Map<String, Object> expectedResponseBody = new HashMap<>();
        expectedResponseBody.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        expectedResponseBody.put("error", "Internal Server Error");
        expectedResponseBody.put("message", "Something Wrong With Server");

        Map<String, Object> actualResponseBody = (Map<String, Object>) responseEntity.getBody();

        assertEquals(expectedResponseBody, actualResponseBody);
    }


}
