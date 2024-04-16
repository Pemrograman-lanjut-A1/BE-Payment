package id.ac.ui.cs.advprog.bepayment.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TopUpTest {
    private Wallet wallet;
    private TopUp topUp;

    @BeforeEach
    void setUp() {
        this.wallet = new Wallet("walletId", "3df9d41b-33c3-42a1-b0a4-43cf0ffdc649",
                10000);
        this.topUp = new TopUp("topUpId", wallet, 500, "PENDING");
    }

    @Test
    void testCreateTopUp() {
        assertNotNull(topUp);
        assertEquals("topUpId", topUp.getId());
        assertEquals(wallet, topUp.getWallet());
        assertEquals(500, topUp.getAmount());
        assertEquals("PENDING", topUp.getStatus());
    }

    @Test
    void testSetStatusToValidStatus() {
        topUp.setStatus("SUCCESS");
        assertEquals("SUCCESS", topUp.getStatus());
    }

    @Test
    void testSetStatusToInvalidStatus() {
        assertThrows(IllegalArgumentException.class, () -> topUp.setStatus("MEOW"));
    }
}
