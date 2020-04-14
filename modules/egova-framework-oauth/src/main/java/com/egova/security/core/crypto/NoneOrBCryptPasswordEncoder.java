package com.egova.security.core.crypto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Slf4j
public class NoneOrBCryptPasswordEncoder implements PasswordEncoder
{

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	public String encode(CharSequence rawPassword)
	{
		return passwordEncoder.encode(rawPassword);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if (encodedPassword == null || encodedPassword.length() == 0) {
			log.warn("Empty encoded password");
			return false;
		}

		if (rawPassword.toString().equals(encodedPassword)) {
			return true;
		}


		return passwordEncoder.matches(rawPassword, encodedPassword) || passwordEncoder.matches(encodedPassword, rawPassword.toString());
	}
}
