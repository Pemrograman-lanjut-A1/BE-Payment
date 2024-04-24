package id.ac.ui.cs.advprog.bepayment.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class WalletTest {
    private Wallet wallet;

    @BeforeEach
    void setUp() {
        wallet = Wallet.builder()
                .id("1")
                .userId("3df9d41b-33c3-42a1-b0a4-43cf0ffdc649")
                .amount(500)
                .build();
    }

    @Test
    void testCreateWallet() {
        assertNotNull(wallet);
        assertEquals("1", wallet.getId());
        assertEquals("3df9d41b-33c3-42a1-b0a4-43cf0ffdc649", wallet.getUserId());
        assertEquals(500, wallet.getAmount());
    }

    @Test
    void testSetUserId() {
        wallet.setUserId("user456");
        assertEquals("user456", wallet.getUserId());
    }

    @Test
    void testSetAmount() {
        wallet.setAmount(1000);
        assertEquals(1000, wallet.getAmount());
    }
}
