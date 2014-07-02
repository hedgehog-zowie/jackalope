package com.iuni.data.avro.exceptions;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public class AvroServerException extends AvroException {

    public AvroServerException(Throwable cause) {
        super(cause);
    }

    public AvroServerException(String message) {
        super(message);
    }

    public AvroServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
