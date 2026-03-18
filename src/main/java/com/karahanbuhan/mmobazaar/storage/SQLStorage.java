package com.karahanbuhan.mmobazaar.storage;

import com.karahanbuhan.mmobazaar.bazaar.BazaarData;
import com.karahanbuhan.mmobazaar.localization.TranslationKey;
import com.karahanbuhan.mmobazaar.localization.TranslationLogger;
import com.karahanbuhan.mmobazaar.storage.api.BazaarStorage;
import com.karahanbuhan.mmobazaar.storage.binding.BazaarBinder;
import com.karahanbuhan.mmobazaar.storage.jdbc.UUIDAdapter;
import com.karahanbuhan.mmobazaar.storage.mapping.BazaarMapper;
import com.karahanbuhan.mmobazaar.storage.util.SQLRunnable;
import com.karahanbuhan.mmobazaar.storage.util.SQLSupplier;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public abstract class SQLStorage implements BazaarStorage {
    protected final DataSource dataSource;
    protected final UUIDAdapter uuidAdapter;
    protected final BazaarBinder binder;
    protected final BazaarMapper mapper;
    protected final TranslationLogger logger;

    public SQLStorage(DataSource dataSource, UUIDAdapter uuidAdapter, TranslationLogger logger) {
        this.dataSource = dataSource;
        this.uuidAdapter = uuidAdapter;
        this.binder = new BazaarBinder(uuidAdapter);
        this.mapper = new BazaarMapper(uuidAdapter);
        this.logger = logger;
    }

    protected void executeSchema(Connection conn, String... statements) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            for (String sql : statements) {
                stmt.executeUpdate(sql);
            }
        }
    }

    protected boolean runTransaction(SQLRunnable action) {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try {
                action.run(conn);
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                logger.exception(TranslationKey.LOG_TRANSACTION_FAILED, e);
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            logger.exception(TranslationKey.LOG_CONNECTION_FAILED, e);
            return false;
        }
    }

    protected <T> Optional<T> runQuery(SQLSupplier<T> action) {
        try (Connection conn = dataSource.getConnection()) {
            return Optional.ofNullable(action.run(conn));
        } catch (SQLException e) {
            logger.exception(TranslationKey.LOG_QUERY_FAILED, e);
            return Optional.empty();
        }
    }

    protected boolean deleteBazaar(Connection conn, UUID bazaarId, String deleteBazaarSQL, String deleteListingsSQL) throws SQLException {
        try (PreparedStatement psListing = conn.prepareStatement(deleteListingsSQL); PreparedStatement psBazaar = conn.prepareStatement(deleteBazaarSQL)) {
            uuidAdapter.set(psListing, 1, bazaarId);
            uuidAdapter.set(psBazaar, 1, bazaarId);

            psListing.executeUpdate(); // Delete listing first because of reference system
            psBazaar.executeUpdate();

            return true;
        }
    }

    protected void upsertBazaarWithListings(Connection conn, BazaarData data, String insertBazaarSQL, String insertListingSQL) throws SQLException {
        try (PreparedStatement psBazaar = conn.prepareStatement(insertBazaarSQL); PreparedStatement psListing = conn.prepareStatement(insertListingSQL)) {
            binder.bindBazaarInsert(psBazaar, data);
            binder.bindListingsInsert(psListing, data.getId(), data.getListings());

            psBazaar.executeUpdate();
            psListing.executeBatch();
        }
    }

    protected void batchInsertBazaars(Connection conn, Collection<BazaarData> all, String insertBazaarSQL, String insertListingSQL) throws SQLException {
        try (PreparedStatement psBazaar = conn.prepareStatement(insertBazaarSQL); PreparedStatement psListing = conn.prepareStatement(insertListingSQL)) {
            for (BazaarData data : all) {
                binder.bindBazaarInsert(psBazaar, data);
                psBazaar.addBatch();

                binder.bindListingsInsert(psListing, data.getId(), data.getListings());
            }

            psBazaar.executeBatch();
            psListing.executeBatch();
        }
    }

    protected Optional<BazaarData> fetchBazaarWithListings(Connection conn, UUID bazaarId, String selectBazaarSQL, String selectListingSQL) throws SQLException {
        try (PreparedStatement psBazaar = conn.prepareStatement(selectBazaarSQL); PreparedStatement psListing = conn.prepareStatement(selectListingSQL)) {
            uuidAdapter.set(psBazaar, 1, bazaarId);

            try (ResultSet rs = psBazaar.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(mapper.fromResultSet(rs, psListing));
            }
        }
    }

    protected List<BazaarData> fetchAllBazaarWithListings(Connection conn, String selectAllBazaarsSQL, String selectListingSQL) throws SQLException {
        List<BazaarData> result = new ArrayList<>();

        try (PreparedStatement psBazaars = conn.prepareStatement(selectAllBazaarsSQL); PreparedStatement psListing = conn.prepareStatement(selectListingSQL); ResultSet rs = psBazaars.executeQuery()) {
            while (rs.next()) {
                try {
                    result.add(mapper.fromResultSet(rs, psListing));
                } catch (IllegalStateException e) {
                    logger.exception(TranslationKey.LOG_CORRUPTED_BAZAAR, e);
                }
            }
        }

        return result;
    }
}
