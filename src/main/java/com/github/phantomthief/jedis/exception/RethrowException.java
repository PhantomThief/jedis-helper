package com.github.phantomthief.jedis.exception;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

/**
 * @author w.vela
 * Created on 2017-12-05.
 */
public class RethrowException extends Exception {

    private final RuntimeException throwable;

    public RethrowException(@Nonnull RuntimeException throwable) {
        this.throwable = checkNotNull(throwable);
    }

    public RuntimeException getThrowable() {
        return throwable;
    }
}
