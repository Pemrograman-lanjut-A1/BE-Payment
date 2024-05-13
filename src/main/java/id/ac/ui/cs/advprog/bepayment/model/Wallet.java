package id.ac.ui.cs.advprog.bepayment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
@Entity(name = "wallet")
public class Wallet {
    @Id
    private String id;
    private String userId;
    private double amount;

    public Wallet(String id, String userId, double amount) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
    }
}
