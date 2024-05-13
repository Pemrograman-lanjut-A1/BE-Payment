package id.ac.ui.cs.advprog.bepayment.controller;

import id.ac.ui.cs.advprog.bepayment.config.JwtAuthFilter;
import id.ac.ui.cs.advprog.bepayment.model.TopUp;
import id.ac.ui.cs.advprog.bepayment.pojos.TopUpRequest;
import id.ac.ui.cs.advprog.bepayment.service.TopUpService;
import io.jsonwebtoken.ExpiredJwtException;
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

    private static final String MESSAGE_KEY = "message";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Something Wrong With Server";
    private static final String EXPIRED_JWT_MESSAGE = "JWT token has expired";
    private static final String INVALID_JWT_MESSAGE = "Invalid JWT token";
    private static final String FORBIDDEN_MESSAGE = "You are not authorized to make this request";
    private static final String ERROR_KEY_MESSAGE = "Error";
    @Autowired
    private TopUpService topUpService;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @PostMapping("/create")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> createTopUp(@RequestHeader(value = "Authorization") String token, @RequestBody TopUpRequest topUpRequest) {
        Map<String, Object> response = new HashMap<>();
        String role = null;
        try {
            role = jwtAuthFilter.filterToken(token);
        }catch (ExpiredJwtException e){
            response.put("code", HttpStatus.FORBIDDEN.value());
            response.put(MESSAGE_KEY, EXPIRED_JWT_MESSAGE);
        }catch (Exception e){
            response.put("code", HttpStatus.FORBIDDEN.value());
            response.put(MESSAGE_KEY, INVALID_JWT_MESSAGE);

        }

        if (role == null){
            response.put("code", HttpStatus.FORBIDDEN.value());
            response.put(MESSAGE_KEY, FORBIDDEN_MESSAGE);
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.FORBIDDEN).body(response));
        }

        CompletableFuture<TopUp> createdTopUpFuture = topUpService.createTopUp(topUpRequest);

        return createdTopUpFuture.thenApply(createdTopUp -> {
            response.put("topUp", createdTopUp);
            response.put(MESSAGE_KEY, "Topup Created Successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }).exceptionally(e -> {
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put(ERROR_KEY_MESSAGE, e.getMessage());
            response.put(MESSAGE_KEY, INTERNAL_SERVER_ERROR_MESSAGE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        });
    }


    @DeleteMapping("/")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> deleteAllTopUp(@RequestHeader(value = "Authorization") String token) {
        Map<String, Object> response = new HashMap<>();

        String role = null;
        try {
            role = jwtAuthFilter.filterToken(token);
        }catch (ExpiredJwtException e){
            response.put("code", HttpStatus.FORBIDDEN.value());
            response.put(MESSAGE_KEY, EXPIRED_JWT_MESSAGE);
        }catch (Exception e){
            response.put("code", HttpStatus.FORBIDDEN.value());
            response.put(MESSAGE_KEY, INVALID_JWT_MESSAGE);

        }

        if (role == null){
            response.put("code", HttpStatus.FORBIDDEN.value());
            response.put(MESSAGE_KEY, FORBIDDEN_MESSAGE);
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.FORBIDDEN).body(response));
        }

        CompletableFuture<Object> deletionFuture = topUpService.deleteAllTopUp();

        return deletionFuture.thenApply(deleted -> {
            response.put("code", HttpStatus.OK.value());
            response.put(MESSAGE_KEY, "All top-ups deleted successfully.");
            return ResponseEntity.ok(response);
        }).exceptionally(e -> {
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put(ERROR_KEY_MESSAGE, e.getMessage());
            response.put(MESSAGE_KEY, INTERNAL_SERVER_ERROR_MESSAGE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        });
    }


    @DeleteMapping("/{topUpId}/delete")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> deleteTopUpById(@RequestHeader(value = "Authorization") String token, @PathVariable("topUpId") String topUpId) {
        Map<String, Object> response = new HashMap<>();

        String role = null;
        try {
            role = jwtAuthFilter.filterToken(token);
        } catch (ExpiredJwtException e) {
            response.put("code", HttpStatus.FORBIDDEN.value());
            response.put(MESSAGE_KEY, EXPIRED_JWT_MESSAGE);
        } catch (Exception e) {
            response.put("code", HttpStatus.FORBIDDEN.value());
            response.put(MESSAGE_KEY, INVALID_JWT_MESSAGE);

        }

        if (role == null) {
            response.put("code", HttpStatus.FORBIDDEN.value());
            response.put(MESSAGE_KEY, FORBIDDEN_MESSAGE);
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.FORBIDDEN).body(response));
        }

        return topUpService.deleteTopUpById(topUpId)
                .thenApply(deleted -> {
                    int statusCode = deleted ? HttpStatus.OK.value() : HttpStatus.NOT_FOUND.value();
                    response.put("code", statusCode);
                    response.put(MESSAGE_KEY, deleted ? "Top-up with ID " + topUpId + " deleted successfully." : "Top-up with ID " + topUpId + " not found.");
                    return deleted ? ResponseEntity.ok(response) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                })
                .exceptionally(e -> {
                    response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
                    response.put(ERROR_KEY_MESSAGE, e.getMessage());
                    response.put(MESSAGE_KEY, INTERNAL_SERVER_ERROR_MESSAGE);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                });
    }


        @PutMapping("/{topUpId}/cancel")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> cancelTopUp(@RequestHeader(value = "Authorization") String token, @PathVariable("topUpId") String topUpId) {
            Map<String, Object> response = new HashMap<>();

            String role = null;
            try {
                role = jwtAuthFilter.filterToken(token);
            } catch (ExpiredJwtException e) {
                response.put("code", HttpStatus.FORBIDDEN.value());
                response.put(MESSAGE_KEY, EXPIRED_JWT_MESSAGE);
            } catch (Exception e) {
                response.put("code", HttpStatus.FORBIDDEN.value());
                response.put(MESSAGE_KEY, INVALID_JWT_MESSAGE);

            }

            if (role == null) {
                response.put("code", HttpStatus.FORBIDDEN.value());
                response.put(MESSAGE_KEY, FORBIDDEN_MESSAGE);
                return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.FORBIDDEN).body(response));
            }

            return topUpService.cancelTopUp(topUpId)
                    .thenApply(cancelled -> {
                        int statusCode = cancelled ? HttpStatus.OK.value() : HttpStatus.NOT_FOUND.value();
                        response.put("code", statusCode);
                        response.put(MESSAGE_KEY, cancelled ? "Top-up with ID " + topUpId + " cancelled successfully." : "Top-up with ID " + topUpId + " not found.");
                        return cancelled ? ResponseEntity.ok(response) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                    })
                    .exceptionally(e -> {
                        response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
                        response.put(ERROR_KEY_MESSAGE, e.getMessage());
                        response.put(MESSAGE_KEY, INTERNAL_SERVER_ERROR_MESSAGE);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                    });
        }


            @PutMapping("/{topUpId}/confirm")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> confirmTopUp(@RequestHeader(value = "Authorization") String token, @PathVariable("topUpId") String topUpId) {
                Map<String, Object> response = new HashMap<>();

                String role = null;
                try {
                    role = jwtAuthFilter.filterToken(token);
                } catch (ExpiredJwtException e) {
                    response.put("code", HttpStatus.FORBIDDEN.value());
                    response.put(MESSAGE_KEY, EXPIRED_JWT_MESSAGE);
                } catch (Exception e) {
                    response.put("code", HttpStatus.FORBIDDEN.value());
                    response.put(MESSAGE_KEY, INVALID_JWT_MESSAGE);

                }

                if (role == null) {
                    response.put("code", HttpStatus.FORBIDDEN.value());
                    response.put(MESSAGE_KEY, FORBIDDEN_MESSAGE);
                    return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.FORBIDDEN).body(response));
                } else if (role.equals("REGULAR")) {
                    response.put("code", HttpStatus.FORBIDDEN.value());
                    response.put(MESSAGE_KEY, FORBIDDEN_MESSAGE);
                    return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.FORBIDDEN).body(response));
                }

                return topUpService.confirmTopUp(topUpId)
                        .thenApply(confirmed -> {
                            int statusCode = confirmed ? HttpStatus.OK.value() : HttpStatus.NOT_FOUND.value();
                            response.put("code", statusCode);
                            response.put(MESSAGE_KEY, confirmed ? "Top-up with ID " + topUpId + " confirmed successfully." : "Top-up with ID " + topUpId + " not found.");
                            return confirmed ? ResponseEntity.ok(response) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                        })
                        .exceptionally(e -> {
                            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
                            response.put(ERROR_KEY_MESSAGE, e.getMessage());
                            response.put(MESSAGE_KEY, INTERNAL_SERVER_ERROR_MESSAGE);
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                        });
            }
            @GetMapping("/")
    public CompletableFuture<ResponseEntity<List<TopUp>>> getAllTopUps() {
        Map<String, Object> response = new HashMap<>();

        CompletableFuture<List<TopUp>> topUpsFuture = topUpService.findAll();
        return topUpsFuture.thenApplyAsync(topUps -> ResponseEntity.ok(topUps))
                .exceptionally(exception -> {
                    response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
                    response.put(ERROR_KEY_MESSAGE, exception.getCause() != null ? exception.getCause().getMessage() : "Unknown error");
                    response.put(MESSAGE_KEY,INTERNAL_SERVER_ERROR_MESSAGE);
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
            response.put(ERROR_KEY_MESSAGE, e.getMessage());
            response.put(MESSAGE_KEY, INTERNAL_SERVER_ERROR_MESSAGE);
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
                response.put(MESSAGE_KEY, "Top-up with ID " + topUpId + " not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.ok(topUp);
        }).exceptionally(exception -> {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put(MESSAGE_KEY, INTERNAL_SERVER_ERROR_MESSAGE);

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
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put(MESSAGE_KEY, INTERNAL_SERVER_ERROR_MESSAGE);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        });
    }

}
