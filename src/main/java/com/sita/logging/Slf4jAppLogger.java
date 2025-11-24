package com.sita.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jAppLogger implements AppLogger {

    private final Logger logger;

    public Slf4jAppLogger(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    @Override
    public void debug(String msg, Object... args) {
        logger.debug(msg, args);
    }

    @Override
    public void info(String msg, Object... args) {
        logger.info(msg, args);
    }

    @Override
    public void warn(String msg, Object... args) {
        logger.warn(msg, args);
    }

    @Override
    public void error(String msg, Object... args) {
        logger.error(msg, args);
    }
}

