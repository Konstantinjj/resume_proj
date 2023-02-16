package com.urise.webapp.storage;

import com.urise.webapp.storage.serializer.ObjectSerializer;

import static org.junit.Assert.*;

public class PathStorageTest extends AbstractStorageTest {
    public PathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new ObjectSerializer()));
    }
}