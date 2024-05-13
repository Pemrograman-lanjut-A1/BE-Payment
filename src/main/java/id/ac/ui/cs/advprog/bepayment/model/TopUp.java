package id.ac.ui.cs.advprog.bepayment.model;

import id.ac.ui.cs.advprog.bepayment.enums.TopUpMethod;
import id.ac.ui.cs.advprog.bepayment.enums.TopUpStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "topup")
public class TopUp {
    @Id
    private String id;
    private String userId;
    @ManyToOne
    @JoinColumn(name = "walletId")
    private Wallet wallet;
    private double amount;
    private TopUpStatus status;
    private Date dateAdded;
    private TopUpMethod topUpMethod;

    public TopUp(String id, String userId, Wallet wallet, double amount, TopUpStatus status, TopUpMethod method, Date dateAdded) {
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
        this.dateAdded = dateAdded;
        this.topUpMethod = method;
    }
    public void setAmount(double amount) {
        if(amount <= 0){
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        this.amount = amount;
    }
}