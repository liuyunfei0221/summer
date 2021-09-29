package com.blue.gateway.config.deploy;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * circuitBreaker deploy for resilience4j
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "circuit")
public class CircuitBreakerDeploy {

    private Integer failureRateThreshold;

    private Integer slowCallRateThreshold;

    private Integer slowCallDurationThresholdMillis;

    private Integer permittedNumberOfCallsInHalfOpenState;

    private Integer maxWaitDurationInHalfOpenStateMillis;

    private CircuitBreakerConfig.SlidingWindowType slidingWindowType;

    private Integer slidingWindowSize;

    private Integer minimumNumberOfCalls;

    private Integer waitDurationInOpenStateMillis;

    private Boolean automaticTransitionFromOpenToHalfOpenEnabled;

    private String recordFailurePredicateClassName;

    public CircuitBreakerDeploy() {
    }

    public Integer getFailureRateThreshold() {
        return failureRateThreshold;
    }

    public void setFailureRateThreshold(Integer failureRateThreshold) {
        this.failureRateThreshold = failureRateThreshold;
    }

    public Integer getSlowCallRateThreshold() {
        return slowCallRateThreshold;
    }

    public void setSlowCallRateThreshold(Integer slowCallRateThreshold) {
        this.slowCallRateThreshold = slowCallRateThreshold;
    }

    public Integer getSlowCallDurationThresholdMillis() {
        return slowCallDurationThresholdMillis;
    }

    public void setSlowCallDurationThresholdMillis(Integer slowCallDurationThresholdMillis) {
        this.slowCallDurationThresholdMillis = slowCallDurationThresholdMillis;
    }

    public Integer getPermittedNumberOfCallsInHalfOpenState() {
        return permittedNumberOfCallsInHalfOpenState;
    }

    public void setPermittedNumberOfCallsInHalfOpenState(Integer permittedNumberOfCallsInHalfOpenState) {
        this.permittedNumberOfCallsInHalfOpenState = permittedNumberOfCallsInHalfOpenState;
    }

    public Integer getMaxWaitDurationInHalfOpenStateMillis() {
        return maxWaitDurationInHalfOpenStateMillis;
    }

    public void setMaxWaitDurationInHalfOpenStateMillis(Integer maxWaitDurationInHalfOpenStateMillis) {
        this.maxWaitDurationInHalfOpenStateMillis = maxWaitDurationInHalfOpenStateMillis;
    }

    public CircuitBreakerConfig.SlidingWindowType getSlidingWindowType() {
        return slidingWindowType;
    }

    public void setSlidingWindowType(CircuitBreakerConfig.SlidingWindowType slidingWindowType) {
        this.slidingWindowType = slidingWindowType;
    }

    public Integer getSlidingWindowSize() {
        return slidingWindowSize;
    }

    public void setSlidingWindowSize(Integer slidingWindowSize) {
        this.slidingWindowSize = slidingWindowSize;
    }

    public Integer getMinimumNumberOfCalls() {
        return minimumNumberOfCalls;
    }

    public void setMinimumNumberOfCalls(Integer minimumNumberOfCalls) {
        this.minimumNumberOfCalls = minimumNumberOfCalls;
    }

    public Integer getWaitDurationInOpenStateMillis() {
        return waitDurationInOpenStateMillis;
    }

    public void setWaitDurationInOpenStateMillis(Integer waitDurationInOpenStateMillis) {
        this.waitDurationInOpenStateMillis = waitDurationInOpenStateMillis;
    }

    public Boolean getAutomaticTransitionFromOpenToHalfOpenEnabled() {
        return automaticTransitionFromOpenToHalfOpenEnabled;
    }

    public void setAutomaticTransitionFromOpenToHalfOpenEnabled(Boolean automaticTransitionFromOpenToHalfOpenEnabled) {
        this.automaticTransitionFromOpenToHalfOpenEnabled = automaticTransitionFromOpenToHalfOpenEnabled;
    }

    public String getRecordFailurePredicateClassName() {
        return recordFailurePredicateClassName;
    }

    public void setRecordFailurePredicateClassName(String recordFailurePredicateClassName) {
        this.recordFailurePredicateClassName = recordFailurePredicateClassName;
    }
}
