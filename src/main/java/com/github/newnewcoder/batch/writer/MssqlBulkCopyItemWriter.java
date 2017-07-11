package com.github.newnewcoder.batch.writer;

import java.sql.Connection;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import com.github.newnewcoder.batch.exception.MssqlBulkCopyBatchException;
import com.github.newnewcoder.batch.jdbc.DefaultSQLServerListBulkRecordFactory;
import com.github.newnewcoder.batch.jdbc.SQLServerBulkRecordFactory;
import com.github.newnewcoder.batch.util.MssqlJdbcUtil;
import com.microsoft.sqlserver.jdbc.ISQLServerBulkRecord;
import com.microsoft.sqlserver.jdbc.SQLServerBulkCopy;
import com.microsoft.sqlserver.jdbc.SQLServerBulkCopyOptions;

public class MssqlBulkCopyItemWriter implements ItemWriter<ItemEntity>, InitializingBean {

    private DataSource dataSource;

    private SQLServerBulkRecordFactory recordFactory;

    private String destinationTableName;

    private SQLServerBulkCopyOptions copyOptions;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(dataSource, "DataSource must be set.");
        Assert.isTrue(StringUtils.isNotEmpty(destinationTableName), "DestinationTableName must be set.");
        if (copyOptions == null) {
            copyOptions = new SQLServerBulkCopyOptions();
            copyOptions.setTableLock(true);
        }

        if (recordFactory == null) {
            recordFactory = new DefaultSQLServerListBulkRecordFactory();
        }
    }

    @Override
    public void write(List<? extends ItemEntity> items) throws Exception {
        final ISQLServerBulkRecord record = recordFactory.create(items);
        try (Connection connection = MssqlJdbcUtil.retrieveSQLServerConnection(dataSource);
             SQLServerBulkCopy bulkCopy = new SQLServerBulkCopy(connection)) {
            bulkCopy.setBulkCopyOptions(copyOptions);
            bulkCopy.setDestinationTableName(destinationTableName);
            for (int idx : record.getColumnOrdinals()) {
                final String columnName = record.getColumnName(idx);
                bulkCopy.addColumnMapping(columnName, columnName);
            }
            //connection.setAutoCommit(false);
            try {
                bulkCopy.writeToServer(record);
                //connection.commit();
            } catch (Exception e) {
                //connection.rollback();
                throw new MssqlBulkCopyBatchException("Error occurred while bulk copy.", e);
            }
        }
    }

    public void setDataSource(DataSource dataSource) throws Exception {
        this.dataSource = dataSource;
    }

    public void setDestinationTableName(String destinationTableName) {
        this.destinationTableName = destinationTableName;
    }
}
