package com.github.newnewcoder.batch;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.jdbc.core.JdbcTemplate;
import com.github.newnewcoder.batch.tasklet.DefaultMssqlBulkCopyTasklet;
import com.github.newnewcoder.batch.tasklet.MssqlSqlBulkCopyTasklet;
import com.github.newnewcoder.batch.writer.MssqlBulkCopyItemWriter;
import com.microsoft.sqlserver.jdbc.SQLServerBulkCSVFileRecord;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BulkCopyTest extends AbstractTest {

    //@Ignore
    @Test
    public void MssqlFileBulkCopyTaskletTest() throws Exception {
        DataSource dataSource = getDataSource();
        Integer countBeforeExecute = count(new JdbcTemplate(dataSource), "TEST_TARGET_TABLE");
        logger.debug("countBeforeExecute: {}", countBeforeExecute);
        DefaultMssqlBulkCopyTasklet tasklet = new DefaultMssqlBulkCopyTasklet();
        tasklet.setDataSource(dataSource);
        tasklet.setDestinationTableName("TEST_TARGET_TABLE");
        SQLServerBulkCSVFileRecord record = new SQLServerBulkCSVFileRecord("src/test/resources/test_data.D", "utf8", "\\|", false);
        record.addColumnMetadata(1, "DATE_COL", java.sql.Types.TIMESTAMP, 0, 0);
        record.addColumnMetadata(2, "NVARCHAR_COL", java.sql.Types.NVARCHAR, 20, 0);
        record.addColumnMetadata(3, "INTEGER_COL", java.sql.Types.INTEGER, 0, 0);
        record.addColumnMetadata(4, "VARCHAR_COL", java.sql.Types.VARCHAR, 20, 0);
        record.addColumnMetadata(5, "DECIMAL_COL", java.sql.Types.DECIMAL, 5, 3);
        tasklet.setRecord(record);
        tasklet.afterPropertiesSet();
        tasklet.execute(null, null);
        Integer countAfterExecute = count(new JdbcTemplate(dataSource), "TEST_TARGET_TABLE");
        logger.debug("countAfterExecute: {}", countAfterExecute);
        Assert.assertTrue(countAfterExecute == 3);
    }

    //@Ignore
    @Test
    public void MssqlSqlBulkCopyTaskletTest() throws Exception {
        DataSource dataSource = getDataSource();
        Integer countBeforeExecute = count(new JdbcTemplate(dataSource), "TEST_TARGET_TABLE");
        logger.debug("countBeforeExecute: {}", countBeforeExecute);
        MssqlSqlBulkCopyTasklet tasklet = new MssqlSqlBulkCopyTasklet();
        tasklet.setSourceDataSource(dataSource);
        tasklet.setSql("SELECT * FROM TEST_SOURCE_TABLE");
        tasklet.setTargetDataSource(dataSource);
        tasklet.setDestinationTableName("TEST_TARGET_TABLE");
        Map<String, String> mapping = new HashMap<>();
        mapping.put("DATE_COL", "DATE_COL");
        mapping.put("NVARCHAR_COL", "NVARCHAR_COL");
        mapping.put("INTEGER_COL", "INTEGER_COL");
        mapping.put("VARCHAR_COL", "VARCHAR_COL");
        mapping.put("DECIMAL_COL", "DECIMAL_COL");
        tasklet.setColumnMapping(mapping);
        tasklet.afterPropertiesSet();
        tasklet.execute(null, null);
        Integer countAfterExecute = count(new JdbcTemplate(dataSource), "TEST_TARGET_TABLE");
        logger.debug("countAfterExecute: {}", countAfterExecute);
        Assert.assertTrue(countAfterExecute == 1);
    }

    //@Ignore
    @Test
    public void MssqlBulkCopyItemWriterTest() throws Exception {
        DataSource dataSource = getDataSource();
        Integer countBeforeExecute = count(new JdbcTemplate(dataSource), "TEST_TARGET_TABLE");
        logger.debug("countBeforeExecute: {}", countBeforeExecute);
        MssqlBulkCopyItemWriter writer = new MssqlBulkCopyItemWriter();
        writer.setDataSource(dataSource);
        writer.setDestinationTableName("TEST_TARGET_TABLE");
        writer.afterPropertiesSet();
        TestModel model = new TestModel();
        model.setDateCol(new Date());
        model.setDecimalCol(BigDecimal.valueOf(1.234));
        model.setIntegerCol(123);
        model.setNvarcharCol("吃飯飯;");
        model.setVarcharCol("睡覺覺");
        writer.write(Arrays.asList(model));
        Integer countAfterExecute = count(new JdbcTemplate(dataSource), "TEST_TARGET_TABLE");
        logger.debug("countAfterExecute: {}", countAfterExecute);
        Assert.assertTrue(countAfterExecute == 1);
    }

    private Integer count(JdbcTemplate jdbcTemplate, String table) {
        return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM " + table, Integer.class);
    }

}
