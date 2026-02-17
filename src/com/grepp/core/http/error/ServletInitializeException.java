package com.grepp.core.http.error;

/**
 * Servlet 초기화 과정에서 발생하는 예외를 처리합니다.
 */
public class ServletInitializeException extends RuntimeException {
    public ServletInitializeException(String message) {
        super(message);
    }
    public ServletInitializeException(Throwable cause) {
        super(cause);
    }
    public ServletInitializeException(String message, Throwable cause) {
        super(message, cause);
    }
}
