package com.grepp.core.servlet;

import com.grepp.core.http.HttpHeader;
import com.grepp.core.http.HttpMethod;
import com.grepp.core.http.request.HttpRequest;
import com.grepp.core.http.request.RequestParameter;
import com.grepp.core.http.request.RequestStartLine;
import java.io.BufferedReader;
import java.io.IOException;

public class RequestParser {
    
    public HttpRequest parseRequest(String line, BufferedReader reader) throws IOException {
        RequestStartLine startLine = parseStartLine(line);
        HttpHeader header = parseHttpHeader(reader);
        RequestParameter param = new RequestParameter();
        
        if (startLine.method().equals(HttpMethod.GET)) {
            param = parseQueryString(startLine.queryString());
        }
        
        if (startLine.method().equals(HttpMethod.POST)) {
            param = parseRequestBody(header, reader);
        }
        
        return new HttpRequest(startLine, header, param);
    }
    
    private RequestStartLine parseStartLine(String startLine) {
        String[] arr = startLine.split(" ");
        HttpMethod method = HttpMethod.valueOf(arr[0].toUpperCase());
        String protocol = arr[2];
        String url = arr[1];
        String queryString = "";
        
        int queryIndex = arr[1].indexOf("?");
        if (queryIndex > 0) {
            url = arr[1].substring(0, queryIndex);
            queryString = arr[1].substring(queryIndex + 1);
        }
        
        return new RequestStartLine(method, url, queryString, protocol);
    }
    
    private HttpHeader parseHttpHeader(BufferedReader reader) throws IOException {
        HttpHeader header = new HttpHeader();
        
        String headerLine = reader.readLine();
        while (!headerLine.isEmpty()) {
            header.add(headerLine);
            headerLine = reader.readLine();
        }
        return header;
    }
    
    private RequestParameter parseQueryString(String queryString) {
        RequestParameter param = new RequestParameter();
        if (queryString.isEmpty()) {
            return param;
        }
        
        String[] pairTokens = queryString.split("&");
        
        for (String pair : pairTokens) {
            String[] token = pair.split("=");
            String key = token[0];
            String value = token[1];
            param.add(key, value);
        }
        
        return param;
    }
    
    private RequestParameter parseRequestBody(HttpHeader header, BufferedReader reader)
        throws IOException {
        RequestParameter param = new RequestParameter();
        int contentLength = Integer.parseInt(
            header.getHeader("Content-Length").getFirst().trim()
        );
        
        char[] bodyData = new char[contentLength];
        reader.read(bodyData, 0, contentLength);
        String bodyTxt = String.valueOf(bodyData);
        
        if (!bodyTxt.isEmpty()) {
            param = parseQueryString(bodyTxt);
        }
        
        return param;
    }
}
