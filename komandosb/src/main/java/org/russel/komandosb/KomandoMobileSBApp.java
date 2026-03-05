package org.russel.komandosb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class KomandoMobileSBApp {
    public static void main(String[] args) {
        SpringApplication.run(KomandoMobileSBApp.class, args);
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//
//        String[] passwords = {"password", "password", "password"};
//
//        for (String pwd : passwords) {
//            System.out.println(encoder.encode(pwd));
//        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        System.out.println("\n" +
                "====================================================================================================\n" +
                "KOMANDO MOBILE Spring Boot App running on PORT 8080. \n" +
                "====================================================================================================" + "\n");
    }
}
