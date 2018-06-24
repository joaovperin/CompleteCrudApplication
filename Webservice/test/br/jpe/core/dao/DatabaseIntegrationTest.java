package br.jpe.core.dao;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import br.jpe.core.database.Connection;
import br.jpe.core.database.sql.ConnectionFactory;
import br.jpe.core.database.DBException;
import br.jpe.core.database.sql.connection.SQLConnection;
import org.junit.Test;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;

/**
 * Test suite for DATABASE integration tests
 *
 * @author joaovperin
 */
public class DatabaseIntegrationTest {

    /**
     * Main method, used to run the tests
     *
     * @throws DBException
     */
    @Test
    public void runTestSuite() throws DBException {
        cleanUpDatabase();
        setUpDatabase();
        print("Running tests");
        Result result = org.junit.runner.JUnitCore.runClasses(SuiteTests.class);

        print("Finished Tests! Status: " + result.wasSuccessful());
        result.getFailures().forEach((Failure failure) -> {
            error(failure.toString());
        });
        print("***");
        result.getFailures().forEach(System.err::println);
    }

    /**
     * Setup database before executing the tests
     *
     * @throws DBException
     */
    private void setUpDatabase() throws DBException {
        print("Setting UP Database...");
        try (Connection conn = ConnectionFactory.transaction()) {
            DBUtils.createTestDB((SQLConnection) conn);
            conn.commit();
        }
        print("Setting UP Database Completed.");
    }

    /**
     * Cleans up database after the tests
     *
     * @throws DBException
     */
    private void cleanUpDatabase() throws DBException {
        print("Cleaning up Database...");
        try (Connection conn = ConnectionFactory.transaction()) {
            DBUtils.deleteTestDB((SQLConnection) conn);
            conn.commit();
        }
        print("Cleaning up Database Completed.");
    }

    /**
     * Prints a message
     *
     * @param message
     */
    private void print(Object message) {
        System.out.printf("*** %s\n", message);
    }

    /**
     * Prints an error message
     *
     * @param message
     */
    private void error(Object message) {
        System.err.printf("*** %s\n", message);
    }

    /**
     * The test suite class
     */
    @RunWith(Suite.class)
    @Suite.SuiteClasses({
        GenericDAOTestDB.class,
        GenericDAOTransactionsTestDB.class
    })
    public static class SuiteTests {
    }

}
