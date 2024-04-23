package id.ac.ui.cs.advprog.bepayment.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Builder
@Getter
public class TopUp {
    String id;
    String userId;
    Wallet wallet;
    double amount;
    String status;

    public void setStatus(String status){
        String[] statusList = {"WAITING_APPROVAL", "REJECTED", "SUCCESS", "CANCELED"};
        if(Arrays.stream(statusList).noneMatch(item -> (item.equals(status)))){
            throw new IllegalArgumentException();
        }else{
            this.status = status;
        }
    }
}
