package io.github.drr00t.filmcatalogjob.boundary.endpoint;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/api/film")
public class FilmController {
    @RequestMapping()
    String getAll() {
        return "Hello World!";
    }
}
