package io.github.drr00t.filmcatalogjob.entity;

import io.github.drr00t.filmcatalogjob.boundary.Result;

import java.util.List;
import java.util.Objects;

public class Film {
    private String title;
    private String description;
    private int releaseYear;
    private int length;


    public Film(String title, String description, int releaseYear, int length) {
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.length = length;
    }

    public static Result<Film> of(String title, String descr, int releaseYear, int duration) {

        if (title == null) {
            return Result.notOk(List.of("Title cannot be null"));
        }
        Objects.requireNonNull(descr);

        return Result.isOk(new Film(title, descr, releaseYear, duration));
    }

    public String title() {
        return title;
    }

    public String description() {
        return description;
    }

    public int releaseYear() {
        return releaseYear;
    }

    public int duration() {
        return length;
    }
}
