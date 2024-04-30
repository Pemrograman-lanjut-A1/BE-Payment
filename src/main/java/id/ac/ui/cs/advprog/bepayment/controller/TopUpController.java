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
        Map<String, Object> response = new HashMap<>();
        try{
            TopUp createdTopUp = topUpService.createTopUp(topUpRequest);
            response.put("topUp", createdTopUp);
            response.put("message", "Topup Created Successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (Exception e){
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("error", e.getMessage());
            response.put("message", "Something Wrong With Server");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    @DeleteMapping("/")
    public ResponseEntity<?> deleteAllTopUp(){
        Map<String, Object> response = new HashMap<>();
        try {
            topUpService.deleteAllTopUp();
            response.put("code", HttpStatus.OK.value());
            response.put("message", "All top-ups deleted successfully.");
            return ResponseEntity.ok(response);
        }catch (Exception e){
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("error", e.getMessage());
            response.put("message", "Something Wrong With Server");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{topUpId}/delete")
    public ResponseEntity<?> deleteTopUpById(@PathVariable("topUpId") String topUpId){
        Map<String, Object> response = new HashMap<>();
        try {
            boolean deleted = topUpService.deleteTopUpById(topUpId);
            if (deleted) {
                response.put("code", HttpStatus.OK.value());
                response.put("message", "Top-up with ID " + topUpId + " deleted successfully.");
                return ResponseEntity.ok(response);
            } else {
                response.put("code", HttpStatus.NOT_FOUND.value());
                response.put("message", "Top-up with ID " + topUpId + " not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        }catch (Exception e){
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("error", e.getMessage());
            response.put("message", "Something Wrong With Server");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{topUpId}/cancel")
    public ResponseEntity<?> cancelTopUp(@PathVariable("topUpId") String topUpId){
        Map<String, Object> response = new HashMap<>();
        try {
            boolean cancelled = topUpService.cancelTopUp(topUpId);
            if (cancelled) {
                response.put("code", HttpStatus.OK.value());
                response.put("message", "Top-up with ID " + topUpId +" cancelled successfully.");
                return ResponseEntity.ok(response);
            } else {
                response.put("code", HttpStatus.NOT_FOUND.value());
                response.put("message", "Top-up with ID " + topUpId + " not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        }catch (Exception e){
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("error", e.getMessage());
            response.put("message", "Something Wrong With Server");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{topUpId}/confirm")
    public ResponseEntity<?> confirmTopUp(@PathVariable("topUpId") String topUpId){
        Map<String, Object> response = new HashMap<>();
        try {
            boolean confirmed = topUpService.confirmTopUp(topUpId);
            if (confirmed) {
                response.put("code", HttpStatus.OK.value());
                response.put("message", "Top-up with ID " + topUpId +" confirmed successfully.");
                return ResponseEntity.ok(response);
            } else {
                response.put("code", HttpStatus.NOT_FOUND.value());
                response.put("message", "Top-up with ID " + topUpId + " not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        }catch (Exception e){
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("error", e.getMessage());
            response.put("message", "Something Wrong With Server");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllTopUps(){
        try {
            List<TopUp> topUps = topUpService.findAll();
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
    public ResponseEntity<?> getTopUpById(@PathVariable("topUpId") String topUpId){
        Map<String, Object> response = new HashMap<>();
        try {
            TopUp topUp = topUpService.findById(topUpId);
            if (topUp == null){
                response.put("code", HttpStatus.NOT_FOUND.value());
                response.put("message", "Top-up with ID " + topUpId + " not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.ok(topUp);
        }catch (Exception e){
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("error", e.getMessage());
            response.put("message", "Something Wrong With Server");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
