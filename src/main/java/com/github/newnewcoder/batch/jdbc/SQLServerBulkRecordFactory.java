package com.github.newnewcoder.batch.jdbc;

import java.util.List;
import com.github.newnewcoder.batch.writer.ItemEntity;
import com.microsoft.sqlserver.jdbc.ISQLServerBulkRecord;

/**
 * A simple factory interface for creating ISQLServerBulkRecord.
 */
public interface SQLServerBulkRecordFactory {

    ISQLServerBulkRecord create(List<? extends ItemEntity> entity);
}
