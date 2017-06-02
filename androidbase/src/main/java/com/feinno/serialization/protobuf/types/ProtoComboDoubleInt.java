/*
 * FAE, Feinno App Engine
 *  
 * Create by Fred 2011-4-17
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.serialization.protobuf.types;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * {在这里补充类的功能说明}
 * 
 * @auther Fred
 */
public class ProtoComboDoubleInt extends ProtoEntity {
	@ProtoMember(1)
	private int int1;
	@ProtoMember(2)
	private int int2;
	
	
	public ProtoComboDoubleInt()
	{
	}

	public ProtoComboDoubleInt(int v1, int v2)
	{
		this.int1 = v1;
		this.int2 = v2;
	}

	@Override
	public boolean equals(Object obj)
	{

		ProtoComboDoubleInt rval = (ProtoComboDoubleInt)obj;
		if (obj == null) {
			return false;
		} else {
			return this.int1== rval.int1  && int2 == rval.int2;
		}
	}
	
	@Override
	public int hashCode()
	{
		return int1  ^ int2;
	}

	public int getStr1() {
		return int1;
	}

	public void setStr1(int str1) {
		this.int1 = str1;
	}

	public int getStr2() {
		return int2;
	}

	public void setStr2(int str2) {
		this.int2 = str2;
	}

	public int getInt1() {
		return int1;
	}

	public void setInt1(int int1) {
		this.int1 = int1;
	}

	public int getInt2() {
		return int2;
	}

	public void setInt2(int int2) {
		this.int2 = int2;
	}
	
}
