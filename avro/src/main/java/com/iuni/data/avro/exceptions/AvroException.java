package com.iuni.data.avro.exceptions;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public class AvroException extends Exception {

    public AvroException(Throwable cause) {
        super(cause);
    }

    public AvroException(String message) {
        super(message);
    }

    public AvroException(String message, Throwable cause) {
        super(message, cause);
    }
}
