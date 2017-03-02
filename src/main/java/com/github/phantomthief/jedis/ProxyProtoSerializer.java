package com.github.phantomthief.jedis;

import static com.github.phantomthief.jedis.ProtoUtils.serializeSetString;
import static com.google.common.base.Throwables.propagate;
import static java.lang.System.currentTimeMillis;

import java.lang.reflect.Method;
import java.util.Objects;

import com.github.phantomthief.jedis.proto.Op;

import redis.clients.jedis.JedisCommands;

/**
 * @author w.vela <wangtianzhou@kuaishou.com>
 * Created on 2017-03-02.
 */
public class ProxyProtoSerializer {

    private static final Method SET_STRING;
    static {
        try {
            SET_STRING = JedisCommands.class.getMethod("set", String.class, String.class);
        } catch (NoSuchMethodException e) {
            throw propagate(e);
        }
    }

    public static Op serialize(long requestTime, Method method, Object[] args) {
        if (Objects.equals(method, SET_STRING)) {
            return serializeSetString(requestTime, currentTimeMillis(), (String) args[0],
                    (String) args[1]);
        }
        return null;
    }
}
