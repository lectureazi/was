package com.grepp.core.http.request;


import com.grepp.core.http.HttpHeader;

/**
 * Request를 parsing 한 결과를 저장하는 객체
 * @param startLine : 시작줄
 * @param header : header
 * @param param : queryString과 body로 전달된 요청파라미터를 저장하는 객체 
 */
public record HttpRequest(
    RequestStartLine startLine,
    HttpHeader header,
    RequestParameter param
) {

}
