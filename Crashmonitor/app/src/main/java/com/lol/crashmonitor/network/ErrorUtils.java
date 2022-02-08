package com.lol.crashmonitor.network;

public class ErrorUtils {

    public static class ApiErrorException extends Throwable {

        public ApiErrorException(String message) {
            super(message);
        }
    }
}