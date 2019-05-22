package com.cyecize.multidb.repository.utils;

public class NoEntityManagerForSessionException extends RuntimeException {

    public NoEntityManagerForSessionException(String message) {
        super(message);
    }

    public NoEntityManagerForSessionException(String message, Throwable cause) {
        super(message, cause);
    }
}
