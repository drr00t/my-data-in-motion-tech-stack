package io.github.drr00t.filmcatalogjob.entity;

import io.github.drr00t.filmcatalogjob.boundary.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {

    @Test
    void createFilmWithValues() {
        var film = new Film("Test Title", "Test Description", 2023, 120);
        assertEquals("Test Title", film.title());
        assertEquals("Test Description", film.description());
        assertEquals(2023, film.releaseYear());
        assertEquals(120, film.duration());
    }

    @Test
    @DisplayName("Create a valid film")
    void createFilmThatIsValid() {
        var inputTitle = "Test Title";
        var inputDescr = "Test Description";
        var inputYear = 2023;
        var inputDuration = 120;

        Result<Film> result = Film.of(inputTitle, inputDescr, inputYear, inputDuration);
        assertTrue(result.isValid());
        assertEquals("Test Title", result.entity().title());
        assertEquals("Test Description", result.entity().description());
        assertEquals(2023, result.entity().releaseYear());
        assertEquals(120, result.entity().duration());
    }

    @Test
    @DisplayName("Failed to create a Film with null title")
    void failedToCreateFilmWithNullTitle() {
        var inputDescr = "Test Description";
        var inputYear = 2023;
        var inputDuration = 120;

        Result<Film> result = Film.of(null, inputDescr, inputYear, inputDuration);
        assertFalse(result.isValid());
        assertEquals("Title cannot be null", result.errorMessages().get(0));
    }
}