package cc.bucaedie.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({ "classpath:spring/spring-usecase-event-subscriber.xml" })
public class BucaedieSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(BucaedieSampleApplication.class, args);
    }

}
