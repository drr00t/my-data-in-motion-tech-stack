package io.github.drr00t.filmcatalogjob.boundary;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    @Test
    @DisplayName("Result is valid when created with isOk")
    void resultEntityIsOk() {
        var input = "Test Entity";
        var result = Result.isOk(input);
        assertTrue(input.equals(result.entity()));
    }

    @Test
    @DisplayName("Result is not valid when created with notOk")
    void resultEntityNotOk() {
        var errorMessages = List.of("Error 1", "Error 2");
        var result = Result.notOk(errorMessages);
        assertNull(result.entity());
        assertFalse(result.isValid());
        assertEquals(errorMessages, result.errorMessages());
        assertEquals(errorMessages.get(0), result.errorMessages().get(0));
    }

    @Test
    @DisplayName("Result is valid when created with isOk and has no error messages")
    void isValid() {
        var input = "Test Entity";
        var result = Result.isOk(input);
        assertTrue(result.isValid());
        assertEquals(0, result.errorMessages().size());
    }

    @Test
    @DisplayName("Result entity returns the correct entity")
    void entity() {
        var input = "Test Entity";
        var result = Result.isOk(input);
        assertEquals(input, result.entity());
        assertTrue(result.isValid());
        assertEquals(0, result.errorMessages().size());
    }

    @Test
    void errorMessages() {
        var errorMessages = List.of("Error 1", "Error 2");
        var result = Result.notOk(errorMessages);
        assertEquals(errorMessages, result.errorMessages());
        assertFalse(result.isValid());
        assertEquals(errorMessages.get(0), result.errorMessages().get(0));
        assertEquals(errorMessages.get(1), result.errorMessages().get(1));
    }
}