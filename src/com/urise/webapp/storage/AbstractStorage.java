package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

import java.security.Key;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage<KEY> implements Storage {

    private static final Comparator<Resume> RESUME_COMPARATOR = Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);

    public void update(Resume resume) {
        KEY searchKey = (KEY) getExistedSearchKey(resume.getUuid());
        doUpdate(resume, searchKey);
    }

    public void save(Resume r) {
        KEY searchKey = (KEY) getNotExistedSearchKey(r.getUuid());
        doSave(r, searchKey);
    }

    public Resume get(String uuid) {
        KEY searchKey = (KEY) getExistedSearchKey(uuid);
        return doGet(searchKey);
    }

    public void delete(String uuid) {
        KEY searchKey = (KEY) getExistedSearchKey(uuid);
        doDelete(searchKey);
    }

    private Object getExistedSearchKey(String uuid) {
        KEY searchKey = (KEY) getSearchKey(uuid);
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private Object getNotExistedSearchKey(String uuid) {
        KEY searchKey = (KEY) getSearchKey(uuid);
        if (isExist(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> list = doGetAll();
        list.sort(RESUME_COMPARATOR);
        return list;
    }

    protected abstract List<Resume> doGetAll();

    protected abstract Resume doGet(KEY key);

    protected abstract void doDelete(KEY key);

    protected abstract void doUpdate(Resume resume, KEY key);

    protected abstract void doSave(Resume r, KEY key);

    protected abstract boolean isExist(KEY key);

    protected abstract Object getSearchKey(String uuid);

}
