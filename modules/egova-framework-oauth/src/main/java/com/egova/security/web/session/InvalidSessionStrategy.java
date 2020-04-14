package com.egova.security.web.session;


import com.egova.json.JsonMapping;
import com.egova.security.core.properties.BrowserProperties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class InvalidSessionStrategy extends AbstractSessionStrategy implements org.springframework.security.web.session.InvalidSessionStrategy {

    public InvalidSessionStrategy(JsonMapping jsonMapping, BrowserProperties browserProperties) {
        super(jsonMapping, browserProperties);
    }

    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        onSessionInvalid(request, response);
    }

}