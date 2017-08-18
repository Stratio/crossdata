package com.stratio.tests;

import com.stratio.qa.cucumber.testng.CucumberRunner;
import com.stratio.qa.utils.BaseTest;
import com.stratio.qa.utils.CassandraUtils;
import com.stratio.qa.utils.ThreadProperty;
import cucumber.api.CucumberOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.TimeZone;

//Indicar feature
@CucumberOptions(features = {
        "src/test/resources/features/PostgreSQL/PostgresqlNotBetweenFilter.feature",
        "src/test/resources/features/PostgreSQL/PostgresqlBetweenFilter.feature",
        "src/test/resources/features/PostgreSQL/PostgresqlLimit.feature",
        "src/test/resources/features/PostgreSQL/PostgresqlInFilter.feature",
        "src/test/resources/features/PostgreSQL/PostgresqlLessFilter.feature",
        "src/test/resources/features/PostgreSQL/PostgresqlLessEqualFilter.feature",
        "src/test/resources/features/PostgreSQL/PostgresqlGreaterFilter.feature",
        "src/test/resources/features/PostgreSQL/PostgresqlGreaterEqualsFilter.feature",
        "src/test/resources/features/PostgreSQL/PostgresqlSelectAndOrFilter.feature",
        "src/test/resources/features/PostgreSQL/PostgresqlSelectSimple.feature",
        "src/test/resources/features/PostgreSQL/PostgresqlSelectEqualsFilter.feature"
})
public class ATPostgreSqlXDTest extends BaseTest {

	private final Logger logger = LoggerFactory.getLogger(this.getClass()
			.getCanonicalName());
    private String postgreSQL_hostname = System.getProperty("POSTGRESQL_HOSTNAME", "127.0.0.1");
    private String postgreSQL_port = System.getProperty("POSTGRESQL_PORT", "5432");
    private String postgreSQL_database = System.getProperty("POSTGRESQL_DATABASE", "hakama");
    private String postgreSQL_user = System.getProperty("POSTGRESQL_USER", "hakama");
    private String postgreSQL_password = System.getProperty("POSTGRESQL_PASSWORD", "hakama");
    CassandraUtils functions = new CassandraUtils();


    public ATPostgreSqlXDTest() {
	}

	@BeforeClass(groups = {"postgreSQL"})
	public void setUp() {
        logger.info("Default timezone: " + TimeZone.getDefault().toString());
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Madrid")); //works
        logger.info("Default timezone before set: " + TimeZone.getDefault().toString());
        String connector = "postgreSQL";
        ThreadProperty.set("Connector", connector);
        ThreadProperty.set("Driver", "context");
        logger.info("-------- PostgreSQL JDBC Connection ------------");
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
            return;
        }
        logger.info("PostgreSQL JDBC Driver Registered!");

        String postgresqlParams = "Postgresql parameters: " +
                "\nPostgresql hostname: " +  postgreSQL_hostname +
                "\nPostgresql Port: " + postgreSQL_port +
                "\nPostgresql database: " + postgreSQL_database +
                "\nPostgresql user: " +  postgreSQL_user +
                "\nPostgresql password: " + postgreSQL_password;

        logger.info(postgresqlParams);

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://"+ postgreSQL_hostname +":"+postgreSQL_port+"/" + postgreSQL_database, postgreSQL_user,
                    postgreSQL_password);

        } catch (SQLException e) {
            logger.error("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }
        List<String> sqls = functions.loadScript("/scripts/PostgresSQLScript.sql");
        for(int i = 0;  i < sqls.size(); i ++) {
            try {
                connection.createStatement().execute(sqls.get(i));
            } catch (SQLException e) {
                logger.error("SQL error:" + sqls.get(i));
                e.printStackTrace();
            }
        }

        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("The connection could not be closed");
            e.printStackTrace();
        }
    }

	@AfterClass(groups = {"postgreSQL"})
	public void cleanUp() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
            return;
        }
        logger.info("PostgreSQL JDBC Driver Registered!");
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://"+ postgreSQL_hostname +":"+postgreSQL_port+"/" + postgreSQL_database, postgreSQL_user,
                    postgreSQL_password);

        } catch (SQLException e) {
            logger.error("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }
        try {
            connection.createStatement().execute("DROP SCHEMA databasetest CASCADE;");
        } catch (SQLException e) {
            logger.error("SQL error: DROP SCHEMA databasetest CASCADE;");
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("The connection could not be closed");
            e.printStackTrace();
        }
	}

	@Test(enabled = true, groups = {"postgreSQL"})
	public void ATPostgreSqlXDTest() throws Exception {
		new CucumberRunner(this.getClass()).runCukes();
	}

}

