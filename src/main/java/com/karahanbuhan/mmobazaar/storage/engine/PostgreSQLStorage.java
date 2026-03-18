package com.karahanbuhan.mmobazaar.storage.engine;

import com.karahanbuhan.mmobazaar.bazaar.BazaarData;
import com.karahanbuhan.mmobazaar.localization.TranslationLogger;
import com.karahanbuhan.mmobazaar.storage.SQLStorage;
import com.karahanbuhan.mmobazaar.storage.jdbc.UUIDAdapter;
import com.karahanbuhan.mmobazaar.storage.schema.SQLSchema;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PostgreSQLStorage extends SQLStorage {
    public PostgreSQLStorage(DataSource dataSource, TranslationLogger logger) {
        super(dataSource, UUIDAdapter.postgresAdapter(), logger);
    }

    @Override
    public boolean init() {
        return runTransaction(conn -> executeSchema(conn, SQLSchema.PostgreSQL.CREATE_BAZAARS, SQLSchema.PostgreSQL.CREATE_LISTINGS));
    }

    @Override
    public boolean saveBazaar(BazaarData data) {
        return runTransaction(conn -> upsertBazaarWithListings(conn, data, SQLSchema.PostgreSQL.INSERT_BAZAAR, SQLSchema.PostgreSQL.INSERT_LISTING));
    }

    @Override
    public boolean deleteBazaar(UUID bazaarId) {
        return runTransaction(conn -> deleteBazaar(conn, bazaarId, SQLSchema.PostgreSQL.DELETE_BAZAAR, SQLSchema.PostgreSQL.DELETE_LISTINGS));
    }

    @Override
    public boolean saveBazaars(Collection<BazaarData> bazaars) {
        return runTransaction(conn -> batchInsertBazaars(conn, bazaars, SQLSchema.PostgreSQL.INSERT_BAZAAR, SQLSchema.PostgreSQL.INSERT_LISTING));
    }

    @Override
    public Optional<BazaarData> loadBazaar(UUID bazaarId) {
        return runQuery(conn -> fetchBazaarWithListings(conn, bazaarId, SQLSchema.PostgreSQL.SELECT_BAZAAR, SQLSchema.PostgreSQL.SELECT_LISTINGS).orElse(null));
    }

    @Override
    public Optional<List<BazaarData>> loadAllBazaars() {
        return runQuery(conn -> fetchAllBazaarWithListings(conn, SQLSchema.PostgreSQL.SELECT_ALL_BAZAARS, SQLSchema.PostgreSQL.SELECT_LISTINGS));
    }
}