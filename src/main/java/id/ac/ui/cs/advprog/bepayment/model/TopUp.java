package id.ac.ui.cs.advprog.bepayment.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class TopUp {
    String id;
    Wallet wallet;
    Integer amount;
    @Setter
    String status;

    public TopUp(String id, Wallet wallet, Integer amount, String status) {
        this.id = id;
        this.wallet = wallet;
        this.amount = amount;
        this.status = status;
    }
}
