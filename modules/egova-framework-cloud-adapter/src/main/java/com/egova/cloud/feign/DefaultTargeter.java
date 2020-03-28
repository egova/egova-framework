package com.egova.cloud.feign;

import feign.Feign;
import feign.Target;

/**
 * @author Spencer Gibb
 */
class DefaultTargeter implements Targeter {

    @Override
    public <T> T target(FeignClientFactoryBean factory, Feign.Builder feign, FeignContext context,
                        Target.HardCodedTarget<T> target) {
        return feign.target(target);
    }
}
