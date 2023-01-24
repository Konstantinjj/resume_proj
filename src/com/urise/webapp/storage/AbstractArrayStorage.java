package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10000;

    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    protected void doUpdate(Resume resume, Object key) {
        storage[(int) key] = resume;
    }

    @Override
    protected void doSave(Resume r, Object key) {
        if (size >= STORAGE_LIMIT) {
            throw new StorageException("База переполнена", r.getUuid());
        }
        insertResume(r, (int) key);
        size++;
    }


    @Override
    protected Resume doGet(Object key) {
        return storage[(int) key];
    }

    @Override
    protected void doDelete(Object key) {
        fillAfterDelete((int) key);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected boolean isExist(Object key) {
        return (int) key >= 0;
    }

    public List<Resume> doGetAll() {
        return Arrays.asList(Arrays.copyOf(storage, size));
    }

    public int size() {
        return size;
    }

    protected abstract Integer getSearchKey(String uuid);

    protected abstract void insertResume(Resume r, int index);

    protected abstract void fillAfterDelete(int index);


}
