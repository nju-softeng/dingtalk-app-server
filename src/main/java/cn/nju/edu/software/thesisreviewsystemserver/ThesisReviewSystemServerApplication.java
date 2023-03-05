package cn.nju.edu.software.thesisreviewsystemserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class ThesisReviewSystemServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThesisReviewSystemServerApplication.class, args);
    }

}
