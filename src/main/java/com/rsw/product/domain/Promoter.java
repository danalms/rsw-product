package com.rsw.product.domain;

import java.util.Objects;

/**
 * Created by DAlms on 2/19/17.
 * Thin Promoter domain view used for revealing schema and optional shard
 */
public class Promoter {
    private Long promoterId;
    private String promoterName;
    private String schemaName;
    private String hostShardName;

    public Long getPromoterId() {
        return promoterId;
    }

    public Promoter setPromoterId(Long promoterId) {
        this.promoterId = promoterId;
        return this;
    }

    public String getPromoterName() {
        return promoterName;
    }

    public Promoter setPromoterName(String promoterName) {
        this.promoterName = promoterName;
        return this;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public Promoter setSchemaName(String schemaName) {
        this.schemaName = schemaName;
        return this;
    }

    public String getHostShardName() {
        return hostShardName;
    }

    public Promoter setHostShardName(String hostShardName) {
        this.hostShardName = hostShardName;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Promoter promoter = (Promoter) o;
        return Objects.equals(promoterId, promoter.promoterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(promoterId);
    }

}
