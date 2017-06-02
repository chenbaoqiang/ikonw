/*
 * FAE, Feinno App Engine
 *  
 * Create by wanglihui 2011-3-14
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.serialization.protobuf.types;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * string类型数据序列化
 * 
 * @auther wanglihui
 */
public class ProtoString extends ProtoEntity
{
	@ProtoMember(1)
	private String value;

	public ProtoString()
	{
	}
	
	public ProtoString(String string)
	{
		this.value = string;
	}

	/**
	 * @return the value
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value)
	{
		this.value = value;
	}

}
