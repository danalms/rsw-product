package com.rsw.product.tenant;

import com.rsw.product.domain.Promoter;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by DAlms on 2/19/17.
 */
public class TenantSchemaResolver implements CurrentTenantIdentifierResolver {
    private static final Logger LOG = LoggerFactory.getLogger(TenantSchemaResolver.class);

    private String defaultSchema;

    public TenantSchemaResolver(String defaultSchema) {
        this.defaultSchema = defaultSchema;
    }

    /**
     * unit test support
     * @param defaultSchema
     */
    void setDefaultSchema(String defaultSchema) {
        this.defaultSchema = defaultSchema;
    }

    @Override
    public String resolveCurrentTenantIdentifier() {
        Promoter promoter = PromoterContext.getCurrentPromoter();
        if (promoter == null) {
            return defaultSchema;
        }
        return promoter.getSchemaName();
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
