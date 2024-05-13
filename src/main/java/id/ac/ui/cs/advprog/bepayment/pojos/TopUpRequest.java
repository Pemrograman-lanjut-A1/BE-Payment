package id.ac.ui.cs.advprog.bepayment.pojos;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TopUpRequest {
    public String userId;
    public String walletId;
    public double amount;
    public String topUpMethod;
}
