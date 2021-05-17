package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class V8__init_task_groups_table extends BaseJavaMigration {
    @Override
    public void migrate(final Context context) {
        new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true))
                .execute("CREATE TABLE TASK_GROUPS (id int primary key auto_increment, description varchar(100) not null, done bit); " +
                        "ALTER TABLE TASKS ADD COLUMN TASK_GROUP_ID int; " +
                        "ALTER TABLE TASKS ADD foreign key (TASK_GROUP_ID) REFERENCES TASK_GROUPS(ID); ");
    }
}
