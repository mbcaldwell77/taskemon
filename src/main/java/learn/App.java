package learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
//        var encoder = new BCryptPasswordEncoder();
//        System.out.println(encoder.encode("bobspassword1!"));
//        System.out.println(encoder.encode("admin"));
    }
}