package com.rsw.product.tenant;

import com.rsw.product.utils.PromoterModelUtils;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by DAlms on 2/26/17.
 */
public class TenantDataSourceTest {

    private TenantDataSource subject;

    @Before
    public void setup() {
        subject = new TenantDataSource("testdefault");
    }

    @Test
    public void determineCurrentLookupKey_happy() throws Exception {
        PromoterContext.setCurrentPromoter(PromoterModelUtils.buildPromoter(3L).setHostShardName("myshardkey"));

        String result = (String) subject.determineCurrentLookupKey();
        assertThat(result).isEqualTo("myshardkey");
    }

    @Test
    public void determineCurrentLookupKey_default() throws Exception {
        PromoterContext.setCurrentPromoter(null);

        String result = (String) subject.determineCurrentLookupKey();
        assertThat(result).isEqualTo("testdefault");
    }

}