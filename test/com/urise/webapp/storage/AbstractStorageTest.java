package com.urise.webapp.storage;

import com.urise.webapp.ResumeTestData;
import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public abstract class AbstractStorageTest {
    protected final Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final Resume RESUME_1 = ResumeTestData.createResume(UUID_1, "Name1");
    private static final String UUID_2 = "uuid2";
    private static final Resume RESUME_2 = ResumeTestData.createResume(UUID_2, "Name2");
    private static final String UUID_3 = "uuid3";
    private static final Resume RESUME_3 = ResumeTestData.createResume(UUID_3, "Name3");
    private static final String UUID_4 = "uuid4";
    private static final Resume RESUME_4 = ResumeTestData.createResume(UUID_4, "Name4");

    private static final String UUID_NOT_EXIST = "dummy";

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
        assertArrayEquals(new Resume[0], storage.getAllSorted().toArray());
    }

    @Test
    public void update() {
        Resume resumeNew = new Resume(UUID_3, "ChangedName");
        storage.update(resumeNew);
        assertSame(resumeNew, storage.get(UUID_3));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(RESUME_4);
    }

    @Test
    public void save() {
        storage.save(RESUME_4);
        assertSize(4);
        assertGet(RESUME_4);
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(RESUME_3);
    }

    @Test
    public void get() {
        assertGet(RESUME_1);
        assertGet(RESUME_2);
        assertGet(RESUME_3);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(UUID_NOT_EXIST);
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        storage.delete(UUID_1);
        assertSize(2);
        storage.get(UUID_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(UUID_4);
    }

    @Test
    public void getAll() {
        List<Resume> array = storage.getAllSorted();
        assertEquals(3, array.size());
        assertEquals(storage.get("uuid1"), array.get(0));
        assertEquals(storage.get("uuid2"), array.get(1));
        assertEquals(storage.get("uuid3"), array.get(2));

        Resume expectedResumeOne = new Resume(UUID_1, "Name1");
        Resume expectedResumeTwo = new Resume(UUID_2, "Name2");
        Resume expectedResumeThree = new Resume(UUID_3, "Name3");
        Resume[] expected = new Resume[3];
        expected[0] = expectedResumeOne;
        expected[1] = expectedResumeTwo;
        expected[2] = expectedResumeThree;

        assertArrayEquals(expected, storage.getAllSorted().toArray());
    }

    @Test
    public void size() {
        assertSize(3);
    }

    private void assertSize(int size) {
        assertEquals(size, storage.size());
    }

    private void assertGet(Resume r) {
        assertEquals(storage.get(r.getUuid()), r);
    }
}
