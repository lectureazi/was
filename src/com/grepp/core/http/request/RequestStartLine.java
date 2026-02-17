package com.grepp.core.http.request;

import com.grepp.core.http.HttpMethod;

public record RequestStartLine(
    HttpMethod method,
    String url,
    String queryString,
    String protocol
) {

}
