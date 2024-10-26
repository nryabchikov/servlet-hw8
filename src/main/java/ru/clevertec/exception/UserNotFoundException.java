package ru.clevertec.exception;

public class UserNotFoundException extends RuntimeException {
    private UserNotFoundException(String message) {
        super(message);
    }

    public static UserNotFoundException byId(int id) {
        return new UserNotFoundException(String.format("User with id: %s not found.", id));
    }
}
