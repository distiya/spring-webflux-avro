package com.sasitha.springavrowebflux.avro;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.errors.SerializationException;

public interface Deserializer<T extends SpecificRecordBase> {

    /**
     * Deserialize object from a byte array.
     * @param Class<? extends T> clazz the expected class for the deserialized object
     * @param byte[] data the byte array
     * @param clazz
     * @return T object instance
     */
    T deserialize(Class<T> clazz, byte[] data) throws SerializationException;

}
