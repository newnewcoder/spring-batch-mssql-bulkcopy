package com.github.newnewcoder.batch.writer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

    enum Types {
        BIGINT(java.sql.Types.BIGINT),
        INT(java.sql.Types.INTEGER),
        SMALLINT(java.sql.Types.SMALLINT),
        TINYINT(java.sql.Types.TINYINT),
        BIT(java.sql.Types.BIT),
        DECIMAL(java.sql.Types.DECIMAL),
        NUMERIC(java.sql.Types.NUMERIC),
        MONEY(microsoft.sql.Types.MONEY),
        FLOAT(java.sql.Types.FLOAT),
        REAL(java.sql.Types.REAL),
        CHAR(java.sql.Types.CHAR),
        VARCHAR(java.sql.Types.VARCHAR),
        NCHAR(java.sql.Types.NCHAR),
        NVARCHAR(java.sql.Types.NVARCHAR),
        DATETIME(java.sql.Types.TIMESTAMP),
        DATE(java.sql.Types.DATE),
        TIME(java.sql.Types.TIME),
        BINARY(java.sql.Types.BINARY),
        VARBINARY(java.sql.Types.VARBINARY);

        public final int sqlType;

        Types(int type) {
            this.sqlType = type;
        }

        public int getValue() {
            return sqlType;
        }
    }

    String name();

    Types type() default Types.VARCHAR;

    int precision() default 0;

    int scale() default 0;
}
