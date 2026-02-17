package com.grepp.core.servlet.annotation;

import com.grepp.core.http.HttpMethod;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface EndPoint {
    String url();
    HttpMethod method() default HttpMethod.GET;
}
