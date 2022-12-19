package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    private int size;

    public void clear() {
        for (int i = 0; i < size; i++) {
            storage[i] = null;
        }
        size = 0;
    }

    public void update(Resume resume) {
        int index = getIndexOfResume(resume.getUuid());
        if (index == -1) {
            printMessNotExist(resume.getUuid());
            return;
        }
        storage[index] = resume;
    }

    public void save(Resume r) {
        if (getIndexOfResume(r.getUuid()) != -1) {
            System.out.println("Резюме " + r.getUuid() + " уже существует");
            return;
        }
        if (size >= storage.length) {
            System.out.println("База переполнена");
            return;
        }
        storage[size] = r;
        size++;
    }

    public Resume get(String uuid) {
        int index = getIndexOfResume(uuid);
        if (index == -1) {
            printMessNotExist(uuid);
            return null;
        }
        return storage[index];
    }

    public void delete(String uuid) {
        int index = getIndexOfResume(uuid);
        if (index == -1) {
            printMessNotExist(uuid);
        }
        storage[index] = storage[size - 1];
        storage[size - 1] = null;
        size--;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        Resume[] copyArray = new Resume[size];
        System.arraycopy(storage, 0, copyArray, 0, size);
        return copyArray;
    }

    public int size() {
        return size;
    }

    private int getIndexOfResume(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    private void printMessNotExist(String uuid) {
        System.out.println("Резюме " + uuid + " не существует");
    }
}