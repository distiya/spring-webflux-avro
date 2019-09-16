package com.sasitha.springavrowebflux.config.spring;

import com.sasitha.springavrowebflux.config.datasource.MultiTenantDataSourceConfig;
import com.sasitha.springavrowebflux.constant.Constants;
import com.sasitha.springavrowebflux.context.TenantContext;
import com.sasitha.springavrowebflux.persistence.datasource.MultitenantDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Operators;
import reactor.util.context.Context;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Profile("!test")
public class SpringConfig {

    @Autowired
    private MultiTenantDataSourceConfig multiTenantDataSourceConfig;

    @PostConstruct
    public void setValueToThreadLocal(){
        Hooks.onEachOperator("MultiTenantContextResolver",Operators.lift((sc, sub) ->
                new CoreSubscriber<Object>(){
                    @Override
                    public void onSubscribe(Subscription s) {
                        sub.onSubscribe(s);
                    }

                    @Override
                    public void onNext(Object o) {
                        Context context = currentContext();
                        if(context.size()>0&&context.hasKey(Constants.DEFAULT_COUNTRY_HEADER)){
                            List<String> countryHeaderList = context.get(Constants.DEFAULT_COUNTRY_HEADER);
                            if(countryHeaderList!=null && countryHeaderList.size() == 1)
                                TenantContext.setTenant(countryHeaderList.get(0));
                            else
                                TenantContext.setTenant(Constants.DEFAULT_COUNTRY_CODE);
                        }
                        else
                            TenantContext.setTenant(Constants.DEFAULT_COUNTRY_CODE);
                        sub.onNext(o);
                    }

                    @Override
                    public void onError(Throwable t) {
                        sub.onError(t);
                    }

                    @Override
                    public void onComplete() {
                        sub.onComplete();
                    }

                    @Override
                    public Context currentContext() {
                        return sub.currentContext();
                    }
                }));
    }

    @PreDestroy
    public void cleanUpHook(){
        Hooks.resetOnEachOperator("MultiTenantContextResolver");
    }

    @Bean
    public DataSource clientDataSources(){
        Map<Object, Object> targetDataSources = new HashMap();
        MultitenantDataSource clientRoutingDataSource = new MultitenantDataSource();
        multiTenantDataSourceConfig.getDataSources().stream()
                .forEach(c->{
                    HikariDataSource dataSource = new HikariDataSource();
                    dataSource.setUsername(c.getUserName());
                    dataSource.setPassword(c.getPassword());
                    dataSource.setJdbcUrl(c.getUrl());
                    dataSource.setMaximumPoolSize(c.getMaxPoolSize());
                    dataSource.setMinimumIdle(c.getMinIdle());
                    targetDataSources.put(c.getCountryCode(),dataSource);
                    if(Constants.DEFAULT_COUNTRY_CODE.equals(c.getCountryCode())){
                        clientRoutingDataSource.setDefaultTargetDataSource(dataSource);
                        targetDataSources.put(null,dataSource);
                    }
                });
        clientRoutingDataSource.setTargetDataSources(targetDataSources);
        return clientRoutingDataSource;
    }


}
