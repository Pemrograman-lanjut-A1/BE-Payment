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
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TopUpControllerTest {

    @InjectMocks
    private TopUpController topUpController;

    @Mock
    private TopUpService topUpService;

    @Test
    public void testCreateTopUpSuccess() {
        TopUpRequest topUpRequest = new TopUpRequest();
        TopUp createdTopUp = new TopUp();

        when(topUpService.createTopUp(topUpRequest)).thenReturn(createdTopUp);

        ResponseEntity<?> responseEntity = topUpController.createTopUp(topUpRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertInstanceOf(Map.class, responseEntity.getBody());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody.get("topUp"));
    }

    @Test
    public void testCreateTopUpInternalServerError() {
        when(topUpService.createTopUp(any())).thenThrow(new RuntimeException("Internal Server Error"));

        ResponseEntity<?> responseEntity = topUpController.createTopUp(new TopUpRequest());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertInstanceOf(Map.class, responseEntity.getBody());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody.get("error"));
    }

//    @Test
//    public void testDeleteAllTopUpSuccess() {
//        doNothing().when(topUpService).deleteAllTopUp();
//
//        ResponseEntity<?> responseEntity = topUpController.deleteAllTopUp();
//
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
//        assert responseBody != null;
//        assertNotNull(responseBody.get("message"));
//    }

    @Test
    public void testDeleteTopUpByIdSuccess() {
        String topUpId = "dummyId";

        when(topUpService.deleteTopUpById(topUpId)).thenReturn(true);

        ResponseEntity<?> responseEntity = topUpController.deleteTopUpById(topUpId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assert responseBody != null;
        assertNotNull(responseBody.get("message"));
    }

    @Test
    public void testCancelTopUpSuccess() {
        String topUpId = "dummyId";

        when(topUpService.cancelTopUp(topUpId)).thenReturn(true);

        ResponseEntity<?> responseEntity = topUpController.cancelTopUp(topUpId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assert responseBody != null;
        assertNotNull(responseBody.get("message"));
    }

    @Test
    public void testGetAllTopUpsSuccess() {
        List<TopUp> dummyTopUps = Arrays.asList(new TopUp());

        when(topUpService.findAll()).thenReturn(dummyTopUps);

        ResponseEntity<?> responseEntity = topUpController.getAllTopUps();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(dummyTopUps, responseEntity.getBody());
    }


    @Test
    public void testDeleteAllTopUpInternalServerError() {
        doThrow(new RuntimeException("Internal Server Error")).when(topUpService).deleteAllTopUp();

        ResponseEntity<?> responseEntity = topUpController.deleteAllTopUp();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertInstanceOf(Map.class, responseEntity.getBody());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody.get("error"));
    }

    @Test
    public void testDeleteTopUpByIdNotFound() {
        String topUpId = "nonExistentId";

        when(topUpService.deleteTopUpById(topUpId)).thenReturn(false);

        ResponseEntity<?> responseEntity = topUpController.deleteTopUpById(topUpId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertInstanceOf(Map.class, responseEntity.getBody());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody.get("message"));
    }

    @Test
    public void testCancelTopUpNotFound() {
        String topUpId = "nonExistentId";

        when(topUpService.cancelTopUp(topUpId)).thenReturn(false);

        ResponseEntity<?> responseEntity = topUpController.cancelTopUp(topUpId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertInstanceOf(Map.class, responseEntity.getBody());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody.get("message"));
    }

    @Test
    public void testGetAllTopUpsInternalServerError() {
        when(topUpService.findAll()).thenThrow(new RuntimeException("Internal Server Error"));

        ResponseEntity<?> responseEntity = topUpController.getAllTopUps();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertInstanceOf(Map.class, responseEntity.getBody());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody.get("error"));
    }

    @Test
    void testConfirmTopUpSuccessful() {
        String topUpId = "123";
        when(topUpService.confirmTopUp(topUpId)).thenReturn(true);

        ResponseEntity<?> responseEntity = topUpController.confirmTopUp(topUpId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().toString().contains("confirmed successfully"));
    }

    @Test
    void testConfirmTopUpNotFound() {
        String topUpId = "456";
        when(topUpService.confirmTopUp(topUpId)).thenReturn(false);

        ResponseEntity<?> responseEntity = topUpController.confirmTopUp(topUpId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().toString().contains("not found"));
    }

    @Test
    void testConfirmTopUpInternalServerError() {
        String topUpId = "789";
        when(topUpService.confirmTopUp(topUpId)).thenThrow(new RuntimeException("Internal Server Error"));

        ResponseEntity<?> responseEntity = topUpController.confirmTopUp(topUpId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().toString().contains("Something Wrong With Server"));
    }

    @Test
    void testGetTopUpByIdSuccessful() {
        String topUpId = "123";
        TopUp expectedTopUp = new TopUp();
        when(topUpService.findById(topUpId)).thenReturn(expectedTopUp);

        ResponseEntity<?> responseEntity = topUpController.getTopUpById(topUpId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedTopUp, responseEntity.getBody());
    }

    @Test
    void testGetTopUpByIdTopUpNotFound() {
        String topUpId = "789";
        when(topUpService.findById(topUpId)).thenReturn(null);

        ResponseEntity<?> responseEntity = topUpController.getTopUpById(topUpId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().toString().contains("Top-up with ID " + topUpId + " not found"));
    }

    @Test
    void testGetTopUpByIdInternalServerError() {
        String topUpId = "456";
        when(topUpService.findById(topUpId)).thenThrow(new RuntimeException("Internal Server Error"));

        ResponseEntity<?> responseEntity = topUpController.getTopUpById(topUpId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().toString().contains("Something Wrong With Server"));
    }

}
