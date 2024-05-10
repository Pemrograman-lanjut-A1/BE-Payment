package id.ac.ui.cs.advprog.bepayment.controller;

import id.ac.ui.cs.advprog.bepayment.model.TopUp;
import id.ac.ui.cs.advprog.bepayment.pojos.TopUpRequest;
import id.ac.ui.cs.advprog.bepayment.service.TopUpService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TopUpControllerTest {

    @InjectMocks
    private TopUpController topUpController;

    @Mock
    private TopUpService topUpService;


    @Test
    public void testCreateTopUpSuccess() throws ExecutionException, InterruptedException, ExecutionException {
        TopUpRequest topUpRequest = new TopUpRequest();

        CompletableFuture<TopUp> completedFuture = CompletableFuture.completedFuture(new TopUp());

        when(topUpService.createTopUp(topUpRequest)).thenReturn(completedFuture);

        CompletableFuture<ResponseEntity<Map<String, Object>>> responseFuture = topUpController.createTopUp(topUpRequest);

        ResponseEntity<?> responseEntity = responseFuture.get();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertInstanceOf(Map.class, responseEntity.getBody());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody.get("topUp"));
    }


    @Test
    public void testCreateTopUpInternalServerError() throws ExecutionException, InterruptedException {
        CompletableFuture<TopUp> completedFuture = new CompletableFuture<>();
        completedFuture.completeExceptionally(new RuntimeException("Internal Server Error"));

        when(topUpService.createTopUp(any())).thenReturn(completedFuture);

        CompletableFuture<ResponseEntity<Map<String, Object>>> responseFuture = topUpController.createTopUp(new TopUpRequest());

        ResponseEntity<?> responseEntity = responseFuture.get();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertInstanceOf(Map.class, responseEntity.getBody());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody.get("error"));
    }


    @Test
    public void testDeleteAllTopUpSuccess() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> completedFuture = CompletableFuture.completedFuture(null);
        doReturn(completedFuture).when(topUpService).deleteAllTopUp();

        CompletableFuture<ResponseEntity<Map<String, Object>>> responseFuture = topUpController.deleteAllTopUp();
        ResponseEntity<?> responseEntity = responseFuture.get();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertNotNull(responseBody.get("message"));
    }


    @Test
    public void testDeleteTopUpByIdSuccess() throws ExecutionException, InterruptedException {
        String topUpId = "dummyId";
        CompletableFuture<Boolean> completedFuture = CompletableFuture.completedFuture(true);
        when(topUpService.deleteTopUpById(topUpId)).thenReturn(completedFuture);

        CompletableFuture<ResponseEntity<Map<String, Object>>> responseFuture = topUpController.deleteTopUpById(topUpId);
        ResponseEntity<?> responseEntity = responseFuture.get();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertNotNull(responseBody.get("message"));
    }

    @Test
    public void testCancelTopUpSuccess() throws ExecutionException, InterruptedException {
        String topUpId = "dummyId";
        CompletableFuture<Boolean> completedFuture = CompletableFuture.completedFuture(true);
        when(topUpService.cancelTopUp(topUpId)).thenReturn(completedFuture);

        CompletableFuture<ResponseEntity<Map<String, Object>>> responseFuture = topUpController.cancelTopUp(topUpId);
        ResponseEntity<?> responseEntity = responseFuture.get();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertNotNull(responseBody.get("message"));
    }

    @Test
    public void testGetAllTopUpsSuccess() {
        List<TopUp> dummyTopUps = Arrays.asList(new TopUp());
        CompletableFuture<List<TopUp>> completedFuture = CompletableFuture.completedFuture(dummyTopUps);

        when(topUpService.findAll()).thenReturn(completedFuture);

        CompletableFuture<ResponseEntity<List<TopUp>>> responseFuture = topUpController.getAllTopUps();

        responseFuture.thenAccept(responseEntity -> {
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(dummyTopUps, responseEntity.getBody());
        }).join();
    }


    @Test
    public void testGetAllWaitingTopUpsSuccess() {
        List<TopUp> dummyTopUps = Arrays.asList(new TopUp());

        when(topUpService.findAllWaiting()).thenReturn(dummyTopUps);

        ResponseEntity<?> responseEntity = topUpController.getAllWaitingTopUps();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(dummyTopUps, responseEntity.getBody());
    }


    @Test
    public void testDeleteAllTopUpInternalServerError() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> completedFuture = CompletableFuture.failedFuture(new RuntimeException("Internal Server Error"));
        doReturn(completedFuture).when(topUpService).deleteAllTopUp();

        CompletableFuture<ResponseEntity<Map<String, Object>>> responseFuture = topUpController.deleteAllTopUp();
        ResponseEntity<?> responseEntity = responseFuture.get();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertInstanceOf(Map.class, responseEntity.getBody());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody.get("error"));
    }

    @Test
    public void testDeleteTopUpByIdNotFound() throws ExecutionException, InterruptedException {
        String topUpId = "nonExistentId";
        CompletableFuture<Boolean> completedFuture = CompletableFuture.completedFuture(false);
        when(topUpService.deleteTopUpById(topUpId)).thenReturn(completedFuture);

        CompletableFuture<ResponseEntity<Map<String, Object>>> responseFuture = topUpController.deleteTopUpById(topUpId);
        ResponseEntity<?> responseEntity = responseFuture.get();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertInstanceOf(Map.class, responseEntity.getBody());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody.get("message"));
    }

    @Test
    public void testCancelTopUpNotFound() throws ExecutionException, InterruptedException {
        String topUpId = "nonExistentId";
        CompletableFuture<Boolean> completedFuture = CompletableFuture.completedFuture(false);
        when(topUpService.cancelTopUp(topUpId)).thenReturn(completedFuture);

        CompletableFuture<ResponseEntity<Map<String, Object>>> responseFuture = topUpController.cancelTopUp(topUpId);
        ResponseEntity<?> responseEntity = responseFuture.get();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertInstanceOf(Map.class, responseEntity.getBody());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertNotNull(responseBody.get("message"));
    }

    @Test
    public void testGetAllTopUpsInternalServerError() {
        CompletableFuture<List<TopUp>> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException("Internal Server Error"));
        when(topUpService.findAll()).thenReturn(future);

        CompletableFuture<ResponseEntity<List<TopUp>>> responseFuture = topUpController.getAllTopUps();

        responseFuture.thenAccept(responseEntity -> {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
            assertEquals(Collections.emptyList(), responseEntity.getBody());
        }).join();
    }



    @Test
    public void testGetAllWaitingTopUpsInternalServerError() {
        when(topUpService.findAllWaiting()).thenThrow(new RuntimeException("Internal Server Error"));

        ResponseEntity<?> responseEntity = topUpController.getAllWaitingTopUps();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertInstanceOf(Map.class, responseEntity.getBody());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody.get("error"));
    }


    @Test
    void testConfirmTopUpSuccessful() throws ExecutionException, InterruptedException {
        String topUpId = "123";
        CompletableFuture<Boolean> completedFuture = CompletableFuture.completedFuture(true);
        when(topUpService.confirmTopUp(topUpId)).thenReturn(completedFuture);

        CompletableFuture<ResponseEntity<Map<String, Object>>> responseFuture = topUpController.confirmTopUp(topUpId);
        ResponseEntity<?> responseEntity = responseFuture.get();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().toString().contains("confirmed successfully"));
    }

    @Test
    void testConfirmTopUpNotFound() throws ExecutionException, InterruptedException {
        String topUpId = "456";
        CompletableFuture<Boolean> completedFuture = CompletableFuture.completedFuture(false);
        when(topUpService.confirmTopUp(topUpId)).thenReturn(completedFuture);

        CompletableFuture<ResponseEntity<Map<String, Object>>> responseFuture = topUpController.confirmTopUp(topUpId);
        ResponseEntity<?> responseEntity = responseFuture.get();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().toString().contains("not found"));
    }

    @Test
    void testConfirmTopUpInternalServerError() throws ExecutionException, InterruptedException {
        String topUpId = "789";
        CompletableFuture<Boolean> completedFuture = new CompletableFuture<>();
        completedFuture.completeExceptionally(new RuntimeException("Internal Server Error"));
        when(topUpService.confirmTopUp(topUpId)).thenReturn(completedFuture);

        CompletableFuture<ResponseEntity<Map<String, Object>>> responseFuture = topUpController.confirmTopUp(topUpId);
        ResponseEntity<?> responseEntity = responseFuture.get();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().toString().contains("Something Wrong With Server"));
    }

    @Test
    void testGetTopUpByIdSuccessful() {
        String topUpId = "123";
        TopUp expectedTopUp = new TopUp();
        CompletableFuture<TopUp> future = CompletableFuture.completedFuture(expectedTopUp);
        when(topUpService.findById(topUpId)).thenReturn(future);

        CompletableFuture<ResponseEntity<?>> responseFuture = topUpController.getTopUpById(topUpId);

        responseFuture.thenAccept(responseEntity -> {
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(expectedTopUp, responseEntity.getBody());
        }).join();
    }


    @Test
    void testGetTopUpByIdTopUpNotFound() {
        String topUpId = "789";
        CompletableFuture<TopUp> future = CompletableFuture.completedFuture(null);
        when(topUpService.findById(topUpId)).thenReturn(future);

        CompletableFuture<ResponseEntity<?>> responseFuture = topUpController.getTopUpById(topUpId);

        responseFuture.thenAccept(responseEntity -> {
            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
            assertTrue(responseEntity.getBody().toString().contains("Top-up with ID " + topUpId + " not found"));
        }).join();
    }

    @Test
    void testGetTopUpByIdInternalServerError() {
        String topUpId = "456";
        CompletableFuture<TopUp> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException("Internal Server Error"));
        when(topUpService.findById(topUpId)).thenReturn(future);

        CompletableFuture<ResponseEntity<?>> responseFuture = topUpController.getTopUpById(topUpId);

        responseFuture.thenAccept(responseEntity -> {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

            Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
            assertNotNull(responseBody);
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseBody.get("code"));
            assertEquals("Internal Server Error", responseBody.get("error"));
            assertEquals("Something went wrong with the server", responseBody.get("message"));
        }).join();
    }


    @Test
    void testGetTopUpByUserIdSuccess() throws ExecutionException, InterruptedException {
        String userId = "valid-user-id";
        List<TopUp> dummyTopUps = Arrays.asList(new TopUp(), new TopUp());
        CompletableFuture<List<TopUp>> completedFuture = CompletableFuture.completedFuture(dummyTopUps);
        when(topUpService.findAllByUserId(userId)).thenReturn(completedFuture);

        CompletableFuture<ResponseEntity<Map<String, Object>>> responseFuture = topUpController.getTopUpByUserId(userId);

        ResponseEntity<Map<String, Object>> responseEntity = responseFuture.get();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        List<TopUp> actualTopUps = (List<TopUp>) responseEntity.getBody().get("topUps");

        assertEquals(dummyTopUps.size(), actualTopUps.size());
        assertTrue(dummyTopUps.containsAll(actualTopUps));
        assertTrue(actualTopUps.containsAll(dummyTopUps));
    }


    @Test
    void testGetTopUpByUserIdInternalServerError() throws ExecutionException, InterruptedException {
        String userId = "valid-user-id";
        CompletableFuture<List<TopUp>> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException("Internal Server Error"));
        when(topUpService.findAllByUserId(userId)).thenReturn(future);

        CompletableFuture<ResponseEntity<Map<String, Object>>> responseFuture = topUpController.getTopUpByUserId(userId);

        ResponseEntity<?> responseEntity = responseFuture.get();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        if (responseEntity.getBody() instanceof Map) {
            Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
            assertTrue(responseBody.containsKey("message"));
            assertTrue(responseBody.get("message").toString().contains("Something went wrong with the server"));
        } else {
            fail("Unexpected response body type");
        }
    }



}
