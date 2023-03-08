package com.urise.webapp.sql;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.StorageException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    private final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void runSql(String sql) {
        runSql(sql, st -> st.execute());
    }

    public interface SqlStatement<T> {
        T runSql(PreparedStatement st) throws SQLException;
    }

    public <T> T runSql(String sql, SqlStatement<T> statement) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return statement.runSql(ps);
        } catch (SQLException e) {
            throw new ExistStorageException(null);
        }
    }
}
