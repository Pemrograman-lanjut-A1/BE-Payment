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
}
