package com.egova.cloud.feign;

import org.springframework.cloud.context.named.NamedContextFactory;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Dave Syer
 * @author Gregor Zurowski
 */
class FeignClientSpecification implements NamedContextFactory.Specification {

    private String name;

    private Class<?>[] configuration;

    public FeignClientSpecification() {
    }

    public FeignClientSpecification(String name, Class<?>[] configuration) {
        this.name = name;
        this.configuration = configuration;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Class<?>[] getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Class<?>[] configuration) {
        this.configuration = configuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeignClientSpecification that = (FeignClientSpecification) o;
        return Objects.equals(name, that.name) &&
                Arrays.equals(configuration, that.configuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, configuration);
    }

    @Override
    public String toString() {
        return "FeignClientSpecification{" +
                "name='" + name + "', " +
                "configuration=" + Arrays.toString(configuration) +
                "}";
    }

}
