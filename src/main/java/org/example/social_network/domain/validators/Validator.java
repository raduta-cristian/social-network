package org.example.social_network.domain.validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}