package br.com.navis.transportadora.config.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class MultiTenantConnectionProviderImpl implements MultiTenantConnectionProvider {

    private final DataSource dataSource;

    public MultiTenantConnectionProviderImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Override
    public Connection getConnection(Object tenantIdentifier) throws SQLException {
        if (tenantIdentifier instanceof String) {
            String tenantId = (String) tenantIdentifier;
            final Connection connection = getAnyConnection();
            try {
                connection.createStatement().execute("SET search_path TO " + tenantId);

                System.out.println("conexão: ");
                System.out.println(connection);

            } catch (SQLException e) {
                throw new HibernateException("Não foi possível alterar para o schema " + tenantId, e);
            }
            return connection;
        } else {
            throw new HibernateException("O identificador do locatário deve ser uma String, mas foi fornecido: " + tenantIdentifier);
        }
    }

    @Override
    public void releaseConnection(Object tenantIdentifier, Connection connection) throws SQLException {
        if (tenantIdentifier instanceof String) {
            String tenantId = (String) tenantIdentifier;
            try {
                connection.createStatement().execute("SET search_path TO " + tenantId);
            } catch (SQLException e) {
                throw new HibernateException("Não foi possível se conectar ao schema " + tenantId, e);
            } finally {
                releaseAnyConnection(connection);
            }
        } else {
            throw new HibernateException("O identificador do locatário deve ser uma String, mas foi fornecido: " + tenantIdentifier);
        }
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return true;
    }

    @Override
    public boolean isUnwrappableAs(Class aClass) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return null;
    }
}