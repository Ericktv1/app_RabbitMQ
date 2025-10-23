package co.vinni;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "co.vinni")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
