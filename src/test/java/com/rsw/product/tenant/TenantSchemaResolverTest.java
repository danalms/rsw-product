package com.rsw.product.tenant;

import com.rsw.product.domain.Promoter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by DAlms on 2/26/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class TenantSchemaResolverTest {

    private static final String DEFAULT_SCHEMA = "rswtest";

    @InjectMocks
    private TenantSchemaResolver subject;

    @Before
    public void setup() {
        PromoterContext.clear();
        subject.setDefaultSchema(DEFAULT_SCHEMA);
    }

    @Test
    public void resolveCurrentTenantIdentifier_happyPath() throws Exception {
        Promoter community = new Promoter().setSchemaName("myschema").setPromoterId(3L);

        PromoterContext.setCurrentPromoter(community);

        String result = subject.resolveCurrentTenantIdentifier();
        assertThat(result).isEqualTo("myschema");
    }

    @Test
    public void resolveCurrentTenantIdentifier_default() throws Exception {

        String result = subject.resolveCurrentTenantIdentifier();
        assertThat(result).isEqualTo(DEFAULT_SCHEMA);
    }

}