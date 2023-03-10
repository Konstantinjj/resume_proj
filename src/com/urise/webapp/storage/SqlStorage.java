package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.ConnectionFactory;
import com.urise.webapp.sql.SqlHelper;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public void update(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid =?")) {
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getUuid());
                executeUpdate(ps, r.getUuid());
            }
            sqlHelper.runSql("DELETE FROM contact WHERE resume_uuid=?", st -> {
                st.setString(1, r.getUuid());
                st.execute();
                return null;
            });
            insertContact(conn, r);
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                        ps.setString(1, r.getUuid());
                        ps.setString(2, r.getFullName());
                        ps.execute();
                    }
                    insertContact(conn, r);
                    return null;
                }
        );
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.runSql("SELECT * FROM resume r LEFT JOIN contact c" +
                " ON r.uuid = c.resume_uuid WHERE r.uuid =?\n", st -> {
            st.setString(1, uuid);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume r = new Resume(uuid, rs.getString("full_name"));
            addContacts(rs, r);
            return r;
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
                Resume resume = new Resume(rs.getString("uuid"), rs.getString("full_name"));
                resumes.add(resume);
                sqlHelper.runSql("SELECT * FROM contact WHERE resume_uuid=?", st1 -> {
                    st1.setString(1, resume.getUuid());
                    ResultSet rs1 = st1.executeQuery();
                    while (rs1.next()) {
                        addContacts(rs1, resume);
                    }
                    return null;
                });
            }
            return resumes;
        });
    }

    private void addContacts(ResultSet rs, Resume resume) throws SQLException {
        do {
            String value = rs.getString("value");
            if (value != null) {
                resume.addContact(ContactType.valueOf(rs.getString("type")), value);
            }
        } while (rs.next());
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

    private void insertContact(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
