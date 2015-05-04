package com.fet.carpool.serv;

public class Exception extends RuntimeException {

    private static final long serialVersionUID = -3785561143123198830L;

    public Exception(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public Exception(String message) {
        super(message);
    }

    public Exception(Throwable cause) {
        super(cause);
    }
}
