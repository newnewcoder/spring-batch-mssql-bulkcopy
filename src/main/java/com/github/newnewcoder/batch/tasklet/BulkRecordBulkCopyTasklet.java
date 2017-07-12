package com.github.newnewcoder.batch.tasklet;

import java.sql.Connection;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import com.github.newnewcoder.batch.util.MssqlJdbcUtil;
import com.microsoft.sqlserver.jdbc.ISQLServerBulkRecord;
import com.microsoft.sqlserver.jdbc.SQLServerBulkCopy;
import com.microsoft.sqlserver.jdbc.SQLServerBulkCopyOptions;

public class BulkRecordBulkCopyTasklet implements Tasklet, InitializingBean {

    private DataSource dataSource;

    private ISQLServerBulkRecord record;

    private Map<String, String> columnMapping;

    private String destinationTableName;

    private SQLServerBulkCopyOptions copyOptions;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(dataSource, "DataSource must be set.");
        Assert.notNull(record, "Record must be set.");
        Assert.isTrue(StringUtils.isNotEmpty(destinationTableName), "DestinationTableName must be set.");
        if (copyOptions == null) {
            copyOptions = new SQLServerBulkCopyOptions();
            copyOptions.setTableLock(true);
        }
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try (Connection connection = MssqlJdbcUtil.retrieveSQLServerConnection(dataSource);
             SQLServerBulkCopy bulkCopy = new SQLServerBulkCopy(connection)) {
            bulkCopy.setBulkCopyOptions(copyOptions);
            bulkCopy.setDestinationTableName(destinationTableName);
            if (!CollectionUtils.isEmpty(columnMapping)) {
                for (Map.Entry<String, String> entry : columnMapping.entrySet()) {
                    bulkCopy.addColumnMapping(entry.getKey(), entry.getValue());
                }
            }
            bulkCopy.writeToServer(record);
        }
        return RepeatStatus.FINISHED;
    }

    public void setDataSource(DataSource dataSource) throws Exception {
        this.dataSource = dataSource;
    }

    public void setRecord(ISQLServerBulkRecord record) {
        this.record = record;
    }

    public void setDestinationTableName(String destinationTableName) {
        this.destinationTableName = destinationTableName;
    }

    public void setCopyOptions(SQLServerBulkCopyOptions copyOptions) {
        this.copyOptions = copyOptions;
    }

    public void setColumnMapping(Map<String, String> columnMapping) {
        this.columnMapping = columnMapping;
    }
}
