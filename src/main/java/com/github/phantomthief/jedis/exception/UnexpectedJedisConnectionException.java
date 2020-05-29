package com.github.phantomthief.jedis.exception;

import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * @author w.vela
 * Created on 2020-05-29.
 */
public class UnexpectedJedisConnectionException extends JedisConnectionException {

    public UnexpectedJedisConnectionException(Throwable cause) {
        super(cause);
    }
}
