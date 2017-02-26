package com.rsw.product.utils;

import com.rsw.product.domain.Promoter;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAlms on 2/26/17.
 */
public abstract class PromoterModelUtils {

    public static List<Promoter> buildPromoters(Long ... ids) {
        List<Promoter> result = new ArrayList<>();
        for (int ix = 0; ix < ids.length; ix++) {
            result.add(buildPromoter(ids[ix]));
        }
        return result;
    }

    public static Promoter buildPromoter(Long id) {
        Promoter community = new Promoter()
                .setPromoterId(id)
                .setPromoterName(RandomStringUtils.randomAlphabetic(5))
                .setSchemaName(RandomStringUtils.randomAlphanumeric(10))
                .setHostShardName(RandomStringUtils.randomAlphanumeric(6));
        return community;
    }
}
