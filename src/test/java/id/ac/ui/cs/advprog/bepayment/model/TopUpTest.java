package id.ac.ui.cs.advprog.bepayment.model;

import id.ac.ui.cs.advprog.bepayment.enums.TopUpMethod;
import id.ac.ui.cs.advprog.bepayment.enums.TopUpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

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
                .topUpMethod(TopUpMethod.E_WALLET)
                .wallet(wallet);
        topUp = topUpBuilder.build();
    }

    @Test
    void testCreateTopUp() {
        assertNotNull(topUp);
        assertEquals(wallet, topUp.getWallet());
        assertEquals(500, topUp.getAmount());
        assertEquals(TopUpStatus.WAITING_APPROVAL, topUp.getStatus());
        assertEquals(TopUpMethod.E_WALLET, topUp.getTopUpMethod());
    }

    @Test
    void testSetStatusValid() {
        topUp.setStatus(TopUpStatus.REJECTED);
        assertEquals(TopUpStatus.REJECTED, topUp.getStatus());
    }

    @Test
    void testSetStatusInvalid() {
        assertThrows(IllegalArgumentException.class, () -> TopUpStatus.valueOf("INVALID_STATUS"));
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
    @Test
    void testSetTopUpMethod() {
        TopUpMethod method = TopUpMethod.KARTU_KREDIT;
        topUp.setTopUpMethod(method);
        assertEquals(method, topUp.getTopUpMethod());
    }

    @Test
    void testSetDateAdded() {
        Date date = new Date();
        topUp.setDateAdded(date);
        assertEquals(date, topUp.getDateAdded());
    }
}
