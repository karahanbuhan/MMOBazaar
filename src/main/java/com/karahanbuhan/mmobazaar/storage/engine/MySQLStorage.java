package com.karahanbuhan.mmobazaar.storage.engine;

import com.zaxxer.hikari.HikariDataSource;
import com.karahanbuhan.mmobazaar.bazaar.BazaarData;
import com.karahanbuhan.mmobazaar.localization.TranslationLogger;
import com.karahanbuhan.mmobazaar.storage.SQLStorage;
import com.karahanbuhan.mmobazaar.storage.jdbc.UUIDAdapter;
import com.karahanbuhan.mmobazaar.storage.schema.SQLSchema;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MySQLStorage extends SQLStorage {
    public MySQLStorage(HikariDataSource dataSource, TranslationLogger logger) {
        super(dataSource, UUIDAdapter.defaultAdapter(), logger);
    }

    @Override
    public boolean init() {
        return runTransaction(conn -> executeSchema(conn, SQLSchema.MySQL.CREATE_BAZAARS, SQLSchema.MySQL.CREATE_LISTINGS));
    }

    @Override
    public boolean saveBazaar(BazaarData data) {
        return runTransaction(conn -> upsertBazaarWithListings(conn, data, SQLSchema.MySQL.INSERT_BAZAAR, SQLSchema.MySQL.INSERT_LISTING));
    }

    @Override
    public boolean deleteBazaar(UUID bazaarId) {
        return runTransaction(conn -> deleteBazaar(conn, bazaarId, SQLSchema.MySQL.DELETE_BAZAAR, SQLSchema.MySQL.DELETE_LISTINGS));
    }

    @Override
    public boolean saveBazaars(Collection<BazaarData> bazaars) {
        return runTransaction(conn -> batchInsertBazaars(conn, bazaars, SQLSchema.MySQL.INSERT_BAZAAR, SQLSchema.MySQL.INSERT_LISTING));
    }

    @Override
    public Optional<BazaarData> loadBazaar(UUID bazaarId) {
        return runQuery(conn -> fetchBazaarWithListings(conn, bazaarId, SQLSchema.MySQL.SELECT_BAZAAR, SQLSchema.MySQL.SELECT_LISTINGS).orElse(null));
    }

    @Override
    public Optional<List<BazaarData>> loadAllBazaars() {
        return runQuery(conn -> fetchAllBazaarWithListings(conn, SQLSchema.MySQL.SELECT_ALL_BAZAARS, SQLSchema.MySQL.SELECT_LISTINGS));
    }
}