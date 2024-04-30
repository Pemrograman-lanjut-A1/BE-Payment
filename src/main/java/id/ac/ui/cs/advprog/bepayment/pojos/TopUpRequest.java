package id.ac.ui.cs.advprog.bepayment.pojos;

import id.ac.ui.cs.advprog.bepayment.enums.TopUpStatus;
import id.ac.ui.cs.advprog.bepayment.model.Wallet;
import jakarta.persistence.*;

public class TopUpRequest {
    public String userId;
    public String walletId;
    public double amount;
}
