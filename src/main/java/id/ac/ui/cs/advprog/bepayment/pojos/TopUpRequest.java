package id.ac.ui.cs.advprog.bepayment.pojos;

import id.ac.ui.cs.advprog.bepayment.enums.TopUpStatus;
import id.ac.ui.cs.advprog.bepayment.model.Wallet;
import jakarta.persistence.*;

public class TopUpRequest {
    public String userId;
    public Wallet wallet_id;
    public double amount;
    public TopUpStatus status;
}
