package com.karahanbuhan.mmobazaar.storage;

import com.zaxxer.hikari.HikariDataSource;
import com.karahanbuhan.mmobazaar.config.StorageConfig;
import com.karahanbuhan.mmobazaar.storage.api.BazaarStorage;
import com.karahanbuhan.mmobazaar.storage.engine.MySQLStorage;
import com.karahanbuhan.mmobazaar.storage.engine.PostgreSQLStorage;
import com.karahanbuhan.mmobazaar.storage.engine.SQLiteStorage;

import java.util.logging.Logger;

public class SQLStorageFactory {
    private final Logger logger;

    public SQLStorageFactory(Logger logger) {
        this.logger = logger;
    }

    public BazaarStorage create(StorageConfig config) throws ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
        Class.forName("org.postgresql.Driver");

        return switch (config.getDialect()) {
            case SQLITE -> new SQLiteStorage(new HikariDataSource(config.getHikariConfig()), logger);
            case MYSQL, MARIADB -> new MySQLStorage(new HikariDataSource(config.getHikariConfig()), logger);
            case POSTGRES -> new PostgreSQLStorage(new HikariDataSource(config.getHikariConfig()), logger);
        };
    }
}