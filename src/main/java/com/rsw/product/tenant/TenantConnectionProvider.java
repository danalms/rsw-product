package com.rsw.product.tenant;

import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by DAlms on 1/2/17.
 */
public class TenantConnectionProvider implements MultiTenantConnectionProvider {
    private DataSource dataSource;
    private String defaultSchema;

    public TenantConnectionProvider(DataSource dataSource,
                                    String defaultSchema) {
        this.dataSource = dataSource;
        this.defaultSchema = defaultSchema;
    }

    /**
     * unit test support
     */
    void setDefaultSchema(String defaultSchema) {
        this.defaultSchema = defaultSchema;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        Connection connection = getAnyConnection();
        try {
            String searchPathSql = String.format("SET search_path = %s", tenantIdentifier);
            connection.createStatement().execute(searchPathSql);
        } catch (SQLException e) {
            throw new HibernateException("Could not set search_path for community [" + tenantIdentifier + "]");
        }
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        try {
            String searchPathSql = String.format("SET search_path = %s", defaultSchema);
            connection.createStatement().execute(searchPathSql);
        } catch (SQLException e) {
            throw new HibernateException("Could not set search_path back to default "
                    + "schema [" + defaultSchema + "]");
        } finally {
            releaseAnyConnection(connection);
        }
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return true;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return null;
    }
}
