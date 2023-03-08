package com.urise.webapp.storage;

import com.urise.webapp.Config;
import junit.framework.TestCase;

public class SqlStorageTest extends AbstractStorageTest {
    public SqlStorageTest() {
        super(Config.get().getSqlStorage());
    }

}