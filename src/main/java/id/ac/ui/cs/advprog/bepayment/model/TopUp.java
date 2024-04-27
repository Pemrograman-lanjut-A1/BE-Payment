package id.ac.ui.cs.advprog.bepayment.model;

import id.ac.ui.cs.advprog.bepayment.enums.TopUpStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity(name = "topup")
public class TopUp {
    @Id
    private String id;
    private String userId;
    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;
    private double amount;
    private TopUpStatus status;

    public TopUp(String id, String userId, Wallet wallet, double amount, TopUpStatus status) {
        if (userId == null || wallet == null) {
            throw new IllegalArgumentException("UserId and Wallet cannot be null");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        this.id = id;
        this.userId = userId;
        this.wallet = wallet;
        this.amount = amount;
        this.status = status;
    }
    public void setAmount(double amount) {
        if(amount <= 0){
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        this.amount = amount;
    }

    public void setStatus(TopUpStatus status) {
        this.status = status;
    }
}