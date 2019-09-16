package com.sasitha.springavrowebflux.config.datasource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@ConfigurationProperties(prefix = "org.multitenant")
public class MultiTenantDataSourceConfig {
    private List<DataSourceConfig> dataSources;
}
