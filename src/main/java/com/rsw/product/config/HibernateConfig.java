package com.rsw.product.config;

import com.rsw.product.config.DataSourceProperties.CommonProperties;
import com.rsw.product.config.DataSourceProperties.ShardHost;
import com.rsw.product.tenant.TenantConnectionProvider;
import com.rsw.product.tenant.TenantDataSource;
import com.rsw.product.tenant.TenantSchemaResolver;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DAlms on 1/2/17.
 */
@Configuration
public class HibernateConfig {
    private static final Logger LOG = LoggerFactory.getLogger(HibernateConfig.class);

    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Value("${product.defaultShard}")
    private String defaultShard;
    @Value("${product.defaultSchema}")
    private String defaultSchema;

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    public MultiTenantConnectionProvider tenantConnectionProvider() {
        return new TenantConnectionProvider(tenantDataSource(), defaultSchema);
    }

    @Bean
    public CurrentTenantIdentifierResolver tenantSchemaResolver() {
        return new TenantSchemaResolver(defaultSchema);
    }

    @Primary
    @Bean
    public TenantDataSource tenantDataSource() {
        TenantDataSource tenantDataSource = new TenantDataSource(defaultShard);
        Map<Object, Object> dataSourceMap = new HashMap<>();

        DataSource defaultDataSource = null;
        CommonProperties commonProperties = dataSourceProperties.getCommonProperties();
        Map<String, ShardHost> hostMap = dataSourceProperties.getHostMap();
        if (hostMap == null || hostMap.isEmpty()) {
            LOG.error("No shard host properties were mapped, no Datasources defined!");
            throw new IllegalArgumentException("No shard host properties were mapped");
        }
        for (String shardKey : hostMap.keySet()) {
            ShardHost shardHost = hostMap.get(shardKey);
            try {
                DataSource dataSource = DataSourceFactory.newInstance(commonProperties, shardHost);
                dataSourceMap.put(shardKey, dataSource);
                if (shardKey.equalsIgnoreCase(defaultShard)) {
                    defaultDataSource = dataSource;
                }
            } catch(ClassNotFoundException e) {
                LOG.error("Shard key {} has invalid class type {}", shardKey, commonProperties.getType());
                continue;
            }
        }

        tenantDataSource.setTargetDataSources(dataSourceMap);
        tenantDataSource.setDefaultTargetDataSource(defaultDataSource);

        return tenantDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("tenantDataSource")
                                                                       TenantDataSource tenantDataSource,
                                                                       @Qualifier("tenantConnectionProvider")
                                                                       MultiTenantConnectionProvider connectionProvider,
                                                                       @Qualifier("tenantSchemaResolver")
                                                                       CurrentTenantIdentifierResolver
                                                                                   tenantIdentifierResolver) {
        Map<String, Object> properties = new HashMap<>();
        properties.putAll(jpaProperties.getHibernateProperties(tenantDataSource));
        properties.put(Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
        properties.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, connectionProvider);
        properties.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantIdentifierResolver);

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(tenantDataSource);
        em.setPackagesToScan("com.rsw.product.entity");
        em.setJpaVendorAdapter(jpaVendorAdapter());
        em.setJpaPropertyMap(properties);

        return em;
    }
}
