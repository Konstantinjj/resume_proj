package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.serializer.StreamSerializer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PathStorage extends AbstractStorage<Path> {
    private final Path directory;
    private final StreamSerializer ss;

    protected PathStorage(String dir, StreamSerializer ss) {
        directory = Paths.get(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        this.ss = ss;
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory or is not writable");
        }
    }

    @Override
    protected List<Resume> doGetAll() {
        try {
            return Files.list(directory).map(this::doGet).collect(Collectors.toList());
        } catch (IOException e) {
            throw new StorageException("Read Path error", e);
        }
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return ss.doRead(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Read Path error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void doDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("Delete Path Error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void doUpdate(Resume r, Path path) {
        try {
            ss.doWrite(r, new BufferedOutputStream(Files.newOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Update Path Error", r.getUuid(), e);
        }
    }

    @Override
    protected void doSave(Resume r, Path path) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new StorageException("Create error", path.getFileName().toString(), e);
        }
        doUpdate(r, path);
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.isRegularFile(path);
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve((uuid));
    }

    @Override
    public void clear() {
        try {
            Files.list(directory).forEach(this::doDelete);
        } catch (IOException e) {
            throw new StorageException("Path delete error", e);
        }
    }

    @Override
    public int size() {
        try {
            return (int) Files.list(directory).count();
        } catch (IOException e) {
            throw new StorageException("Size error", e);
        }
    }
}
