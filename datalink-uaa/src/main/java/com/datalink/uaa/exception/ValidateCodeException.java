package com.datalink.uaa.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * ValidateCodeException
 *
 * @author wenmo
 * @since 2021/5/11
 */
public class ValidateCodeException extends AuthenticationException {
	private static final long serialVersionUID = -7285211528095468156L;

	public ValidateCodeException(String msg) {
		super(msg);
	}
}
