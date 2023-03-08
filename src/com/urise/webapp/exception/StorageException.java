package com.urise.webapp.exception;

import java.io.IOException;
import java.sql.SQLException;

public class StorageException extends RuntimeException {
    private String uuid;

    public StorageException(String message, String uuid) {
        super(message);
        this.uuid = uuid;
    }

    public StorageException(String message, String uuid, Exception e) {
        super(message, e);
        this.uuid = uuid;
    }

    public StorageException(String message, Exception e) {
        super(message, e);
        this.uuid = null;
    }

    public StorageException(String message) {
        super(message);
        this.uuid = null;
    }

    public StorageException(Exception e) {
        this(e.getMessage(), e);
    }

    public String getUuid() {
        return uuid;
    }
}
