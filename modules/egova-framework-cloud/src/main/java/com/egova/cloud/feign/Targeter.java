package com.egova.cloud.feign;

import feign.Feign;
import feign.Target;

/**
 * @author Spencer Gibb
 */
interface Targeter {
    <T> T target(FeignClientFactoryBean factory, Feign.Builder feign, FeignContext context,
                 Target.HardCodedTarget<T> target);
}
