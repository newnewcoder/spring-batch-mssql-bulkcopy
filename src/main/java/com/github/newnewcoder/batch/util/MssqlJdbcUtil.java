package com.github.newnewcoder.batch.util;


import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import com.microsoft.sqlserver.jdbc.ISQLServerConnection;

public class MssqlJdbcUtil {

    public static ISQLServerConnection retrieveSQLServerConnection(DataSource dataSource) throws SQLException, IllegalArgumentException {
        final Connection conn = dataSource.getConnection();
        if (conn.isWrapperFor(ISQLServerConnection.class)) {
            return conn.unwrap(ISQLServerConnection.class);
        } else {
            throw new IllegalArgumentException("Native connection must be implements by ISQLServerConnection.");
        }
    }
}
