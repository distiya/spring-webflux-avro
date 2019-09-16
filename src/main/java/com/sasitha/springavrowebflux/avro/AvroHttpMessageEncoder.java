package com.sasitha.springavrowebflux.avro;

import org.apache.avro.specific.SpecificRecordBase;
import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.Encoder;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.util.MimeType;
import reactor.core.publisher.Flux;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AvroHttpMessageEncoder<T extends SpecificRecordBase> implements Encoder<T>{

    private Serializer<SpecificRecordBase> serializer;
    private boolean useBinaryEncoding;
    private final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    public AvroHttpMessageEncoder(boolean useBinaryEncoding){
        serializer = new AvroSerializer<>(useBinaryEncoding);
        this.useBinaryEncoding = useBinaryEncoding;
    }

    @Override
    public boolean canEncode(ResolvableType resolvableType, MimeType mimeType) {
        MimeType type = useBinaryEncoding?new MimeType("application","avro",DEFAULT_CHARSET):new MimeType("application","avro+json",DEFAULT_CHARSET);
        return mimeType!=null?(mimeType.getType().equals(type.getType()) && mimeType.getSubtype().equals(type.getSubtype()) && resolvableType.getRawClass().getSuperclass().equals(SpecificRecordBase.class)):false;
    }

    @Override
    public Flux<DataBuffer> encode(Publisher<? extends T> publisher, DataBufferFactory dataBufferFactory, ResolvableType resolvableType, MimeType mimeType, Map<String, Object> map) {
        return Flux.from(publisher)
                .map(t-> dataBufferFactory.wrap(serializer.serialize((SpecificRecordBase) t)));
    }

    @Override
    public List<MimeType> getEncodableMimeTypes() {
        List<MimeType> acceptableMimeTypes = new ArrayList<>();
        MimeType type = useBinaryEncoding?new MimeType("application","avro",DEFAULT_CHARSET):new MimeType("application","avro+json",DEFAULT_CHARSET);
        acceptableMimeTypes.add(type);
        return acceptableMimeTypes;
    }
}
