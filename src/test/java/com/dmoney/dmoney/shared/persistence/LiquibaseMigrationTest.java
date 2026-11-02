package com.dmoney.dmoney.shared.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:migration;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=validate",
        "spring.liquibase.enabled=true",
        "spring.liquibase.changelog=classpath:db/changelog/db.changelog-master.xml",
        "spring.liquibase.contexts=test"
})
class LiquibaseMigrationTest {

    private static final List<String> TABLES = List.of(
            "users", "category", "subcategory", "tag", "movement", "movement_tags"
    );

    @Autowired
    private JdbcTemplate jdbc;

    @Test
    void appliesTheChangelogToAnEmptyDatabase() {
        TABLES.forEach(table ->
                assertThat(jdbc.queryForObject(
                        "select count(*) from " + table,
                        Integer.class
                )).isZero());
    }

    @Test
    void recordsTheSchemaChangesets() {
        Integer applied = jdbc.queryForObject(
                "select count(*) from databasechangelog",
                Integer.class
        );

        assertThat(applied).isGreaterThanOrEqualTo(6);
    }
}
