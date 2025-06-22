package io.github.drr00t.filmcatalogjob.boundary;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Result<T> {
    private final boolean isValid;
    private final T entity;
    private final List<String> errorMessages;

    public Result(boolean isValid, T entity, List<String> errorMessages) {
        this.isValid = isValid;
        this.entity = entity;
        this.errorMessages = errorMessages;
    }

    public static <T> Result<T> isOk(T entity) {
        Objects.requireNonNull(entity, "entity must not be null");
        return new Result<>(true, entity, Collections.emptyList());
    }

    public static <T> Result<T> notOk(List<String> errorMessages) {
        Objects.requireNonNull(errorMessages, "errorMessages must not be null");
        return new Result<>(false, null, errorMessages);
    }

    public boolean isValid() {
        return isValid;
    }

    public T entity() {
        return entity;
    }

    public List<String> errorMessages() {
        return errorMessages;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Result) obj;
        return this.isValid == that.isValid &&
                Objects.equals(this.entity, that.entity) &&
                Objects.equals(this.errorMessages, that.errorMessages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isValid, entity, errorMessages);
    }

    @Override
    public String toString() {
        return "Result[" +
                "isValid=" + isValid + ", " +
                "entity=" + entity + ", " +
                "errorMessages=" + errorMessages + ']';
    }


}