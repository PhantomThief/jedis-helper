package com.github.phantomthief.jedis;

import com.github.phantomthief.jedis.proto.Op;
import com.github.phantomthief.jedis.proto.OpType;
import com.github.phantomthief.jedis.proto.StringSetOp;
import com.google.protobuf.InvalidProtocolBufferException;

import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.PipelineBase;
import redis.clients.jedis.Response;

/**
 * @author w.vela
 * Created on 2017-03-02.
 */
public abstract class ProtoUtils {

    private ProtoUtils() {
        throw new UnsupportedOperationException();
    }

    public static Op serializeSetString(long requestTime, long responseTime, String key,
            String value) {
        return Op.newBuilder() //
                .setRequestTime(requestTime) //
                .setResponseTime(responseTime) //
                .setType(OpType.STRING_SET) //
                .setPayload(StringSetOp.newBuilder() //
                        .setKey(key) //
                        .setValue(value) //
                        .build().toByteString()) //
                .build();
    }

    public static <P extends PipelineBase> Response<?> executePipeline(P pipeline, Op op)
            throws InvalidProtocolBufferException {
        switch (op.getType()) {
            case UNKNOWN:
                break;
            case STRING_SET:
                StringSetOp stringSetOp = StringSetOp.parseFrom(op.getPayload().toByteArray());
                return pipeline.set(stringSetOp.getKey(), stringSetOp.getValue());
            case UNRECOGNIZED:
                break;
        }
        return null;
    }

    public static <J extends JedisCommands & BinaryJedisCommands> Object executeOps(J jedis, Op op)
            throws InvalidProtocolBufferException {
        switch (op.getType()) {
            case UNKNOWN:
                break;
            case STRING_SET:
                StringSetOp stringSetOp = StringSetOp.parseFrom(op.getPayload().toByteArray());
                return jedis.set(stringSetOp.getKey(), stringSetOp.getValue());
            case UNRECOGNIZED:
                break;
        }
        return null;
    }
}
