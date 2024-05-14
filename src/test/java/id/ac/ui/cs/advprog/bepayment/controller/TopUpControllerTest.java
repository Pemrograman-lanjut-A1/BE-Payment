package id.ac.ui.cs.advprog.bepayment.controller;

import id.ac.ui.cs.advprog.bepayment.config.JwtAuthFilter;
import id.ac.ui.cs.advprog.bepayment.model.TopUp;
import id.ac.ui.cs.advprog.bepayment.pojos.TopUpRequest;
import id.ac.ui.cs.advprog.bepayment.service.TopUpService;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopUpControllerTest {

    @InjectMocks
    private TopUpController topUpController;

    @Mock
    private TopUpService topUpService;

    @Mock
    private JwtAuthFilter jwtAuthFilter;


    @Test
    void createTopUpSuccess() {
        when(jwtAuthFilter.filterToken(anyString())).thenReturn("ADMIN");
        when(topUpService.createTopUp(any())).thenReturn(CompletableFuture.completedFuture(new TopUp()));

        ResponseEntity<Map<String, Object>> responseEntity = topUpController.createTopUp("mockedToken", new TopUpRequest()).join();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Topup Created Successfully", responseEntity.getBody().get("message"));
    }

    @Test
    void createTopUpFailure() {
        when(jwtAuthFilter.filterToken(anyString())).thenReturn("ADMIN");
        when(topUpService.createTopUp(any())).thenReturn(CompletableFuture.failedFuture(new RuntimeException("Some error occurred")));

        ResponseEntity<Map<String, Object>> responseEntity = topUpController.createTopUp("mockedToken", new TopUpRequest()).join();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Something Wrong With Server", responseEntity.getBody().get("message"));
    }

    @Test
    void createTopUpExpiredJwtToken() {
        when(jwtAuthFilter.filterToken(anyString())).thenThrow(ExpiredJwtException.class);

        ResponseEntity<Map<String, Object>> responseEntity = topUpController.createTopUp("mockedToken", new TopUpRequest()).join();

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }


    @Test
    void createTopUpEmptyJwtToken() {
        when(jwtAuthFilter.filterToken(anyString())).thenReturn(null);

        ResponseEntity<Map<String, Object>> responseEntity = topUpController.createTopUp("mockedToken", new TopUpRequest()).join();

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals("You are not authorized to make this request", responseEntity.getBody().get("message"));
    }

    @Test
    void deleteAllTopUpSuccess() {
        when(jwtAuthFilter.filterToken(anyString())).thenReturn("ADMIN");
        when(topUpService.deleteAllTopUp()).thenReturn(CompletableFuture.completedFuture(null));

        ResponseEntity<Map<String, Object>> responseEntity = topUpController.deleteAllTopUp("mockedToken").join();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("All top-ups deleted successfully.", responseEntity.getBody().get("message"));
    }

    @Test
    void deleteAllTopUpExpiredJwtToken() {
        when(jwtAuthFilter.filterToken(anyString())).thenThrow(ExpiredJwtException.class);

        ResponseEntity<Map<String, Object>> responseEntity = topUpController.deleteAllTopUp("mockedToken").join();

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    void deleteAllTopUpEmptyJwtToken() {
        when(jwtAuthFilter.filterToken(anyString())).thenReturn(null);

        ResponseEntity<Map<String, Object>> responseEntity = topUpController.deleteAllTopUp("mockedToken").join();

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals("You are not authorized to make this request", responseEntity.getBody().get("message"));
    }

    @Test
    void deleteAllTopUpInternalServerError() {
        when(jwtAuthFilter.filterToken(anyString())).thenReturn("ADMIN");
        when(topUpService.deleteAllTopUp()).thenReturn(CompletableFuture.failedFuture(new RuntimeException("Some error occurred")));

        ResponseEntity<Map<String, Object>> responseEntity = topUpController.deleteAllTopUp("mockedToken").join();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Something Wrong With Server", responseEntity.getBody().get("message"));
    }

    @Test
    void deleteTopUpByIdSuccess() {
        String topUpId = "mockedTopUpId";
        when(jwtAuthFilter.filterToken(anyString())).thenReturn("ADMIN");
        when(topUpService.deleteTopUpById(eq(topUpId))).thenReturn(CompletableFuture.completedFuture(true));

        ResponseEntity<Map<String, Object>> responseEntity = topUpController.deleteTopUpById("mockedToken", topUpId).join();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Top-up with ID mockedTopUpId deleted successfully.", responseEntity.getBody().get("message"));
    }

    @Test
    void deleteTopUpByIdNotFound() {
        String topUpId = "mockedTopUpId";
        when(jwtAuthFilter.filterToken(anyString())).thenReturn("ADMIN");
        when(topUpService.deleteTopUpById(eq(topUpId))).thenReturn(CompletableFuture.completedFuture(false));

        ResponseEntity<Map<String, Object>> responseEntity = topUpController.deleteTopUpById("mockedToken", topUpId).join();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Top-up with ID mockedTopUpId not found.", responseEntity.getBody().get("message"));
    }

    @Test
    void deleteTopUpByIdExpiredJwtToken() {
        when(jwtAuthFilter.filterToken(anyString())).thenThrow(ExpiredJwtException.class);

        ResponseEntity<Map<String, Object>> responseEntity = topUpController.deleteTopUpById("mockedToken", "mockedTopUpId").join();

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }


    @Test
    void deleteTopUpByIdEmptyJwtToken() {
        when(jwtAuthFilter.filterToken(anyString())).thenReturn(null);

        ResponseEntity<Map<String, Object>> responseEntity = topUpController.deleteTopUpById("mockedToken", "mockedTopUpId").join();

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals("You are not authorized to make this request", responseEntity.getBody().get("message"));
    }

    @Test
    void deleteTopUpByIdInternalServerError() {
        String topUpId = "mockedTopUpId";
        when(jwtAuthFilter.filterToken(anyString())).thenReturn("ADMIN");
        when(topUpService.deleteTopUpById(eq(topUpId))).thenReturn(CompletableFuture.failedFuture(new RuntimeException("Some error occurred")));

        ResponseEntity<Map<String, Object>> responseEntity = topUpController.deleteTopUpById("mockedToken", topUpId).join();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Something Wrong With Server", responseEntity.getBody().get("message"));
    }

    @Test
    void cancelTopUpSuccess() {
        String topUpId = "mockedTopUpId";
        when(jwtAuthFilter.filterToken(anyString())).thenReturn("ADMIN");
        when(topUpService.cancelTopUp(eq(topUpId))).thenReturn(CompletableFuture.completedFuture(true));

        ResponseEntity<Map<String, Object>> responseEntity = topUpController.cancelTopUp("mockedToken", topUpId).join();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Top-up with ID mockedTopUpId cancelled successfully.", responseEntity.getBody().get("message"));
    }

    @Test
    void cancelTopUpNotFound() {
        String topUpId = "mockedTopUpId";
        when(jwtAuthFilter.filterToken(anyString())).thenReturn("ADMIN");
        when(topUpService.cancelTopUp(eq(topUpId))).thenReturn(CompletableFuture.completedFuture(false));

        ResponseEntity<Map<String, Object>> responseEntity = topUpController.cancelTopUp("mockedToken", topUpId).join();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Top-up with ID mockedTopUpId not found.", responseEntity.getBody().get("message"));
    }

    @Test
    void cancelTopUpExpiredJwtToken() {
        when(jwtAuthFilter.filterToken(anyString())).thenThrow(ExpiredJwtException.class);

        ResponseEntity<Map<String, Object>> responseEntity = topUpController.cancelTopUp("mockedToken", "mockedTopUpId").join();

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    void cancelTopUpEmptyJwtToken() {
        when(jwtAuthFilter.filterToken(anyString())).thenReturn(null);

        ResponseEntity<Map<String, Object>> responseEntity = topUpController.cancelTopUp("mockedToken", "mockedTopUpId").join();

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals("You are not authorized to make this request", responseEntity.getBody().get("message"));
    }

    @Test
    void cancelTopUpInternalServerError() {
        String topUpId = "mockedTopUpId";
        when(jwtAuthFilter.filterToken(anyString())).thenReturn("ADMIN");
        when(topUpService.cancelTopUp(eq(topUpId))).thenReturn(CompletableFuture.failedFuture(new RuntimeException("Some error occurred")));

        ResponseEntity<Map<String, Object>> responseEntity = topUpController.cancelTopUp("mockedToken", topUpId).join();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Something Wrong With Server", responseEntity.getBody().get("message"));
    }

    @Test
    void testGetAllTopUpsSuccess() {
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
    void testGetAllWaitingTopUpsSuccess() {
        List<TopUp> dummyTopUps = Arrays.asList(new TopUp());
        CompletableFuture<List<TopUp>> completedFuture = CompletableFuture.completedFuture(dummyTopUps);

        when(topUpService.findAllWaiting()).thenReturn(completedFuture);

        CompletableFuture<ResponseEntity<List<TopUp>>> responseFuture = topUpController.getAllWaitingTopUps();

        responseFuture.thenAccept(responseEntity -> {
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(dummyTopUps, responseEntity.getBody());
        }).join();
    }


    @Test
    void testGetAllTopUpsInternalServerError() {
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
    void testGetAllWaitingTopUpsInternalServerError() {
        CompletableFuture<List<TopUp>> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException("Internal Server Error"));
        when(topUpService.findAllWaiting()).thenReturn(future);

        CompletableFuture<ResponseEntity<List<TopUp>>> responseFuture = topUpController.getAllWaitingTopUps();

        responseFuture.thenAccept(responseEntity -> {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
            assertEquals(Collections.emptyList(), responseEntity.getBody());
        }).join();
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
            assertEquals("Something Wrong With Server", responseBody.get("message"));
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
            assertTrue(responseBody.get("message").toString().contains("Something Wrong With Server"));
        } else {
            fail("Unexpected response body type");
        }
    }

    @Test
    void confirmTopUpExpiredJwtToken() {
        when(jwtAuthFilter.filterToken(anyString())).thenThrow(ExpiredJwtException.class);

        ResponseEntity<Map<String, Object>> responseEntity = topUpController.confirmTopUp("mockedToken", "mockedTopUpId").join();

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    void confirmTopUpSuccess() {
        when(jwtAuthFilter.filterToken(anyString())).thenReturn("ADMIN");

        when(topUpService.confirmTopUp(anyString())).thenReturn(CompletableFuture.completedFuture(true));

        ResponseEntity<Map<String, Object>> responseEntity = topUpController.confirmTopUp("mockedToken", "mockedTopUpId").join();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Top-up with ID mockedTopUpId confirmed successfully.", responseEntity.getBody().get("message"));
    }

    @Test
    void confirmTopUpUnauthorized() {
        when(jwtAuthFilter.filterToken(anyString())).thenReturn("REGULAR");

        ResponseEntity<Map<String, Object>> responseEntity = topUpController.confirmTopUp("mockedToken", "mockedTopUpId").join();

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals("You are not authorized to make this request", responseEntity.getBody().get("message"));
    }

    @Test
    void confirmTopUpInternalServerError() {
        when(jwtAuthFilter.filterToken(anyString())).thenReturn("ADMIN");

        when(topUpService.confirmTopUp(anyString())).thenReturn(CompletableFuture.failedFuture(new RuntimeException("Some error occurred")));

        ResponseEntity<Map<String, Object>> responseEntity = topUpController.confirmTopUp("mockedToken", "mockedTopUpId").join();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Something Wrong With Server", responseEntity.getBody().get("message"));
    }

}
