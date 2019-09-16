package com.sasitha.springavrowebflux.avro;

import org.apache.kafka.common.errors.SerializationException;

public interface Serializer<T> {

    /**
     * Serialize object as byte array.
     * @param T data the object to serialize
     * @return byte[]
     */
    byte[] serialize(T data) throws SerializationException;

}