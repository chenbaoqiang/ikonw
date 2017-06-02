package com.feinno.androidbase.network.tcp;

import java.io.InputStream;
import java.nio.ByteBuffer;

public interface IConnectionReadEvent {
	void onConnectionRead(InputStream inputStream);
	void onConnectionNioRead(ByteBuffer buffer);
}
