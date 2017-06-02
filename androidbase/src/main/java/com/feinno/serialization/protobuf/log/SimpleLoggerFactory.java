/* 
 * Copyright (c) 2004-2005 SLF4J.ORG
 * Copyright (c) 2004-2005 QOS.ch
 *
 * All rights reserved.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute, and/or sell copies of  the Software, and to permit persons
 * to whom  the Software is furnished  to do so, provided  that the above
 * copyright notice(s) and this permission notice appear in all copies of
 * the  Software and  that both  the above  copyright notice(s)  and this
 * permission notice appear in supporting documentation.
 * 
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR  A PARTICULAR PURPOSE AND NONINFRINGEMENT
 * OF  THIRD PARTY  RIGHTS. IN  NO EVENT  SHALL THE  COPYRIGHT  HOLDER OR
 * HOLDERS  INCLUDED IN  THIS  NOTICE BE  LIABLE  FOR ANY  CLAIM, OR  ANY
 * SPECIAL INDIRECT  OR CONSEQUENTIAL DAMAGES, OR  ANY DAMAGES WHATSOEVER
 * RESULTING FROM LOSS  OF USE, DATA OR PROFITS, WHETHER  IN AN ACTION OF
 * CONTRACT, NEGLIGENCE  OR OTHER TORTIOUS  ACTION, ARISING OUT OF  OR IN
 * CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 * 
 * Except as  contained in  this notice, the  name of a  copyright holder
 * shall not be used in advertising or otherwise to promote the sale, use
 * or other dealings in this Software without prior written authorization
 * of the copyright holder.
 *
 */

package com.feinno.serialization.protobuf.log;

import java.util.HashMap;
import java.util.Map;

import com.feinno.serialization.protobuf.log.Logger;

/**
 * An implementation of {@link ILoggerFactory} which always returns
 * {@link SimpleLogger} instances.
 * 
 * @author Ceki G&uuml;lc&uuml;
 */
public class SimpleLoggerFactory {
	public static final SimpleLoggerFactory INSTANCE = new SimpleLoggerFactory();

	Map<String, Logger> loggerMap;

	private boolean infoEnable;
	private boolean warnEnable;
	private boolean errorEnable;

	public SimpleLoggerFactory() {
		loggerMap = new HashMap<String, Logger>();
		infoEnable = true;
		warnEnable = true;
		errorEnable = true;
	}

	public boolean isInfoEnable() {
		return infoEnable;
	}

	public void setInfoEnable(boolean infoEnable) {
		this.infoEnable = infoEnable;
	}

	public boolean isWarnEnable() {
		return warnEnable;
	}

	public void setWarnEnable(boolean warnEnable) {
		this.warnEnable = warnEnable;
	}

	public boolean isErrorEnable() {
		return errorEnable;
	}

	public void setErrorEnable(boolean errorEnable) {
		this.errorEnable = errorEnable;
	}

	/**
	 * Return an appropriate {@link SimpleLogger} instance by name.
	 */
	public Logger getLogger(String name) {
		Logger slogger = null;
		// protect against concurrent access of the loggerMap
		synchronized (this) {
			slogger = loggerMap.get(name);
			if (slogger == null) {
				slogger = new SimpleLogger(name);
				loggerMap.put(name, slogger);
			}
		}
		return slogger;
	}
}
