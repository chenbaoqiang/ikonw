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
 * short类型数据序列化
 * 
 * @auther wanglihui
 */
public class ProtoShort extends ProtoEntity {
	
	@ProtoMember(1)
	private short value;

	/**
	 * @return the value
	 */
	public short getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(short value) {
		this.value = value;
	}
}
