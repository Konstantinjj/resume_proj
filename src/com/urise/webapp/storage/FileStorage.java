package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.serializer.StreamSerializer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {
    private final File directory;

    private final StreamSerializer ss;

    protected FileStorage(File directory, StreamSerializer ss) {
        Objects.requireNonNull(directory, "directory must not be null");
        this.ss = ss;
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
    }

    @Override
    protected List<Resume> doGetAll() {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("Directory error");
        }

        List<Resume> resumes = new ArrayList<>();
        for (File file : files) {
            resumes.add(doGet(file));
        }
        return resumes;
    }

    @Override
    protected Resume doGet(File file) {
        try {
            return ss.doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("Read file error", file.getName(), e);
        }
    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("Delete file error", file.getName());
        }
    }

    @Override
    protected void doUpdate(Resume r, File file) {
        try {
            ss.doWrite(r, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("Update file Error", r.getUuid(), e);
        }
    }

    @Override
    protected void doSave(Resume r, File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
        doUpdate(r, file);
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    public void clear() {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("Directory error");
        }
        for (File file : files) {
            doDelete(file);
        }
    }

    @Override
    public int size() {
        String[] list = directory.list();
        if (list == null) {
            throw new StorageException("Directory error");
        }
        return list.length;
    }
}
