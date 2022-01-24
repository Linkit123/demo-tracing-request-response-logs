package com.dvtt.demo.coredemo.thread;

import com.dvtt.demo.coredemo.context.LoggingContext;

public class ThreadContextKeeper {

    private static final ThreadLocal<LoggingContext> threadLocalValue = ThreadLocal.withInitial(LoggingContext::new);

    public static LoggingContext getRequestAttributes() {
        return threadLocalValue.get();
    }

}
