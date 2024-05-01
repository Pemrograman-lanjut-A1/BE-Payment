package id.ac.ui.cs.advprog.bepayment.controller;

import id.ac.ui.cs.advprog.bepayment.model.TopUp;
import id.ac.ui.cs.advprog.bepayment.model.Wallet;
import id.ac.ui.cs.advprog.bepayment.pojos.WalletRequest;
import id.ac.ui.cs.advprog.bepayment.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="wallet", produces = "application/json")
@CrossOrigin(origins="*")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @PostMapping("/create")
    public ResponseEntity<?> createWallet(@RequestBody WalletRequest walletRequest){
        Map<String, Object> response = new HashMap<>();
        try{
            Wallet createdWallet = walletService.createWallet(walletRequest);
            response.put("wallet", createdWallet);
            response.put("message", "Wallet Created Successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (Exception e){
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("error", e.getMessage());
            response.put("message", "Something Wrong With Server");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @GetMapping("/{walletId}")
    public ResponseEntity<?> getTopUpById(@PathVariable("walletId") String walletId){
        Map<String, Object> response = new HashMap<>();
        try {
            Wallet wallet = walletService.findById(walletId);
            if (wallet == null){
                response.put("code", HttpStatus.NOT_FOUND.value());
                response.put("message", "Wallet with ID " + walletId + " not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.ok(wallet);
        }catch (Exception e){
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("error", e.getMessage());
            response.put("message", "Something Wrong With Server");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{userId}/user")
    public ResponseEntity<?> getWalletByUserId(@PathVariable("userId") String userId){
        Map<String, Object> response = new HashMap<>();
        try {
            Wallet wallet = walletService.findByUserId(userId);
            return ResponseEntity.ok(wallet);
        }catch (Exception e){
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("error", e.getMessage());
            response.put("message", "Something Wrong With Server");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
