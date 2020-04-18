package com.egova.security.core.provider;

import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDetailsExecutor {
    private ApplicationContext applicationContext;

    public UserDetailsExecutor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private Iterable<UserDetailsProvider> getServices() {
        Map<String, UserDetailsProvider> map = applicationContext.getBeansOfType(UserDetailsProvider.class);
        List<UserDetailsProvider> list = new ArrayList<>(map.values());
        AnnotationAwareOrderComparator.sort(list);
        return list;
    }


    public UserDetails execute(String username) {

        for (UserDetailsProvider provider : getServices()) {
            UserDetails clientDetails = provider.loadUserByUsername(username);
            if (clientDetails != null) {
                return clientDetails;
            }
        }
        return null;
    }
}
