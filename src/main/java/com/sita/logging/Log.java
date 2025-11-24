package com.sita.logging;

public class Log {
	public static AppLogger get(Class<?> clazz) {
        return new Slf4jAppLogger(clazz);
    }
}
