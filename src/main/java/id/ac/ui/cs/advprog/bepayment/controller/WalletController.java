package id.ac.ui.cs.advprog.bepayment.controller;

import id.ac.ui.cs.advprog.bepayment.config.JwtAuthFilter;
import id.ac.ui.cs.advprog.bepayment.model.Wallet;
import id.ac.ui.cs.advprog.bepayment.pojos.WalletRequest;
import id.ac.ui.cs.advprog.bepayment.service.WalletService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(path="wallet", produces = "application/json")
@CrossOrigin(origins="*")
public class WalletController {

    private static final String MESSAGE_KEY = "message";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Something Wrong With Server";
    private static final String EXPIRED_JWT_MESSAGE = "JWT token has expired";
    private static final String INVALID_JWT_MESSAGE = "Invalid JWT token";
    private static final String FORBIDDEN_MESSAGE = "You are not authorized to make this request";
    private static final String ERROR_KEY_MESSAGE = "Error";
    private static final String WALLET_ID_MESSAGE = "Wallet with ID ";
    private static final String NOT_FOUND_MESSAGE = " not found.";
    private static final String WALLET_STRING = "wallet";

    private final WalletService walletService;
    private final JwtAuthFilter jwtAuthFilter;
    @Autowired
    public WalletController(WalletService walletService, JwtAuthFilter jwtAuthFilter) {
        this.walletService = walletService;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @PostMapping("/create")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> createWallet(@RequestHeader(value = "Authorization") String token, @RequestBody WalletRequest walletRequest) {
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
        return walletService.createWallet(walletRequest)
                .thenApply(createdWallet -> {
                    response.put(WALLET_STRING, createdWallet);
                    response.put(MESSAGE_KEY, "Wallet Created Successfully");
                    return ResponseEntity.status(HttpStatus.CREATED).body(response);
                })
                .exceptionally(e -> {
                    response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
                    response.put(ERROR_KEY_MESSAGE, e.getMessage());
                    response.put(MESSAGE_KEY, INTERNAL_SERVER_ERROR_MESSAGE);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                });
    }

    @GetMapping("/{walletId}")
    public CompletableFuture<ResponseEntity<Object>> getWalletById(@PathVariable("walletId") String walletId) {
        return walletService.findById(walletId)
                .thenApply(wallet -> {
                    if (wallet == null) {
                        Map<String, Object> response = new HashMap<>();
                        response.put("code", HttpStatus.NOT_FOUND.value());
                        response.put(MESSAGE_KEY, WALLET_ID_MESSAGE + walletId + NOT_FOUND_MESSAGE);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body((Object) response);
                    }
                    return ResponseEntity.ok((Object) wallet);
                })
                .exceptionally(e -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
                    response.put(ERROR_KEY_MESSAGE, INTERNAL_SERVER_ERROR_MESSAGE);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((Object) response);
                });
    }


    @GetMapping("/{userId}/user")
    public CompletableFuture<ResponseEntity<Object>> getWalletByUserId(@PathVariable("userId") String userId) {
        return walletService.findByUserId(userId)
                .thenApply(wallet -> {
                    if (wallet == null) {
                        Map<String, Object> response = new HashMap<>();
                        response.put("code", HttpStatus.NOT_FOUND.value());
                        response.put(MESSAGE_KEY, WALLET_ID_MESSAGE + userId + NOT_FOUND_MESSAGE);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body((Object) response);
                    }
                    return ResponseEntity.ok((Object) wallet);
                })
                .exceptionally(e -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
                    response.put(MESSAGE_KEY, INTERNAL_SERVER_ERROR_MESSAGE);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((Object) response);
                });
    }


    @PutMapping("/{walletId}/{amount}/add")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> addAmount(@RequestHeader(value = "Authorization") String token, @PathVariable("walletId") String walletId, @PathVariable("amount") Double amount) {
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

        CompletableFuture<Wallet> walletFuture = walletService.findById(walletId);
        return walletFuture.thenApply(wallet -> {
            if (wallet == null) {
                response.put("code", HttpStatus.NOT_FOUND.value());
                response.put(MESSAGE_KEY, WALLET_ID_MESSAGE + walletId + NOT_FOUND_MESSAGE);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            if (amount < 0) {
                response.put("code", HttpStatus.BAD_REQUEST.value());
                response.put(MESSAGE_KEY, "totalAmount cannot be negative");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            try {
                walletService.addAmount(walletId, amount);
                response.put(WALLET_STRING, wallet);
                response.put(MESSAGE_KEY, "Wallet Amount has been Added");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalArgumentException("An error occurred while adding amount to the wallet.");
            } catch (ExecutionException e) {
                throw new IllegalArgumentException("An error occurred while adding amount to the wallet.");
            }
        }).exceptionally(e -> {
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put(MESSAGE_KEY, INTERNAL_SERVER_ERROR_MESSAGE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        });
    }


    @PutMapping("/{walletId}/{amount}/decrease")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> decreaseAmount(@RequestHeader(value = "Authorization") String token, @PathVariable("walletId") String walletId, @PathVariable("amount") Double amount) {
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

        return walletService.findById(walletId)
                .thenCompose(wallet -> {
                    if (wallet == null) {
                        response.put("code", HttpStatus.NOT_FOUND.value());
                        response.put(MESSAGE_KEY, WALLET_ID_MESSAGE + walletId + NOT_FOUND_MESSAGE);
                        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NOT_FOUND).body(response));
                    }
                    if (amount < 0){
                        response.put("code", HttpStatus.BAD_REQUEST.value());
                        response.put(MESSAGE_KEY, "totalAmount cannot be negative");
                        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response));
                    }
                    try {
                        return walletService.decreaseAmount(walletId, amount)
                                .thenApply(Void -> {
                                    response.put(WALLET_STRING, wallet);
                                    response.put(MESSAGE_KEY, "Wallet Amount has been Decreased");
                                    return ResponseEntity.status(HttpStatus.OK).body(response);
                                });
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new IllegalArgumentException("An error occurred while adding amount to the wallet.");
                    } catch (ExecutionException e) {
                        throw new IllegalArgumentException("An error occurred while adding amount to the wallet.");
                    }
                })
                .exceptionally(ex -> {
                    if (ex.getCause() instanceof IllegalArgumentException) {
                        response.put("code", HttpStatus.BAD_REQUEST.value());
                        response.put(MESSAGE_KEY, ex.getCause().getMessage());
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    } else {
                        response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
                        response.put(MESSAGE_KEY, INTERNAL_SERVER_ERROR_MESSAGE);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                    }
                });
    }


}
