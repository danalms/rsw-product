package com.rsw.product.tenant;

import com.rsw.product.config.PromoterProperties;
import com.rsw.product.domain.Promoter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by DAlms on 2/19/17.
 */
@Component
public class PromoterCache {

    @Autowired
    private PromoterProperties promoterProperties;

    private static final Map<Long, Promoter> promoterMap = new HashMap<>();

    @PostConstruct
    public void init() {
        Map<String, Promoter> schemaMap = promoterProperties.getSchemaMap();
        for (String schemaName : schemaMap.keySet()) {
            Promoter promoter = schemaMap.get(schemaName);
            promoter.setSchemaName(schemaName);
            promoterMap.put(promoter.getPromoterId(), promoter);
        }
    }

    public Optional<Promoter> findPromoter(@NotNull Long promoterId) {
        return Optional.ofNullable(promoterMap.get(promoterId));
    }

}
