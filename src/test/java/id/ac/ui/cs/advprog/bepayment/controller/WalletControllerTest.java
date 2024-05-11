package id.ac.ui.cs.advprog.bepayment.controller;

import id.ac.ui.cs.advprog.bepayment.config.JwtAuthFilter;
import id.ac.ui.cs.advprog.bepayment.model.TopUp;
import id.ac.ui.cs.advprog.bepayment.model.Wallet;
import id.ac.ui.cs.advprog.bepayment.pojos.WalletRequest;
import id.ac.ui.cs.advprog.bepayment.service.WalletService;
import io.jsonwebtoken.ExpiredJwtException;
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
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WalletControllerTest {
    @InjectMocks
    private WalletController walletController;

    @Mock
    private WalletService walletService;

    @Mock
    private JwtAuthFilter jwtAuthFilter;

    @Test
    void createWalletSuccess() {
        WalletRequest walletRequest = new WalletRequest();
        when(jwtAuthFilter.filterToken(anyString())).thenReturn("ADMIN");
        when(walletService.createWallet(any())).thenReturn(CompletableFuture.completedFuture(new Wallet()));

        ResponseEntity<Map<String, Object>> responseEntity = walletController.createWallet("mockedToken", walletRequest).join();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Wallet Created Successfully", responseEntity.getBody().get("message"));
    }

    @Test
    void createWalletExpiredJwtToken() {
        when(jwtAuthFilter.filterToken(anyString())).thenThrow(ExpiredJwtException.class);

        ResponseEntity<Map<String, Object>> responseEntity = walletController.createWallet("mockedToken", new WalletRequest()).join();

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    void createWalletInternalServerError() {
        when(jwtAuthFilter.filterToken(anyString())).thenReturn("ADMIN");
        when(walletService.createWallet(any())).thenReturn(CompletableFuture.failedFuture(new RuntimeException("Some error occurred")));

        ResponseEntity<Map<String, Object>> responseEntity = walletController.createWallet("mockedToken", new WalletRequest()).join();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Something Wrong With Server", responseEntity.getBody().get("message"));
    }
    @Test
    void testGetTopUpByIdSuccessful() {
        String walletId = "123";
        Wallet expectedWallet = new Wallet();

        when(walletService.findById(walletId)).thenReturn(CompletableFuture.completedFuture(expectedWallet));

        CompletableFuture<ResponseEntity<?>> responseEntityFuture = walletController.getWalletById(walletId);

        ResponseEntity<?> responseEntity = responseEntityFuture.join();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedWallet, responseEntity.getBody());
    }

    @Test
    void testGetTopUpByIdTopUpNotFound() {
        String walletId = "789";

        when(walletService.findById(walletId)).thenReturn(CompletableFuture.completedFuture(null));

        CompletableFuture<ResponseEntity<?>> responseEntityFuture = walletController.getWalletById(walletId);

        ResponseEntity<?> responseEntity = responseEntityFuture.join();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().toString().contains("Wallet with ID " + walletId + " not found"));
    }


    @Test
    void testGetTopUpByIdInternalServerError() {
        String walletId = "456";

        when(walletService.findById(walletId)).thenReturn(CompletableFuture.failedFuture(new RuntimeException("Internal Server Error")));

        CompletableFuture<ResponseEntity<?>> responseEntityFuture = walletController.getWalletById(walletId);

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

    @Test
    void addAmountSuccess() throws ExecutionException, InterruptedException {
        String walletId = "mockedWalletId";
        double amount = 100.0;
        Wallet wallet = new Wallet();

        when(jwtAuthFilter.filterToken(anyString())).thenReturn("ADMIN");
        when(walletService.findById(walletId)).thenReturn(CompletableFuture.completedFuture(wallet));
        when(walletService.addAmount(walletId, amount)).thenReturn(CompletableFuture.completedFuture(null));

        ResponseEntity<Map<String, Object>> responseEntity = walletController.addAmount("mockedToken", walletId, amount).join();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Wallet Amount has been Added", responseEntity.getBody().get("message"));
    }

    @Test
    void addAmountWalletNotFound() {
        String walletId = "mockedWalletId";
        Double amount = 100.0;
        when(jwtAuthFilter.filterToken(anyString())).thenReturn("ADMIN");
        when(walletService.findById(walletId)).thenReturn(CompletableFuture.completedFuture(null));

        ResponseEntity<Map<String, Object>> responseEntity = walletController.addAmount("mockedToken", walletId, amount).join();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Wallet with ID mockedWalletId not found.", responseEntity.getBody().get("message"));
    }

    @Test
    void addAmountNegativeAmount() {
        String walletId = "mockedWalletId";
        double amount = -100.0;
        Wallet wallet = new Wallet();
        when(jwtAuthFilter.filterToken(anyString())).thenReturn("ADMIN");
        when(walletService.findById(walletId)).thenReturn(CompletableFuture.completedFuture(wallet));

        ResponseEntity<Map<String, Object>> responseEntity = walletController.addAmount("mockedToken", walletId, amount).join();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("totalAmount cannot be negative", responseEntity.getBody().get("message"));
    }

    @Test
    void addAmountInternalServerError() throws ExecutionException, InterruptedException {
        String walletId = "mockedWalletId";
        Double amount = 100.0;
        when(jwtAuthFilter.filterToken(anyString())).thenReturn("ADMIN");
        when(walletService.findById(walletId)).thenReturn(CompletableFuture.completedFuture(new Wallet()));
        doThrow(new RuntimeException("Some error occurred")).when(walletService).addAmount(walletId, amount);

        ResponseEntity<Map<String, Object>> responseEntity = walletController.addAmount("mockedToken", walletId, amount).join();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Something went wrong on the server side", responseEntity.getBody().get("message"));
    }

    @Test
    void addAmountExpiredJwtToken() {
        when(jwtAuthFilter.filterToken(anyString())).thenThrow(ExpiredJwtException.class);

        ResponseEntity<Map<String, Object>> responseEntity = walletController.addAmount("mockedToken", "mockedWalletId", 100.0).join();

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    void addAmountEmptyJwtToken() {
        when(jwtAuthFilter.filterToken(anyString())).thenReturn(null);

        ResponseEntity<Map<String, Object>> responseEntity = walletController.addAmount("mockedToken", "mockedWalletId", 100.0).join();

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals("Login First", responseEntity.getBody().get("message"));
    }


    @Test
    void decreaseAmountSuccess() throws ExecutionException, InterruptedException {
        String walletId = "mockedWalletId";
        double amount = 100.0;
        Wallet wallet = new Wallet();

        when(jwtAuthFilter.filterToken(anyString())).thenReturn("ADMIN");
        when(walletService.findById(walletId)).thenReturn(CompletableFuture.completedFuture(wallet));
        when(walletService.decreaseAmount(walletId, amount)).thenReturn(CompletableFuture.completedFuture(null));

        ResponseEntity<Map<String, Object>> responseEntity = walletController.decreaseAmount("mockedToken", walletId, amount).join();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Wallet Amount has been Decreased", responseEntity.getBody().get("message"));
    }

    @Test
    void decreaseAmountWalletNotFound() {
        String walletId = "mockedWalletId";
        Double amount = 100.0;
        when(jwtAuthFilter.filterToken(anyString())).thenReturn("ADMIN");
        when(walletService.findById(walletId)).thenReturn(CompletableFuture.completedFuture(null));

        ResponseEntity<Map<String, Object>> responseEntity = walletController.decreaseAmount("mockedToken", walletId, amount).join();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Wallet with ID mockedWalletId not found.", responseEntity.getBody().get("message"));
    }

    @Test
    void decreaseAmountNegativeAmount() {
        String walletId = "mockedWalletId";
        double amount = -100.0;
        Wallet wallet = new Wallet();
        when(jwtAuthFilter.filterToken(anyString())).thenReturn("ADMIN");
        when(walletService.findById(walletId)).thenReturn(CompletableFuture.completedFuture(wallet));

        ResponseEntity<Map<String, Object>> responseEntity = walletController.decreaseAmount("mockedToken", walletId, amount).join();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("totalAmount cannot be negative", responseEntity.getBody().get("message"));
    }

    @Test
    void decreaseAmountInternalServerError() throws ExecutionException, InterruptedException {
        String walletId = "mockedWalletId";
        Double amount = 100.0;
        Wallet wallet = new Wallet();
        when(jwtAuthFilter.filterToken(anyString())).thenReturn("ADMIN");
        when(walletService.findById(walletId)).thenReturn(CompletableFuture.completedFuture(wallet));
        doThrow(new RuntimeException("Some error occurred")).when(walletService).decreaseAmount(walletId, amount);

        ResponseEntity<Map<String, Object>> responseEntity = walletController.decreaseAmount("mockedToken", walletId, amount).join();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Something went wrong on the server side", responseEntity.getBody().get("message"));
    }

    @Test
    void decreaseAmountBadRequest() throws ExecutionException, InterruptedException {
        String walletId = "mockedWalletId";
        Double amount = 100.0;
        Wallet wallet = new Wallet();
        when(jwtAuthFilter.filterToken(anyString())).thenReturn("ADMIN");
        when(walletService.findById(walletId)).thenReturn(CompletableFuture.completedFuture(wallet));
        doThrow(new IllegalArgumentException("Insufficient funds")).when(walletService).decreaseAmount(walletId, amount);

        ResponseEntity<Map<String, Object>> responseEntity = walletController.decreaseAmount("mockedToken", walletId, amount).join();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Insufficient funds", responseEntity.getBody().get("message"));
    }

    @Test
    void decreaseAmountExpiredJwtToken() {
        when(jwtAuthFilter.filterToken(anyString())).thenThrow(ExpiredJwtException.class);

        ResponseEntity<Map<String, Object>> responseEntity = walletController.decreaseAmount("mockedToken", "mockedWalletId", 100.0).join();

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    void decreaseAmountEmptyJwtToken() {
        when(jwtAuthFilter.filterToken(anyString())).thenReturn(null);

        ResponseEntity<Map<String, Object>> responseEntity = walletController.decreaseAmount("mockedToken", "mockedWalletId", 100.0).join();

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals("Login First", responseEntity.getBody().get("message"));
    }

}
