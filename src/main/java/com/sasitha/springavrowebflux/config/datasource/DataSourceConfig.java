package com.sasitha.springavrowebflux.config.datasource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataSourceConfig {
    private String countryCode;
    private String url;
    private String userName;
    private String password;
    private int maxPoolSize;
    private int minIdle;
}
