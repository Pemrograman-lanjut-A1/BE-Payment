package id.ac.ui.cs.advprog.bepayment.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TopUpTest {
    private Wallet wallet;
    private TopUp topUp;

    @BeforeEach
    void setUp() {
        wallet = new Wallet(
                "1",
                "3df9d41b-33c3-42a1-b0a4-43cf0ffdc649",
                500);
        topUp = TopUp.builder()
                .id("eb558e9f-1c39-460e-8860-71af6af63bd6")
                .userId("3df9d41b-33c3-42a1-b0a4-43cf0ffdc649")
                .amount(500)
                .wallet(wallet)
                .status("PENDING")
                .build();
    }

    @Test
    void testCreateTopUp() {
        assertNotNull(topUp);
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd6", topUp.getId());
        assertEquals(wallet, topUp.getWallet());
        assertEquals(500, topUp.getAmount());
        assertEquals("PENDING", topUp.getStatus());
    }
    @Test
    void testSetStatusValid() {
        topUp.setStatus("REJECTED");
        assertEquals("REJECTED", topUp.getStatus());
    }

    @Test
    void testSetStatusInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            topUp.setStatus("INVALID_STATUS");
        });
    }

    @Test
    void testSetStatusNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            topUp.setStatus(null);
        });
    }

    @Test
    void testSetStatusWaitingApproval() {
        topUp.setStatus("WAITING_APPROVAL");
        assertEquals("WAITING_APPROVAL", topUp.getStatus());
    }

    @Test
    void testSetStatusSuccess() {
        topUp.setStatus("SUCCESS");
        assertEquals("SUCCESS", topUp.getStatus());
    }

    @Test
    void testSetStatusCanceled() {
        topUp.setStatus("CANCELED");
        assertEquals("CANCELED", topUp.getStatus());
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
            topUp.setAmount(-1000);
        });
    }


}
