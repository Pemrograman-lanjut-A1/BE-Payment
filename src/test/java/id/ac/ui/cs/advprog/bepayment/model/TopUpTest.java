package id.ac.ui.cs.advprog.bepayment.model;

import id.ac.ui.cs.advprog.bepayment.enums.TopUpStatus;
import id.ac.ui.cs.advprog.bepayment.model.TopUp;
import id.ac.ui.cs.advprog.bepayment.model.TopUpBuilder;
import id.ac.ui.cs.advprog.bepayment.model.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TopUpTest {
    private Wallet wallet;
    private TopUpBuilder topUpBuilder;
    private TopUp topUp;

    @BeforeEach
    void setUp() {
        wallet = Wallet.builder()
                .id("1")
                .userId("3df9d41b-33c3-42a1-b0a4-43cf0ffdc649")
                .amount(500)
                .build();
        topUpBuilder = new TopUpBuilder()
                .userId("3df9d41b-33c3-42a1-b0a4-43cf0ffdc649")
                .amount(500)
                .wallet(wallet);
        topUp = topUpBuilder.build();
    }

    @Test
    void testCreateTopUp() {
        assertNotNull(topUp);
        assertNotNull(topUp.getId());
        assertEquals(wallet, topUp.getWallet());
        assertEquals(500, topUp.getAmount());
        assertEquals(TopUpStatus.WAITING_APPROVAL, topUp.getStatus());
    }

    @Test
    void testSetStatusValid() {
        topUp.setStatus(TopUpStatus.REJECTED);
        assertEquals(TopUpStatus.REJECTED, topUp.getStatus());
    }

    @Test
    void testSetStatusInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            topUp.setStatus(TopUpStatus.valueOf("INVALID_STATUS"));
        });
    }

    @Test
    void testSetAmountValid() {
        topUp.setAmount(1000);
        assertEquals(1000, topUp.getAmount());
    }

    @Test
    void testSetAmountNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            topUp.setAmount(-1000);
        });
    }

    @Test
    void testSetAmountZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            topUp.setAmount(0);
        });
    }
}
