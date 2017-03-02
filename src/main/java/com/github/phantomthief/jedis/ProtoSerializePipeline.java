package com.github.phantomthief.jedis;

import static com.github.phantomthief.jedis.ProtoUtils.serializeSetString;
import static java.lang.System.currentTimeMillis;

import com.github.phantomthief.jedis.proto.PipelineOp;

import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

/**
 * @author w.vela
 * Created on 2017-03-02.
 */
public class ProtoSerializePipeline<T extends Pipeline> extends ForwardingPipeline<T> {

    private final PipelineOp.Builder proto = PipelineOp.newBuilder();

    public ProtoSerializePipeline(T pipeline) {
        super(pipeline);
    }

    @Override
    public Response<String> set(String s, String s1) {
        Response<String> set = super.set(s, s1);
        proto.addOp(serializeSetString(currentTimeMillis(), 0, s, s1));
        return set;
    }

    @Override
    public void sync() {
        super.sync();
        proto.setReponseTime(currentTimeMillis());
    }

    public PipelineOp.Builder getProto() {
        return proto;
    }
}
