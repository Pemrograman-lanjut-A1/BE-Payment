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
    private static final String TOP_UP_ID_MESSAGE = "Top-up with ID ";
    private static final String NOT_FOUND_MESSAGE = " not found.";
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
        }catch (Exception e){
            handleJwtException(e);
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
            Map<String, Object> errorResponse = handleInternalError((Exception) e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        });
    }


    @DeleteMapping("/")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> deleteAllTopUp(@RequestHeader(value = "Authorization") String token) {
        Map<String, Object> response = new HashMap<>();

        String role = null;
        try {
            role = jwtAuthFilter.filterToken(token);
        }catch (Exception e){
            handleJwtException(e);
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
            Map<String, Object> errorResponse = handleInternalError((Exception) e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        });
    }


    @DeleteMapping("/{topUpId}/delete")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> deleteTopUpById(@RequestHeader(value = "Authorization") String token, @PathVariable("topUpId") String topUpId) {
        Map<String, Object> response = new HashMap<>();

        String role = null;
        try {
            role = jwtAuthFilter.filterToken(token);
        }catch (Exception e){
            handleJwtException(e);
        }

        if (role == null) {
            Map<String, Object> forbiddenResponse = handleForbidden();
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.FORBIDDEN).body(forbiddenResponse));
        }

        return topUpService.deleteTopUpById(topUpId)
                .thenApply(deleted -> {
                    if (Boolean.TRUE.equals(deleted)) {
                        response.put("code", HttpStatus.OK.value());
                        response.put(MESSAGE_KEY, TOP_UP_ID_MESSAGE + topUpId + " deleted successfully.");
                        return ResponseEntity.ok(response);
                    } else {
                        response.put("code", HttpStatus.NOT_FOUND.value());
                        response.put(MESSAGE_KEY, TOP_UP_ID_MESSAGE + topUpId + NOT_FOUND_MESSAGE);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                    }
                })
                .exceptionally(e -> {
                    Map<String, Object> errorResponse = handleInternalError((Exception) e);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
                });
    }



    @PutMapping("/{topUpId}/cancel")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> cancelTopUp(@RequestHeader(value = "Authorization") String token, @PathVariable("topUpId") String topUpId) {
            Map<String, Object> response = new HashMap<>();

            String role = null;
            try {
                role = jwtAuthFilter.filterToken(token);
            }catch (Exception e){
                handleJwtException(e);
            }

            if (role == null) {
                Map<String, Object> forbiddenResponse = handleForbidden();
                return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.FORBIDDEN).body(forbiddenResponse));
            }

            return topUpService.cancelTopUp(topUpId)
                    .thenApply(cancelled -> {
                        if (Boolean.TRUE.equals(cancelled)) {
                            response.put("code", HttpStatus.OK.value());
                            response.put(MESSAGE_KEY, TOP_UP_ID_MESSAGE + topUpId + " cancelled successfully.");
                            return ResponseEntity.ok(response);
                        } else {
                            response.put("code", HttpStatus.NOT_FOUND.value());
                            response.put(MESSAGE_KEY, TOP_UP_ID_MESSAGE + topUpId + NOT_FOUND_MESSAGE);
                            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                        }
                    })
                    .exceptionally(e -> {
                        Map<String, Object> errorResponse = handleInternalError((Exception) e);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
                    });
        }


            @PutMapping("/{topUpId}/confirm")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> confirmTopUp(@RequestHeader(value = "Authorization") String token, @PathVariable("topUpId") String topUpId) {
                Map<String, Object> response = new HashMap<>();

                String role = null;
                try {
                    role = jwtAuthFilter.filterToken(token);
                }catch (Exception e){
                    handleJwtException(e);
                }

                if (role == null || role.equals("REGULAR")) {
                    Map<String, Object> forbiddenResponse = handleForbidden();
                    return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.FORBIDDEN).body(forbiddenResponse));
                }

                return topUpService.confirmTopUp(topUpId)
                        .thenApply(confirmed -> {
                            if (Boolean.TRUE.equals(confirmed)) {
                                response.put("code", HttpStatus.OK.value());
                                response.put(MESSAGE_KEY, TOP_UP_ID_MESSAGE + topUpId + " confirmed successfully.");
                                return ResponseEntity.ok(response);
                            } else {
                                response.put("code", HttpStatus.NOT_FOUND.value());
                                response.put(MESSAGE_KEY, TOP_UP_ID_MESSAGE + topUpId + NOT_FOUND_MESSAGE);
                                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                            }
                        })
                        .exceptionally(e -> {
                            Map<String, Object> errorResponse = handleInternalError((Exception) e);
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
                        });
            }

            @GetMapping("/")
            public CompletableFuture<ResponseEntity<List<TopUp>>> getAllTopUps() {
                CompletableFuture<List<TopUp>> topUpsFuture = topUpService.findAll();
                return topUpsFuture.thenApplyAsync(topUps -> ResponseEntity.ok(topUps))
                        .exceptionally(exception -> {
                            List<TopUp> emptyList = Collections.emptyList();
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(emptyList);
                        });
            }



    @GetMapping("/waiting")
<<<<<<< HEAD
    public CompletableFuture<ResponseEntity<List<TopUp>>> getAllWaitingTopUps(){
        CompletableFuture<List<TopUp>> topUpsFuture = topUpService.findAllWaiting();
        return topUpsFuture.thenApplyAsync(topUps -> ResponseEntity.ok(topUps))
                .exceptionally(exception -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
                    response.put("error", exception.getCause() != null ? exception.getCause().getMessage() : "Unknown error");
                    response.put("message", "Something went wrong with the server");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
                });
=======
    public ResponseEntity<?> getAllWaitingTopUps(){
        try {
            List<TopUp> topUps = topUpService.findAllWaiting();
            return ResponseEntity.ok(topUps);
        }catch (Exception e){
            Map<String, Object> response = handleInternalError(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
>>>>>>> d573aa2 ([REFACTOR] Decrease Redundant Code.)
    }

    @GetMapping("/{topUpId}")
    public CompletableFuture<ResponseEntity<?>> getTopUpById(@PathVariable("topUpId") String topUpId) {
        CompletableFuture<TopUp> topUpFuture = topUpService.findById(topUpId);
        return topUpFuture.thenApplyAsync(topUp -> {
            if (topUp == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("code", HttpStatus.NOT_FOUND.value());
                response.put(MESSAGE_KEY, TOP_UP_ID_MESSAGE + topUpId + NOT_FOUND_MESSAGE);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.ok(topUp);
        }).exceptionally(exception -> {
            Map<String, Object> errorResponse = handleInternalError((Exception) exception);
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
            Map<String, Object> errorResponse = handleInternalError((Exception) exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        });
    }

    private Map<String, Object> handleJwtException(Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.FORBIDDEN.value());
        response.put(MESSAGE_KEY, e instanceof ExpiredJwtException ? EXPIRED_JWT_MESSAGE : INVALID_JWT_MESSAGE);
        return response;
    }

    private Map<String, Object> handleInternalError(Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put(ERROR_KEY_MESSAGE, e.getMessage());
        response.put(MESSAGE_KEY, INTERNAL_SERVER_ERROR_MESSAGE);
        return response;
    }

    private Map<String, Object> handleForbidden() {
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.FORBIDDEN.value());
        response.put(MESSAGE_KEY, FORBIDDEN_MESSAGE);
        return response;
    }



}
