package com.sasitha.springavrowebflux.avro;

import org.apache.avro.specific.SpecificRecordBase;
import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.Decoder;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.util.MimeType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AvroHttpMessageDecoder<T extends SpecificRecordBase> implements Decoder<T> {

    private Deserializer<T> deserializer;
    private boolean useBinaryEncoding;
    private final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    public AvroHttpMessageDecoder(boolean useBinaryEncoding){
        deserializer = new AvroDeserializer<>(useBinaryEncoding);
        this.useBinaryEncoding = useBinaryEncoding;
    }

    @Override
    public boolean canDecode(ResolvableType resolvableType, MimeType mimeType) {
        MimeType type = useBinaryEncoding?new MimeType("application","avro",DEFAULT_CHARSET):new MimeType("application","avro+json",DEFAULT_CHARSET);
        return mimeType!=null?(mimeType.getType().equals(type.getType()) && mimeType.getSubtype().equals(type.getSubtype()) && resolvableType.getRawClass().getSuperclass().equals(SpecificRecordBase.class)):false;
    }

    @Override
    public Flux<T> decode(Publisher<DataBuffer> publisher, ResolvableType resolvableType, MimeType mimeType, Map<String, Object> map) {
        return Flux.from(publisher)
                .map(de->{
                    ByteBuffer byteBuffer = de.asByteBuffer();
                    byte[] b = new byte[byteBuffer.remaining()];
                    byteBuffer.get(b);
                    return b;
                })
                .map(d->deserializer.deserialize((Class<T>)resolvableType.getRawClass(),d));
    }

    @Override
    public Mono<T> decodeToMono(Publisher<DataBuffer> publisher, ResolvableType resolvableType, MimeType mimeType, Map<String, Object> map) {
        return Mono.from(publisher)
                .map(de->{
                    ByteBuffer byteBuffer = de.asByteBuffer();
                    byte[] b = new byte[byteBuffer.remaining()];
                    byteBuffer.get(b);
                    return b;
                })
                .map(d->deserializer.deserialize((Class<T>)resolvableType.getRawClass(),d));
    }

    @Override
    public List<MimeType> getDecodableMimeTypes() {
        List<MimeType> acceptableMimeTypes = new ArrayList<>();
        MimeType type = useBinaryEncoding?new MimeType("application","avro",DEFAULT_CHARSET):new MimeType("application","avro+json",DEFAULT_CHARSET);
        acceptableMimeTypes.add(type);
        return acceptableMimeTypes;
    }
}
