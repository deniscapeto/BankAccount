package com.cockroachlabs;

import com.cockroachlabs.application.AllOperationsUseCase;
import com.cockroachlabs.infraestructure.cockroachdb.AccountRepository;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BankApplication {

    public static void main(String[] args) throws IOException {

        PGSimpleDataSource ds = new PGSimpleDataSource();

        InitializeDataSource(ds);

        AccountRepository accountRepository = new AccountRepository(ds);

        var useCase = new AllOperationsUseCase(accountRepository);

        useCase.execute();
    }

    private static void InitializeDataSource(PGSimpleDataSource ds) throws IOException {

        Properties props = BankApplication.getProperties();

        ds.setServerNames(new String[]{props.getProperty("datasource.server_name")});
        ds.setPortNumbers(new int[]{Integer.parseInt(props.getProperty("datasource.port"))});
        ds.setDatabaseName(props.getProperty("datasource.database_name"));
        ds.setSsl(true);
        ds.setUser(props.getProperty("datasource.username"));
        ds.setPassword(props.getProperty("datasource.password"));
        ds.setSslMode("verify-full");
        ds.setSslRootCert(System.getenv("$HOME/.postgresql/root.crt"));
        ds.setReWriteBatchedInserts(true); // add `rewriteBatchedInserts=true` to pg connection string
        ds.setApplicationName("BasicExample");
    }

    public static Properties getProperties() throws IOException {

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Properties props = new Properties();
        FileInputStream file = new FileInputStream(
                classLoader.getResource("bank.properties").getFile()
        );
        props.load(file);
        return props;
    }
}
