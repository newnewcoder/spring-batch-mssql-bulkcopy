package com.github.newnewcoder.batch.jdbc;

/**
 * Copy from project Microsoft/mssql-jdbc's SQLServerBulkCSVFileRecord.ColumnMetadata, and updated some code.
 *
 * @see <a href="https://github.com/Microsoft/mssql-jdbc/blob/master/src/main/java/com/microsoft/sqlserver/jdbc/SQLServerBulkCSVFileRecord.java">mssql-jdbc</a>
 */
public final class ColumnMetadata {
    final String fieldName;
    final String columnName;
    final int columnType;
    final int precision;
    final int scale;

    ColumnMetadata(String fieldName, String columnName, int type, int precision, int scale) {
        this.fieldName = fieldName;
        this.columnName = columnName;
        this.columnType = type;
        this.precision = precision;
        this.scale = scale;
    }
}