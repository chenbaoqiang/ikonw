package com.feinno.serialization.protobuf.log;

public interface Logger {

	public abstract String getName();

	public abstract boolean isTraceEnabled();

	public abstract void trace(String s);

	public abstract void trace(String s, Object obj);

	public abstract void trace(String s, Object obj, Object obj1);

	public abstract void trace(String s, Object aobj[]);

	public abstract void trace(String s, Throwable throwable);

	public abstract boolean isTraceEnabled(Marker marker);

	public abstract void trace(Marker marker, String s);

	public abstract void trace(Marker marker, String s, Object obj);

	public abstract void trace(Marker marker, String s, Object obj, Object obj1);

	public abstract void trace(Marker marker, String s, Object aobj[]);

	public abstract void trace(Marker marker, String s, Throwable throwable);

	public abstract boolean isDebugEnabled();

	public abstract void debug(String s);

	public abstract void debug(String s, Object obj);

	public abstract void debug(String s, Object obj, Object obj1);

	public abstract void debug(String s, Object aobj[]);

	public abstract void debug(String s, Throwable throwable);

	public abstract boolean isDebugEnabled(Marker marker);

	public abstract void debug(Marker marker, String s);

	public abstract void debug(Marker marker, String s, Object obj);

	public abstract void debug(Marker marker, String s, Object obj, Object obj1);

	public abstract void debug(Marker marker, String s, Object aobj[]);

	public abstract void debug(Marker marker, String s, Throwable throwable);

	public abstract boolean isInfoEnabled();

	public abstract void info(String s);

	public abstract void info(String s, Object obj);

	public abstract void info(String s, Object obj, Object obj1);

	public abstract void info(String s, Object aobj[]);

	public abstract void info(String s, Throwable throwable);

	public abstract boolean isInfoEnabled(Marker marker);

	public abstract void info(Marker marker, String s);

	public abstract void info(Marker marker, String s, Object obj);

	public abstract void info(Marker marker, String s, Object obj, Object obj1);

	public abstract void info(Marker marker, String s, Object aobj[]);

	public abstract void info(Marker marker, String s, Throwable throwable);

	public abstract boolean isWarnEnabled();

	public abstract void warn(String s);

	public abstract void warn(String s, Object obj);

	public abstract void warn(String s, Object aobj[]);

	public abstract void warn(String s, Object obj, Object obj1);

	public abstract void warn(String s, Throwable throwable);

	public abstract boolean isWarnEnabled(Marker marker);

	public abstract void warn(Marker marker, String s);

	public abstract void warn(Marker marker, String s, Object obj);

	public abstract void warn(Marker marker, String s, Object obj, Object obj1);

	public abstract void warn(Marker marker, String s, Object aobj[]);

	public abstract void warn(Marker marker, String s, Throwable throwable);

	public abstract boolean isErrorEnabled();

	public abstract void error(String s);

	public abstract void error(String s, Object obj);

	public abstract void error(String s, Object obj, Object obj1);

	public abstract void error(String s, Object aobj[]);

	public abstract void error(String s, Throwable throwable);

	public abstract boolean isErrorEnabled(Marker marker);

	public abstract void error(Marker marker, String s);

	public abstract void error(Marker marker, String s, Object obj);

	public abstract void error(Marker marker, String s, Object obj, Object obj1);

	public abstract void error(Marker marker, String s, Object aobj[]);

	public abstract void error(Marker marker, String s, Throwable throwable);

	public static final String ROOT_LOGGER_NAME = "ROOT";
}
