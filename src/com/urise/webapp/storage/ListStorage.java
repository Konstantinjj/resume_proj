package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {
    private final List<Resume> list = new ArrayList<>();

    @Override
    protected Resume doGet(Integer key) {
        return list.get(key);
    }

    @Override
    protected void doDelete(Integer key) {
//        list.remove(key);
        list.remove((int) key);
    }

    @Override
    protected void doUpdate(Resume resume, Integer key) {
        list.set(key, resume);
    }

    @Override
    protected void doSave(Resume r, Integer key) {
        list.add(r);
    }

    @Override
    protected boolean isExist(Integer key) {
        return key != -1;
    }

    @Override
    protected Object getSearchKey(String uuid) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public List<Resume> doGetAll() {
        return new ArrayList<>(list);
    }

    @Override
    public int size() {
        return list.size();
    }
}
