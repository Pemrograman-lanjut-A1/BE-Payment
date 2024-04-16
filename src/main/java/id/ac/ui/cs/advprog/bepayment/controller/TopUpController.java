package id.ac.ui.cs.advprog.bepayment.controller;

import id.ac.ui.cs.advprog.bepayment.model.TopUp;
import id.ac.ui.cs.advprog.bepayment.service.TopUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="topup", produces = "application/json")
@CrossOrigin(origins="*")
public class TopUpController {

    @Autowired
    TopUpService topUpService;

    @PostMapping("/create")
    public String createTopUp(){
        topUpService.createTopUp();
        return "createTopUp";
    }
    @PostMapping("/delete-all")
    public String deleteAllTopUp(){
        topUpService.deleteAllTopUp();
        return "deleteAllTopUp";
    }
    @PostMapping("/delete/{topUpId}")
    public String deleteTopUpById(@PathVariable("topUpId") String topUpId){
        topUpService.deleteTopUpById(topUpId);
        return "deleteTopUpId";
    }
    @PostMapping("/cancel/{topUpId}")
    public String cancelTopUp(@PathVariable("topUpId") String topUpId){
        topUpService.cancelTopUp(topUpId);
        return "cancelTopUp";
    }
    @PostMapping("/list")
    public String topUpListPage(){
        return "TopUpList";
    }
}
