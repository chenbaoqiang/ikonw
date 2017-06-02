package com.feinno.serialization.protobuf.log;


public class LoggerFactory {

	public static Logger getLogger(Class<?> clazz) {
		return SimpleLoggerFactory.INSTANCE.getLogger(clazz.getName());
	}

}
