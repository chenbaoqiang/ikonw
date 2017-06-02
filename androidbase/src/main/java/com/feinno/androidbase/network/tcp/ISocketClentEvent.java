package com.feinno.androidbase.network.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;

public interface ISocketClentEvent {

	void connect() throws IOException;

	void close();

	void send(byte[] bytes) throws IOException;

	void setAddress(InetSocketAddress address);
	
	boolean isConnected();
	
	void registerReadEvent(IConnectionReadEvent event);
	
}
