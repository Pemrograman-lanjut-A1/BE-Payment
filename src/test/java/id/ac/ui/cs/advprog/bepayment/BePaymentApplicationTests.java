package id.ac.ui.cs.advprog.bepayment;

import id.ac.ui.cs.advprog.bepayment.controller.TopUpController;
import id.ac.ui.cs.advprog.bepayment.service.TopUpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class BePaymentApplicationTests {

    @Autowired
    TopUpController topUpController;
    @Autowired
    TopUpService topUpService;
    @Test
    void contextLoads() {
        assertNotNull(topUpController);
        assertNotNull(topUpService);
    }

}
