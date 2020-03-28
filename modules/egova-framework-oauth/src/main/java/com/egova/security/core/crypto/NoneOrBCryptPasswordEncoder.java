package com.egova.security.core.crypto;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class NoneOrBCryptPasswordEncoder implements PasswordEncoder
{
	private final Log logger = LogFactory.getLog(getClass());

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	@Override
	public String encode(CharSequence rawPassword)
	{
		return passwordEncoder.encode(rawPassword);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if (encodedPassword == null || encodedPassword.length() == 0) {
			logger.warn("Empty encoded password");
			return false;
		}

		if (rawPassword.toString().equals(encodedPassword)) {
			return true;
		}


		return passwordEncoder.matches(rawPassword, encodedPassword) || passwordEncoder.matches(encodedPassword, rawPassword.toString());
	}
}
