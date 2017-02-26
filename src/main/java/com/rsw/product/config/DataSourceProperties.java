package com.rsw.product.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Created by DAlms on 2/19/17.
 * Creates a map of property groups from the application.yml property groups prefixed with "datasource"
 * In order for the Map structure to be created, the next level property key must match the name of the
 * Java Map field, i.e. "hostMap"
 */
@Configuration
@ConfigurationProperties("datasource")
public class DataSourceProperties {
    private CommonProperties commonProperties;
    private Map<String,ShardHost> hostMap;

    public CommonProperties getCommonProperties() {
        return commonProperties;
    }

    public DataSourceProperties setCommonProperties(CommonProperties commonProperties) {
        this.commonProperties = commonProperties;
        return this;
    }

    public Map<String, ShardHost> getHostMap() {
        return hostMap;
    }

    public DataSourceProperties setHostMap(Map<String, ShardHost> hostMap) {
        this.hostMap = hostMap;
        return this;
    }

    public static class CommonProperties {
        private String type;
        private String platform;
        private String driverClassName;
        private Integer initialSize;
        private Integer maxActive;
        private Integer maxIdle;
        private Integer minIdle;

        public String getType() {
            return type;
        }

        public CommonProperties setType(String type) {
            this.type = type;
            return this;
        }

        public String getPlatform() {
            return platform;
        }

        public CommonProperties setPlatform(String platform) {
            this.platform = platform;
            return this;
        }

        public String getDriverClassName() {
            return driverClassName;
        }

        public CommonProperties setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
            return this;
        }

        public Integer getInitialSize() {
            return initialSize;
        }

        public CommonProperties setInitialSize(Integer initialSize) {
            this.initialSize = initialSize;
            return this;
        }

        public Integer getMaxActive() {
            return maxActive;
        }

        public CommonProperties setMaxActive(Integer maxActive) {
            this.maxActive = maxActive;
            return this;
        }

        public Integer getMaxIdle() {
            return maxIdle;
        }

        public CommonProperties setMaxIdle(Integer maxIdle) {
            this.maxIdle = maxIdle;
            return this;
        }

        public Integer getMinIdle() {
            return minIdle;
        }

        public CommonProperties setMinIdle(Integer minIdle) {
            this.minIdle = minIdle;
            return this;
        }
    }

    public static class ShardHost {
        private String url;
        private String username;
        private String password;

        public String getUrl() {
            return url;
        }

        public ShardHost setUrl(String url) {
            this.url = url;
            return this;
        }

        public String getUsername() {
            return username;
        }

        public ShardHost setUsername(String username) {
            this.username = username;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public ShardHost setPassword(String password) {
            this.password = password;
            return this;
        }
    }

}