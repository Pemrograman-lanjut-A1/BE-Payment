package id.ac.ui.cs.advprog.bepayment.model;

import id.ac.ui.cs.advprog.bepayment.enums.TopUpMethod;
import id.ac.ui.cs.advprog.bepayment.enums.TopUpStatus;

import java.util.Date;

public class TopUpBuilder {
    private String id;
    private String userId;
    private Wallet wallet;
    private double amount;
    private TopUpStatus status;
    private TopUpMethod topUpMethod;
    private Date dateAdded;

    public TopUpBuilder id(String id) {
        this.id = id;
        return this;
    }

    public TopUpBuilder userId(String userId) {
        this.userId = userId;
        return this;
    }

    public TopUpBuilder wallet(Wallet wallet) {
        this.wallet = wallet;
        return this;
    }

    public TopUpBuilder amount(double amount) {
        if(amount <= 0){
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        this.amount = amount;
        return this;
    }

    public TopUpBuilder status(TopUpStatus status) {
        this.status = status;
        return this;
    }
    public TopUpBuilder topUpMethod(TopUpMethod topUpMethod) {
        this.topUpMethod = topUpMethod;
        return this;
    }
    public TopUpBuilder dateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
        return this;
    }

    public TopUp build() {
        if (userId == null || wallet == null) {
            throw new IllegalArgumentException("UserId and Wallet cannot be null");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        if (status == null){
            status = TopUpStatus.WAITING_APPROVAL;
        }
        return new TopUp(id, userId, wallet, amount, status, topUpMethod, dateAdded);
    }
}