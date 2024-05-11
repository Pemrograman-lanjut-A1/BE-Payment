package id.ac.ui.cs.advprog.bepayment.controller;

import id.ac.ui.cs.advprog.bepayment.model.Wallet;
import id.ac.ui.cs.advprog.bepayment.pojos.WalletRequest;
import id.ac.ui.cs.advprog.bepayment.service.WalletService;
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
    @Autowired
    private WalletService walletService;

    @PostMapping("/create")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> createWallet(@RequestBody WalletRequest walletRequest) {
        return walletService.createWallet(walletRequest)
                .thenApply(createdWallet -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("wallet", createdWallet);
                    response.put("message", "Wallet Created Successfully");
                    return ResponseEntity.status(HttpStatus.CREATED).body(response);
                })
                .exceptionally(e -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
                    response.put("error", e.getMessage());
                    response.put("message", "Something Wrong With Server");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                });
    }

    @GetMapping("/{walletId}")
    public CompletableFuture<ResponseEntity<?>> getWalletById(@PathVariable("walletId") String walletId) {
        return walletService.findById(walletId)
                .thenApply(wallet -> {
                    if (wallet == null) {
                        Map<String, Object> response = new HashMap<>();
                        response.put("code", HttpStatus.NOT_FOUND.value());
                        response.put("message", "Wallet with ID " + walletId + " not found.");
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                    }
                    return ResponseEntity.ok(wallet);
                })
                .exceptionally(e -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
                    response.put("error", e.getMessage());
                    response.put("message", "Something Wrong With Server");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                });
    }

    @GetMapping("/{userId}/user")
    public CompletableFuture<ResponseEntity<?>> getWalletByUserId(@PathVariable("userId") String userId) {
        return walletService.findByUserId(userId)
                .thenApply(wallet -> {
                    if (wallet == null) {
                        Map<String, Object> response = new HashMap<>();
                        response.put("code", HttpStatus.NOT_FOUND.value());
                        response.put("message", "Wallet for user ID " + userId + " not found.");
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                    }
                    return ResponseEntity.ok(wallet);
                })
                .exceptionally(e -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
                    response.put("error", "Internal Server Error");
                    response.put("message", "Something Wrong With Server");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                });
    }

    @PutMapping("/{walletId}/{amount}/add")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> addAmount(@PathVariable("walletId") String walletId, @PathVariable("amount") Double amount) {
        Map<String, Object> response = new HashMap<>();
        CompletableFuture<Wallet> walletFuture = walletService.findById(walletId);
        return walletFuture.thenApply(wallet -> {
            if (wallet == null) {
                response.put("code", HttpStatus.NOT_FOUND.value());
                response.put("message", "Wallet with ID " + walletId + " not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            if (amount < 0) {
                response.put("code", HttpStatus.BAD_REQUEST.value());
                response.put("message", "totalAmount cannot be negative");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            try {
                walletService.addAmount(walletId, amount);
                response.put("wallet", wallet);
                response.put("message", "Wallet Amount has been Added");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).exceptionally(e -> {
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "Something went wrong on the server side");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        });
    }


    @PutMapping("/{walletId}/{amount}/decrease")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> decreaseAmount(@PathVariable("walletId") String walletId, @PathVariable("amount") Double amount) {
        Map<String, Object> response = new HashMap<>();

        return walletService.findById(walletId)
                .thenCompose(wallet -> {
                    if (wallet == null) {
                        response.put("code", HttpStatus.NOT_FOUND.value());
                        response.put("message", "Wallet with ID " + walletId + " not found.");
                        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NOT_FOUND).body(response));
                    }
                    try {
                        return walletService.decreaseAmount(walletId, amount)
                                .thenApply((Void) -> {
                                    response.put("wallet", wallet);
                                    response.put("message", "Wallet Amount has been Decreased");
                                    return ResponseEntity.status(HttpStatus.OK).body(response);
                                });
                    } catch (ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .exceptionally(ex -> {
                    if (ex.getCause() instanceof IllegalArgumentException) {
                        response.put("code", HttpStatus.BAD_REQUEST.value());
                        response.put("message", ex.getCause().getMessage());
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    } else {
                        response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
                        response.put("message", "Something went wrong on the server side");
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                    }
                });
    }



}
