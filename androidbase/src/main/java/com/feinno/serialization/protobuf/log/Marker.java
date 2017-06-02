package com.feinno.serialization.protobuf.log;

/*jadclipse*/// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.

import java.io.Serializable;
import java.util.Iterator;

public interface Marker extends Serializable {

	public abstract String getName();

	public abstract void add(Marker marker);

	public abstract boolean remove(Marker marker);

	/**
	 * @deprecated Method hasChildren is deprecated
	 */

	public abstract boolean hasChildren();

	public abstract boolean hasReferences();

	public abstract Iterator iterator();

	public abstract boolean contains(Marker marker);

	public abstract boolean contains(String s);

	public abstract boolean equals(Object obj);

	public abstract int hashCode();

	public static final String ANY_MARKER = "*";
	public static final String ANY_NON_NULL_MARKER = "+";
}

/*
 * DECOMPILATION REPORT
 * 
 * Decompiled from:
 * /home/lvmingwei/.m2/repository/org/slf4j/slf4j-api/1.6.2/slf4j-api-1.6.2.jar
 * Total time: 8 ms Jad reported messages/errors: Exit status: 0 Caught
 * exceptions:
 */
