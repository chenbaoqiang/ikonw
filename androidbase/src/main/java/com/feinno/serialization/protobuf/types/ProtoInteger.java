/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-3-10
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.serialization.protobuf.types;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;
/**
 * int类型数据序列化
 * 
 * @author 高磊 gaolei@feinno.com
 */

public class ProtoInteger extends ProtoEntity
{
	@ProtoMember(1)
	private int value;
	
	public ProtoInteger()
	{
	}
	
	public ProtoInteger(int value)
	{
		this.value = value; 
	}

	public int getValue()
	{
		return value;
	}

	public void setValue(int value)
	{
		this.value = value;
	}
}
