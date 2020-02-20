package com.rsjava.CarAverage.exception;

public class InvalidUrlAddressException extends RuntimeException {

    public InvalidUrlAddressException(String message) {
        super("Invalid URL" + "\n" + "please try again");
    }
}
