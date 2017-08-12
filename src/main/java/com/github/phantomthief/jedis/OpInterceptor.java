package com.github.phantomthief.jedis;

import java.io.Closeable;
import java.lang.reflect.Method;

import javax.annotation.Nonnull;

/**
 * @author w.vela
 * Created on 2017-08-11.
 */
public interface OpInterceptor<J extends Closeable, P> {

    JedisOpCall<J> interceptCall(P pool, Method method, J jedis, Object[] args);

    class JedisOpCall<J extends Closeable> {

        private final Method method;
        private final J jedis;
        private final Object[] args;

        private Object finalObject;
        private boolean hasFinalObject;

        public JedisOpCall(@Nonnull Method method, @Nonnull J jedis, @Nonnull Object[] args) {
            this.method = method;
            this.jedis = jedis;
            this.args = args;
        }

        boolean hasFinalObject() {
            return hasFinalObject;
        }

        Object getFinalObject() {
            return finalObject;
        }

        public JedisOpCall<J> setFinalObject(Object obj) {
            this.finalObject = obj;
            hasFinalObject = true;
            return this;
        }

        Method getMethod() {
            return method;
        }

        J getJedis() {
            return jedis;
        }

        Object[] getArgs() {
            return args;
        }
    }
}
