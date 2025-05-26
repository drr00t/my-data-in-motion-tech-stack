package io.github.drr00t.filmcatalogjob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 */

@SpringBootApplication
public class FilmIntegrationApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(FilmIntegrationApplication.class);
        application.run(args);

    }
}
