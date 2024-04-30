package id.ac.ui.cs.advprog.bepayment.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class Wallet {
    String id;
    String userId;
    @Setter
    Integer amount;



    public void addAmount(int amount){
        this.amount += amount;
    }

    public void subtractAmount(int amount) {
        if (amount > this.amount) {
            this.amount = 0;
        } else {
            this.amount -= amount;
        }
    }
}


