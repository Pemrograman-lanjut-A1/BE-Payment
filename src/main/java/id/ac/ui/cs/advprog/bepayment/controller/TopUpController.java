package id.ac.ui.cs.advprog.bepayment.controller;

import id.ac.ui.cs.advprog.bepayment.model.TopUp;
import id.ac.ui.cs.advprog.bepayment.pojos.TopUpRequest;
import id.ac.ui.cs.advprog.bepayment.service.TopUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path="topup", produces = "application/json")
@CrossOrigin(origins="*")
public class TopUpController {

    @Autowired
    private TopUpService topUpService;

    @PostMapping("/create")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> createTopUp(@RequestBody TopUpRequest topUpRequest) {
        Map<String, Object> response = new HashMap<>();
        CompletableFuture<TopUp> createdTopUpFuture = topUpService.createTopUp(topUpRequest);

        return createdTopUpFuture.thenApply(createdTopUp -> {
            response.put("topUp", createdTopUp);
            response.put("message", "Topup Created Successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }).exceptionally(e -> {
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("error", e.getMessage());
            response.put("message", "Something Wrong With Server");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        });
    }


    @DeleteMapping("/")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> deleteAllTopUp() {
        Map<String, Object> response = new HashMap<>();
        CompletableFuture<Object> deletionFuture = topUpService.deleteAllTopUp();

        return deletionFuture.thenApply(deleted -> {
            response.put("code", HttpStatus.OK.value());
            response.put("message", "All top-ups deleted successfully.");
            return ResponseEntity.ok(response);
        }).exceptionally(e -> {
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("error", e.getMessage());
            response.put("message", "Something Wrong With Server");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        });
    }


    @DeleteMapping("/{topUpId}/delete")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> deleteTopUpById(@PathVariable("topUpId") String topUpId) {
        Map<String, Object> response = new HashMap<>();
        return topUpService.deleteTopUpById(topUpId)
                .thenApply(deleted -> {
                    if (deleted) {
                        response.put("code", HttpStatus.OK.value());
                        response.put("message", "Top-up with ID " + topUpId + " deleted successfully.");
                        return ResponseEntity.ok(response);
                    } else {
                        response.put("code", HttpStatus.NOT_FOUND.value());
                        response.put("message", "Top-up with ID " + topUpId + " not found.");
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                    }
                })
                .exceptionally(e -> {
                    response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
                    response.put("error", e.getMessage());
                    response.put("message", "Something Wrong With Server");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                });
    }


    @PutMapping("/{topUpId}/cancel")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> cancelTopUp(@PathVariable("topUpId") String topUpId) {
        Map<String, Object> response = new HashMap<>();

        return topUpService.cancelTopUp(topUpId)
                .thenApply(cancelled -> {
                    if (cancelled) {
                        response.put("code", HttpStatus.OK.value());
                        response.put("message", "Top-up with ID " + topUpId + " cancelled successfully.");
                        return ResponseEntity.ok(response);
                    } else {
                        response.put("code", HttpStatus.NOT_FOUND.value());
                        response.put("message", "Top-up with ID " + topUpId + " not found.");
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                    }
                })
                .exceptionally(e -> {
                    response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
                    response.put("error", e.getMessage());
                    response.put("message", "Something Wrong With Server");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                });
    }


    @PutMapping("/{topUpId}/confirm")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> confirmTopUp(@PathVariable("topUpId") String topUpId) {
        Map<String, Object> response = new HashMap<>();

        return topUpService.confirmTopUp(topUpId)
                .thenApply(confirmed -> {
                    if (confirmed) {
                        response.put("code", HttpStatus.OK.value());
                        response.put("message", "Top-up with ID " + topUpId + " confirmed successfully.");
                        return ResponseEntity.ok(response);
                    } else {
                        response.put("code", HttpStatus.NOT_FOUND.value());
                        response.put("message", "Top-up with ID " + topUpId + " not found.");
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                    }
                })
                .exceptionally(e -> {
                    response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
                    response.put("error", e.getMessage());
                    response.put("message", "Something Wrong With Server");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                });
    }


    @GetMapping("/")
    public CompletableFuture<ResponseEntity<List<TopUp>>> getAllTopUps() {
        CompletableFuture<List<TopUp>> topUpsFuture = topUpService.findAll();
        return topUpsFuture.thenApplyAsync(topUps -> ResponseEntity.ok(topUps))
                .exceptionally(exception -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
                    response.put("error", exception.getCause() != null ? exception.getCause().getMessage() : "Unknown error");
                    response.put("message", "Something went wrong with the server");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
                });
    }




    @GetMapping("/waiting")
    public ResponseEntity<?> getAllWaitingTopUps(){
        try {
            List<TopUp> topUps = topUpService.findAllWaiting();
            return ResponseEntity.ok(topUps);
        }catch (Exception e){
            Map<String, Object> response = new HashMap<>();
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("error", e.getMessage());
            response.put("message", "Something Wrong With Server");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{topUpId}")
    public CompletableFuture<ResponseEntity<?>> getTopUpById(@PathVariable("topUpId") String topUpId) {
        CompletableFuture<TopUp> topUpFuture = topUpService.findById(topUpId);
        return topUpFuture.thenApplyAsync(topUp -> {
            if (topUp == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("code", HttpStatus.NOT_FOUND.value());
                response.put("message", "Top-up with ID " + topUpId + " not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.ok(topUp);
        }).exceptionally(exception -> {
            // Create a map containing the error details
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("error", "Internal Server Error");
            errorResponse.put("message", "Something went wrong with the server");

            // Return the error response as the body of the response entity
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        });
    }

    @GetMapping("/all/{userId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getTopUpByUserId(@PathVariable("userId") String userId) {
        CompletableFuture<List<TopUp>> topUpsFuture = topUpService.findAllByUserId(userId);
        return topUpsFuture.thenApplyAsync(topUps -> {
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("topUps", topUps);
            return ResponseEntity.ok(successResponse);
        }).exceptionally(exception -> {
            // Create a map containing the error details
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("error", "Internal Server Error");
            errorResponse.put("message", "Something went wrong with the server");

            // Return the error response as the body of the response entity
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        });
    }




}
