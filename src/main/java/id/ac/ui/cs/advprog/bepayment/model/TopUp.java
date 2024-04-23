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
    @Setter
    String status;

    public TopUp(String id, String userId, Wallet wallet, double amount) {
        this.id = id;
        this.userId = userId;
        this.wallet = wallet;
        this.amount = amount;
        this.status = "WAITING_APPROVAL";

        if (wallet == null) {
            throw new IllegalArgumentException("Wallet must not be null");
        }

        if (userId == null) {
            throw new IllegalArgumentException("User not found");
        }

    }
    public TopUp(String id, String userId, Wallet wallet, double amount, String status) {
        this(id,userId,wallet,amount);

        String[] statusList = {"WAITING_APPROVAL", "REJECTED", "SUCCESS", "CANCELED"};
        if(Arrays.stream(statusList).noneMatch(item -> (item.equals(status)))){
            throw new IllegalArgumentException();
        }else{
            this.status = status;
        }


    }


    public void setStatus(String status){
        String[] statusList = {"WAITING_APPROVAL", "REJECTED", "SUCCESS", "CANCELED"};
        if(Arrays.stream(statusList).noneMatch(item -> (item.equals(status)))){
            throw new IllegalArgumentException();
        }else{
            this.status = status;
        }
    }
}
