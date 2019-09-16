package com.sasitha.springavrowebflux.filter;

import com.sasitha.springavrowebflux.constant.Constants;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class MultiTenantFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        return webFilterChain.filter(serverWebExchange).subscriberContext(ctx->ctx.put(Constants.DEFAULT_COUNTRY_HEADER,serverWebExchange.getRequest().getHeaders().get(Constants.DEFAULT_COUNTRY_HEADER)));
    }
}
