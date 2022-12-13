package com.app.exceptions;

public class InvalidConfigurationFileException extends Exception{
    public InvalidConfigurationFileException(String fileName) {
        super("File with name %s is invalid.".formatted(fileName));
    }
    public InvalidConfigurationFileException(String message, boolean fileFound) {
        super(message);
    }
}
