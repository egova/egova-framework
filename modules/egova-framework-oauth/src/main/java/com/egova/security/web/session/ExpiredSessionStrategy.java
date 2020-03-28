package com.egova.security.web.session;

import com.egova.security.core.properties.BrowserProperties;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import java.io.IOException;

public class ExpiredSessionStrategy extends AbstractSessionStrategy implements SessionInformationExpiredStrategy
{


	public ExpiredSessionStrategy(BrowserProperties browserProperties) {
		super(browserProperties);
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.web.session.SessionInformationExpiredStrategy#onExpiredSessionDetected(org.springframework.security.web.session.SessionInformationExpiredEvent)
	 */
	@Override
	public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException
	{
		onSessionInvalid(event.getRequest(), event.getResponse());
	}

	/* (non-Javadoc)
	 * @see com.imooc.security.browser.session.AbstractSessionStrategy#isConcurrency()
	 */
	@Override
	protected boolean isConcurrency() {
		return true;
	}

}