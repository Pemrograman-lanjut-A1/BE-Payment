package id.ac.ui.cs.advprog.bepayment.model;

import id.ac.ui.cs.advprog.bepayment.enums.TopUpStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Builder
@Getter
@Setter
public class TopUp {
    String id;
    String userId;
    Wallet wallet;
    double amount;
    TopUpStatus status;

    public void setStatus(TopUpStatus status) {
        this.status = status;
    }
    public void setAmount(double amount){
        if(amount <= 0){
            throw new IllegalArgumentException();
        }else{
            this.amount = amount;
        }
    }
}
