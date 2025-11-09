package org.example.bereal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BerealApplication {

    public static void main(String[] args) {
        SpringApplication.run(BerealApplication.class, args);
    }

}
