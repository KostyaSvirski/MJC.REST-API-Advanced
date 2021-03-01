package com.epam.esm.exception;

public class DBCPDataSourceException extends Exception {

    public DBCPDataSourceException() {
        super();
    }

    public DBCPDataSourceException(String message) {
        super(message);
    }

    public DBCPDataSourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBCPDataSourceException(Throwable cause) {
        super(cause);
    }

    protected DBCPDataSourceException
            (String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
