package pl.edu.pwr.wordnetloom.application.flyway;

import db.migration.commentParser.CommentParser;
import db.migration.commentParser.ParserResult;
import db.migration.commentParser.PrincetonDefinitionParser;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

        CommentParser parser = new CommentParser();
        List<ParserResult> results = parser.parse("jarmułka (“kipa czy medalik na szyi są elementami tożsamości”)");
        PrincetonDefinitionParser parser2 = new PrincetonDefinitionParser();
        List<ParserResult> results2 = parser2.parse("jarmułka (“kipa czy medalik na szyi są elementami tożsamości”)");

        setAttributes(results);
    }

    private void setAttributes( List<ParserResult> results)  {
        StringBuilder unknown = new StringBuilder();

        for (ParserResult result : results) {
            switch (result.getType()) {
                case COMMENT:
                    System.out.println(result.getValue());
                    break;
                case REGISTER:
                    System.out.println("REGISTER");
                    break;
                case LINK:
                    System.out.println(result.getValue());
                    break;
                case DEFINITION:
                    System.out.println(result.getValue());
                    break;
                case EXAMPLE:
                    System.out.println("EXAMPLE");
                    break;
                case UNKNOWN:
                    unknown.append(result.getValue()).append(" ");
                    break;
            }
        }
        if (unknown.length() > 0) {
            System.out.println(unknown.toString());
        }
    }
}
