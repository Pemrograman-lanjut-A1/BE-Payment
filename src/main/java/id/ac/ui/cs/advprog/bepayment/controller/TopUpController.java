package id.ac.ui.cs.advprog.bepayment.controller;

import id.ac.ui.cs.advprog.bepayment.model.TopUp;
import id.ac.ui.cs.advprog.bepayment.pojos.TopUpRequest;
import id.ac.ui.cs.advprog.bepayment.service.TopUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="topup", produces = "application/json")
@CrossOrigin(origins="*")
public class TopUpController {

    @Autowired
    private TopUpService topUpService;

    @PostMapping("/create")
    public ResponseEntity<?> createTopUp(@RequestBody TopUpRequest topUpRequest){
        return null;

    }

    @DeleteMapping("/")
    public ResponseEntity<?> deleteAllTopUp(){
        return null;
    }

    @DeleteMapping("/{topUpId}/delete")
    public ResponseEntity<?> deleteTopUpById(@PathVariable("topUpId") String topUpId){
        return null;
    }

    @PutMapping("/{topUpId}/cancel")
    public ResponseEntity<?> cancelTopUp(@PathVariable("topUpId") String topUpId){
        return null;
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllTopUps(){
        return null;
    }

}
