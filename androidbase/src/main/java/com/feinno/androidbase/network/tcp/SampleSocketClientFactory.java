package com.feinno.androidbase.network.tcp;

public class SampleSocketClientFactory {

	public static final String NIO = "NIO";
	public static final String BIO = "BIO";

	public static ISocketClentEvent createSocketClient(String type) {
		if (NIO.equalsIgnoreCase(type))
			return new SocketNioClient();
		else if (BIO.equalsIgnoreCase(type))
			return new SocketClient();
		return null;
	}
}
