package com.rsw.product.config;

import com.rsw.product.domain.Promoter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Created by DAlms on 2/19/17.
 * Creates a map of promoters keyed by schema name
 * This property-driven approach could be replaced by a service that manages/provides schema-promoter mappings
 * real time
 */
@Configuration
@ConfigurationProperties("promoter")
public class PromoterProperties {

    private Map<String,Promoter> schemaMap;

    public Map<String, Promoter> getSchemaMap() {
        return schemaMap;
    }

    public PromoterProperties setSchemaMap(Map<String, Promoter> schemaMap) {
        this.schemaMap = schemaMap;
        return this;
    }

}