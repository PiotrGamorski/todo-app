package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class V9__init_project_tables extends BaseJavaMigration {
    @Override
    public void migrate(final Context context) {
        new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true))
                .execute("CREATE TABLE PROJECTS (" +
                        "id int primary key auto_increment, " +
                        "description varchar(100) not null); " +
                        "" +
                        "CREATE TABLE PROJECT_STEPS (" +
                        "id int primary key auto_increment, " +
                        "description varchar(100) not null, " +
                        "days_to_deadline int not null, " +
                        "project_id int not null, " +
                        "foreign key (project_id) references PROJECTS(id)); " +
                        "" +
                        "ALTER TABLE TASK_GROUPS ADD COLUMN project_id int null; " +
                        "" +
                        "ALTER TABLE TASK_GROUPS ADD foreign key (project_id) references PROJECTS(id);"
                );
    }
}
