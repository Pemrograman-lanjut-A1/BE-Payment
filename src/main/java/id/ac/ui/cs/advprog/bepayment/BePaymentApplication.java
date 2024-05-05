package id.ac.ui.cs.advprog.bepayment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BePaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(BePaymentApplication.class, args);
    }
}
