package id.ac.ui.cs.advprog.bepayment.model;

import id.ac.ui.cs.advprog.bepayment.enums.TopUpStatus;
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
                .status(TopUpStatus.WAITING_APPROVAL.getValue())
                .build();
    }

    @Test
    void testCreateTopUp() {
        assertNotNull(topUp);
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd6", topUp.getId());
        assertEquals(wallet, topUp.getWallet());
        assertEquals(500, topUp.getAmount());
        assertEquals(TopUpStatus.WAITING_APPROVAL.getValue(), topUp.getStatus());
    }
    @Test
    void testSetStatusValid() {
        topUp.setStatus(TopUpStatus.REJECTED.getValue());
        assertEquals(TopUpStatus.REJECTED.getValue(), topUp.getStatus());
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
        topUp.setStatus(TopUpStatus.WAITING_APPROVAL.getValue());
        assertEquals(TopUpStatus.WAITING_APPROVAL.getValue(), topUp.getStatus());
    }

    @Test
    void testSetStatusSuccess() {
        topUp.setStatus(TopUpStatus.SUCCESS.getValue());
        assertEquals(TopUpStatus.SUCCESS.getValue(), topUp.getStatus());
    }

    @Test
    void testSetStatusCanceled() {
        topUp.setStatus(TopUpStatus.CANCELLED.getValue());
        assertEquals(TopUpStatus.CANCELLED.getValue(), topUp.getStatus());
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
