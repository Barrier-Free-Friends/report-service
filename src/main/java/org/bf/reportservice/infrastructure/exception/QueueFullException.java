package org.bf.reportservice.infrastructure.exception;

public class QueueFullException extends RuntimeException {
    public QueueFullException(String message) {
        super(message);
    }
}
