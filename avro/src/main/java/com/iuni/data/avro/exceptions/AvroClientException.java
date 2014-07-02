package com.iuni.data.avro.exceptions;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public class AvroClientException extends AvroException{

    public AvroClientException(Throwable cause) {
        super(cause);
    }

    public AvroClientException(String message) {
        super(message);
    }

    public AvroClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
