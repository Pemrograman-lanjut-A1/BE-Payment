package id.ac.ui.cs.advprog.bepayment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
public class Wallet {
    @Id
    private String id;
    private String userId;
    private Integer amount;

    public Wallet(String id, String userId, Integer amount) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
    }
}
