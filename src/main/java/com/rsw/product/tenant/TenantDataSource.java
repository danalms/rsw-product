package com.rsw.product.tenant;

import com.rsw.product.domain.Promoter;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created by DAlms on 1/22/17.
 * Proxy DataSource -  provides shard-based datasource routing for multi-tenancy based on promoter
 */
public class TenantDataSource extends AbstractRoutingDataSource {
    private String defaultShardKey;

    public TenantDataSource(String defaultShardKey) {
        this.defaultShardKey = defaultShardKey;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        Promoter promoter = PromoterContext.getCurrentPromoter();
        if (promoter == null) {
            return defaultShardKey;
        }
        return promoter.getHostShardName();
    }

}
