package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.ConnectionFactory;
import com.urise.webapp.sql.SqlHelper;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    public final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.runSql("DELETE FROM resume");
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.runSql("UPDATE resume SET full_name = ? WHERE uuid =?", st -> {
            st.setString(1, resume.getFullName());
            st.setString(2, resume.getUuid());
            executeUpdate(st, resume.getUuid());
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.runSql("INSERT INTO resume (uuid, full_name) VALUES (?, ?)", st -> {
            st.setString(1, r.getUuid());
            st.setString(2, r.getFullName());
            st.execute();
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.runSql("SELECT * FROM resume r WHERE r.uuid =?", st -> {
            st.setString(1, uuid);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, rs.getString("full_name"));
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.runSql("DELETE FROM resume WHERE uuid =?", st -> {
            st.setString(1, uuid);
            executeUpdate(st, uuid);
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.runSql("SELECT * FROM resume r ORDER BY full_name, uuid", st -> {
            ResultSet rs = st.executeQuery();
            List<Resume> resumes = new ArrayList<>();
            while (rs.next()) {
                resumes.add(new Resume(rs.getString("uuid"), rs.getString("full_name")));
            }
            return resumes;
        });
    }

    @Override
    public int size() {
        return sqlHelper.runSql("SELECT count(*) FROM resume", st -> {
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        });
    }

    private void executeUpdate(PreparedStatement st, String uuid) throws SQLException {
            if (st.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
    }
}
