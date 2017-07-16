package pl.edu.pwr.wordnetloom.application.flyway;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;

@Singleton
@Startup
@TransactionManagement(TransactionManagementType.BEAN)
public class DbMigrator {

    private final Logger log = Logger.getLogger(DbMigrator.class.getName());

    @Resource(lookup = "java:/datasources/wordnet")
    private DataSource dataSource;

    @PostConstruct
    private void onStartup() {
        if (dataSource == null) {
            log.severe("No datasource found to execute the db migrations!");
            throw new EJBException("No datasource found to execute the db migrations!");
        }

        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setBaselineOnMigrate(true);
        for (MigrationInfo i : flyway.info().all()) {
            log.log(Level.INFO, "Migrate task: {0} : {1} from file: {2}", new Object[]{i.getVersion(), i.getDescription(), i.getScript()});
        }
        flyway.migrate();
    }
}
