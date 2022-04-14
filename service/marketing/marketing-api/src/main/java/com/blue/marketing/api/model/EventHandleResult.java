package com.blue.marketing.api.model;

import java.io.Serializable;

/**
 * event handling result
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class EventHandleResult implements Serializable {

    private static final long serialVersionUID = -6805448049100156657L;

    private boolean success;

    private String message;

    private Throwable throwable;

    public EventHandleResult() {
    }

    public EventHandleResult(boolean success, String message, Throwable throwable) {
        this.success = success;
        this.message = message;
        this.throwable = throwable;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public String toString() {
        return "EventHandleResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", throwable=" + throwable +
                '}';
    }

}
