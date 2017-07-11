package com.github.newnewcoder.batch.tasklet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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
import com.github.newnewcoder.batch.exception.MssqlBulkCopyBatchException;
import com.github.newnewcoder.batch.util.MssqlJdbcUtil;
import com.microsoft.sqlserver.jdbc.SQLServerBulkCopy;
import com.microsoft.sqlserver.jdbc.SQLServerBulkCopyOptions;

public class MssqlSqlBulkCopyTasklet implements Tasklet, InitializingBean {

    private DataSource sourceDataSource;

    private DataSource targetDataSource;

    private String sql;

    private Map<String, String> columnMapping;

    private String destinationTableName;

    private SQLServerBulkCopyOptions copyOptions;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(sourceDataSource, "SourceDataSource must be set.");
        Assert.notNull(targetDataSource, "TargetDataSource must be set.");
        Assert.isTrue(StringUtils.isNotEmpty(destinationTableName), "DestinationTableName must be set.");
        Assert.isTrue(StringUtils.isNotEmpty(sql), "Sql must be set.");
        if (copyOptions == null) {
            copyOptions = new SQLServerBulkCopyOptions();
            copyOptions.setTableLock(true);
        }
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try (Connection srcConn = sourceDataSource.getConnection();
             Statement sourceStatement = srcConn.createStatement();
             ResultSet rsSourceData = sourceStatement.executeQuery(sql);
             Connection targetConn = MssqlJdbcUtil.retrieveSQLServerConnection(targetDataSource);
             SQLServerBulkCopy bulkCopy = new SQLServerBulkCopy(targetConn)) {
            bulkCopy.setDestinationTableName(destinationTableName);
            if (!CollectionUtils.isEmpty(columnMapping)) {
                for (Map.Entry<String, String> entry : columnMapping.entrySet()) {
                    bulkCopy.addColumnMapping(entry.getKey(), entry.getValue());
                }
            }
            bulkCopy.writeToServer(rsSourceData);
        }
        return RepeatStatus.FINISHED;
    }

    public void setTargetDataSource(DataSource dataSource) throws Exception {
        this.targetDataSource = dataSource;
    }

    public void setDestinationTableName(String destinationTableName) {
        this.destinationTableName = destinationTableName;
    }

    public void setCopyOptions(SQLServerBulkCopyOptions copyOptions) {
        this.copyOptions = copyOptions;
    }

    public void setSourceDataSource(DataSource datasource) {
        this.sourceDataSource = datasource;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setColumnMapping(Map<String, String> columnMapping) {
        this.columnMapping = columnMapping;
    }
}
