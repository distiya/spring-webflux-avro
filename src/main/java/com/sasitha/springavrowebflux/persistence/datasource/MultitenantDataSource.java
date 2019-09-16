package com.sasitha.springavrowebflux.persistence.datasource;

import com.sasitha.springavrowebflux.context.TenantContext;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MultitenantDataSource extends AbstractRoutingDataSource {
    @Override
    protected String determineCurrentLookupKey() {
        return TenantContext.getTenantId();
    }
}
