package com.sasitha.springavrowebflux.config.spring;

import com.sasitha.springavrowebflux.avro.AvroHttpMessageDecoder;
import com.sasitha.springavrowebflux.avro.AvroHttpMessageEncoder;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class GlobalCodecConfig implements WebFluxConfigurer {
    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        configurer.customCodecs().encoder(new AvroHttpMessageEncoder<>(true));
        configurer.customCodecs().encoder(new AvroHttpMessageEncoder<>(false));
        configurer.customCodecs().decoder(new AvroHttpMessageDecoder<>(true));
        configurer.customCodecs().decoder(new AvroHttpMessageDecoder<>(false));
    }
}
