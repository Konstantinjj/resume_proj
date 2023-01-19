package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {
    public void update(Resume resume) {
        Object key = getCheckedKey(resume.getUuid(), true);
        doUpdate(resume, key);
    }

    public void save(Resume r) {
        Object key = getCheckedKey(r.getUuid(), false);
        doSave(r, key);
    }

    public Resume get(String uuid) {
        Object key = getCheckedKey(uuid, true);
        return doGet(key);
    }

    public void delete(String uuid) {
        Object key = getCheckedKey(uuid, true);
        doDelete(key);
    }

    protected abstract Resume doGet(Object key);

    protected abstract void doDelete(Object key);

    protected abstract void doUpdate(Resume resume, Object key);

    protected abstract void doSave(Resume r, Object key);

    protected abstract boolean isExist(Object key);

    protected abstract Object getKey(String uuid);

    private Object getCheckedKey(String uuid, boolean isExist) {
        Object key = getKey(uuid);
        if (!isExist(key) && isExist) {
            throw new NotExistStorageException(uuid);
        } else if (isExist(key) && !isExist) {
            throw new ExistStorageException(uuid);
        }
        return key;
    }

}
