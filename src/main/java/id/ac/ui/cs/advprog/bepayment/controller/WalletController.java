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

@RestController
@RequestMapping(path="wallet", produces = "application/json")
@CrossOrigin(origins="*")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @PostMapping("/create")
    public ResponseEntity<?> createWallet(@RequestBody WalletRequest walletRequest){
        return null;
    }
}
