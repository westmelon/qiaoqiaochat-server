package com.neo.qiaoqiaochat.web.config.redis;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class RedisObjectSerializer implements RedisSerializer<Object> {
    private Converter<Object, byte[]> serializer = new SerializingConverter();
    private Converter<byte[], Object> deserializer = new DeserializingConverter();
    static final byte[] EMPTY_ARRAY = new byte[0];

    public RedisObjectSerializer() {
    }

    public Object deserialize(byte[] bytes) {
        if (this.isEmpty(bytes)) {
            return null;
        } else {
            try {
                return this.deserializer.convert(bytes);
            } catch (Exception var3) {
                throw new SerializationException("Cannot deserialize", var3);
            }
        }
    }

    public byte[] serialize(Object object) {
         if (object == null) {
            return EMPTY_ARRAY;
        } else {
            try {
                return (byte[])this.serializer.convert(object);
            } catch (Exception var3) {
                return EMPTY_ARRAY;
            }
        }
    }

    private boolean isEmpty(byte[] data) {
        return data == null || data.length == 0;
    }
}
