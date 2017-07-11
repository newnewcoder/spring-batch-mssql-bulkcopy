package com.github.newnewcoder.batch.util;


import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import com.github.newnewcoder.batch.exception.MssqlBulkCopyBatchException;
import com.microsoft.sqlserver.jdbc.ISQLServerConnection;

public class MssqlJdbcUtil {

    public static ISQLServerConnection retrieveSQLServerConnection(DataSource dataSource) throws SQLException, MssqlBulkCopyBatchException {
        final Connection conn = dataSource.getConnection();
        if (conn.isWrapperFor(ISQLServerConnection.class)) {
            return conn.unwrap(ISQLServerConnection.class);
        } else {
            throw new MssqlBulkCopyBatchException("Native connection must be implements by ISQLServerConnection.");
        }
    }
}
