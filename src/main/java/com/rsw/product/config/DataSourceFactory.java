package com.rsw.product.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import com.rsw.product.config.DataSourceProperties.CommonProperties;
import com.rsw.product.config.DataSourceProperties.ShardHost;
import javax.sql.DataSource;

/**
 * Created by DAlms on 2/19/17.
 */
public abstract class DataSourceFactory {

    public static DataSource newInstance(CommonProperties commonProperties, ShardHost host)
            throws ClassNotFoundException {

        Class clazz = Class.forName(commonProperties.getType());
        DataSource dataSource = DataSourceBuilder.create().type(clazz)
            .driverClassName(commonProperties.getDriverClassName())
            .url(host.getUrl())
            .username(host.getUsername())
            .password(host.getPassword()).build();

        // set pool properties if this is a Tomcat Pool type datasource, and if pool properties exist
        if (dataSource instanceof org.apache.tomcat.jdbc.pool.DataSource) {
            org.apache.tomcat.jdbc.pool.DataSource tomcatDataSource =
                    (org.apache.tomcat.jdbc.pool.DataSource) dataSource;

            if (commonProperties.getInitialSize() != null) {
                tomcatDataSource.setInitialSize(commonProperties.getInitialSize());
            }
            if (commonProperties.getMaxActive() != null) {
                tomcatDataSource.setMaxActive(commonProperties.getMaxActive());
            }
            if (commonProperties.getMinIdle() != null) {
                tomcatDataSource.setMinIdle(commonProperties.getMinIdle());
            }
            if (commonProperties.getMaxIdle() != null) {
                tomcatDataSource.setMaxIdle(commonProperties.getMaxIdle());
            }
        }
        return dataSource;
    }
}
