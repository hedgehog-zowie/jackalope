package org.jackalope.study.iplib;

/**
 * IP库异常类
 * @author Nicholas
 */
public class IpException extends Exception {

	private static final long serialVersionUID = -1774368533862063357L;

	public IpException(Throwable cause) {
		super(cause);
	}

	public IpException(String message) {
		super(message);
	}

	public IpException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
