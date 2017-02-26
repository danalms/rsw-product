package com.rsw.product.config;

import com.rsw.product.domain.ProductRequestData;
import com.rsw.product.entity.Product;
import com.rsw.product.tenant.PromoterCache;
import com.rsw.product.tenant.PromoterInterceptor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by DAlms on 2/19/17.
 * General application beans
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    PromoterCache promoterCache;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
            .addInterceptor(new PromoterInterceptor(promoterCache))
            .addPathPatterns("/api/*/product/**");
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean(name = "messageSource")
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageBundle = new ReloadableResourceBundleMessageSource();
        messageBundle.setBasename("classpath:messages/messages");
        messageBundle.setDefaultEncoding("UTF-8");
        return messageBundle;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new ProductRequestDataMap());
        return modelMapper;
    }

    /**
     * custom map for ModelMapper:
     * skip fields in Product that don't exist in ProductRequestData
     * NOTE: the destination class cannot use the builder pattern for its setters - it trips up the
     * model mapper validation/configuration
     */
    private static class ProductRequestDataMap extends PropertyMap<ProductRequestData, Product> {
        @Override
        protected void configure() {
            skip().setProductId(null);
            skip().setCreatedDate(null);
            skip().setUpdatedDate(null);
        }
    }
}
