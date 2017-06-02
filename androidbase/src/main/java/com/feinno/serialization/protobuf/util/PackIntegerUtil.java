package com.feinno.serialization.protobuf.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.feinno.serialization.protobuf.CodedInputStream;
import com.feinno.serialization.protobuf.CodedOutputStream;

/**
 * 为配合Protobuf中得[packed=true]关键字得用法，特提供此工具，用于将字节数组转换成数字数组或从数字数组转换成字节数组
 * 
 * @author lvmingwei
 * 
 */
public class PackIntegerUtil {

	public static List<Integer> convert(byte[] buffer) throws IOException {
		List<Integer> integers = new ArrayList<Integer>();
		if (buffer == null || buffer.length == 0) {
			return integers;
		}
		CodedInputStream inputStream = CodedInputStream.newInstance(buffer);
		while (!inputStream.isAtEnd()) {
			integers.add(inputStream.readInt32());

		}
		return integers;
	}

	public static byte[] covert(List<Integer> integers) throws IOException {
		if (integers == null || integers.size() == 0) {
			return new byte[0];
		}
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		CodedOutputStream outputStream = CodedOutputStream.newInstance(byteArrayOutputStream);
		for (Integer integer : integers) {
			outputStream.writeInt32NoTag(integer);
		}
		return byteArrayOutputStream.toByteArray();
	}

}
