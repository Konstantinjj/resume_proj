package com.urise.webapp.storage;

import java.io.File;

import static org.junit.Assert.*;

public class ObjectStreamPathStorageTest extends AbstractStorageTest {
    public ObjectStreamPathStorageTest() {
        super(new ObjectStreamPathStorage(STORAGE_DIR.getAbsolutePath()));
    }
}