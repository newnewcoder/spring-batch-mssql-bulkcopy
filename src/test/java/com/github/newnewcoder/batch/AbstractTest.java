package com.github.newnewcoder.batch;

import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.connection.waiting.HealthChecks;

public abstract class AbstractTest {

    protected static final Logger logger = LogManager.getLogger();

    protected static DataSource getDataSource() throws Exception {
        return getDataSource("TEST_DB");
    }

    protected static DataSource getDataSource(String database) throws Exception {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl("jdbc:sqlserver://localhost:1433;databaseName=" + database + ";");
        dataSource.setUsername("sa");
        dataSource.setPassword("\\!Passw0rd");
        return dataSource;
    }

    @ClassRule
    public static DockerComposeRule docker = DockerComposeRule.builder()
            .file("src/test/resources/docker-compose.yml")
            .pullOnStartup(true)
            //.skipShutdown(true)
            .removeConflictingContainersOnStartup(true)
            .waitingForService("sqlserver", HealthChecks.toHaveAllPortsOpen())
            .saveLogsTo("build/dockerLogs/dockerComposeRuleTest")
            .build();

    @BeforeClass
    public static void setupDatabase() throws Exception {
        ScriptUtils.executeSqlScript(getDataSource("master").getConnection(), new ClassPathResource("setup.sql"));
    }

    @Before
    public void truncateTargetTable() throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        jdbcTemplate.execute("TRUNCATE TABLE TEST_TARGET_TABLE");
    }
}
