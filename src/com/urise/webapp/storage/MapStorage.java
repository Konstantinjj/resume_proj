package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class MapStorage extends AbstractStorage {
    private final Map<String, Resume> map = new HashMap<>();

    @Override
    protected Resume doGet(Object key) {
        return (Resume) key;
    }

    @Override
    protected void doDelete(Object key) {
        map.remove(((Resume) key).getUuid());
    }

    @Override
    protected void doUpdate(Resume resume, Object key) {
        map.put(resume.getUuid(), resume);
    }

    @Override
    protected void doSave(Resume r, Object key) {
        map.put(r.getUuid(), r);
    }

    @Override
    protected boolean isExist(Object key) {
        return key != null;
    }

    @Override
    protected Object getSearchKey(String uuid) {
        return map.get(uuid);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Resume[] getAll() {
        return map.values().toArray(new Resume[0]);
    }

    @Override
    public int size() {
        return map.size();
    }
}
