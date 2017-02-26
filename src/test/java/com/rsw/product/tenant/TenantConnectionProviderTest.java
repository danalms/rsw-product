package com.rsw.product.tenant;

import org.hibernate.HibernateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by DAlms on 2/26/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class TenantConnectionProviderTest {

    private static final String DEFAULT_SCHEMA = "rswtest";

    @Mock
    private DataSource dataSource;
    @Mock
    private Connection connection;
    @Mock
    private Statement statement;

    @InjectMocks
    private TenantConnectionProvider subject;

    @Before
    public void setup() {
        subject.setDefaultSchema(DEFAULT_SCHEMA);
    }

    @Test
    public void getAnyConnection_happyPath() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        Connection result = subject.getAnyConnection();
        assertThat(result).isEqualTo(connection);
        verify(dataSource).getConnection();
    }

    @Test
    public void releaseAnyConnection_happyPath() throws Exception {
        subject.releaseAnyConnection(connection);
        verify(connection).close();
    }

    @Test
    public void getConnection_happyPath() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);

        Connection result = subject.getConnection("testschema");
        assertThat(result).isEqualTo(connection);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(statement, times(1)).execute(captor.capture());
        assertThat(captor.getValue().toLowerCase()).containsPattern(" *set +search_path *= *testschema *");
    }

    @Test(expected = HibernateException.class)
    public void getConnection_SQLException() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.execute(anyString())).thenThrow(new SQLException());

        subject.getConnection("testschema");
    }

    @Test
    public void releaseConnection_happyPath() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);

        subject.releaseConnection("testschema", connection);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(statement, times(1)).execute(captor.capture());
        assertThat(captor.getValue().toLowerCase()).containsPattern(" *set +search_path *= *" + DEFAULT_SCHEMA + "*");
        verify(connection).close();
    }

    @Test(expected = HibernateException.class)
    public void releaseConnection_SQLException() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.execute(anyString())).thenThrow(new SQLException());

        subject.releaseConnection("testschema", connection);
        verify(connection).close();
    }

}