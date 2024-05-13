package id.ac.ui.cs.advprog.bepayment.pojos;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TopUpRequest {
    String userId;
    String walletId;
    double amount;
    String topUpMethod;
}
