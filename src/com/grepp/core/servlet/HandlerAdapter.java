package com.grepp.core.servlet;

import com.grepp.core.http.HttpMethod;
import com.grepp.core.http.error.CommonException;
import com.grepp.core.http.error.ServletInitializeException;
import com.grepp.core.http.request.HttpRequest;
import com.grepp.core.http.response.HttpResponse;
import com.grepp.core.http.response.ResponseStartLine;
import com.grepp.core.servlet.annotation.EndPoint;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerAdapter {
    
    public HttpResponse handle(Servlet servlet, HttpRequest request) {
        if (servlet == null) {
            return new HttpResponse(ResponseStartLine.NOT_FOUND);
        }
        
        try {
            
            Method method = findTargetMethod(servlet, request);
            
            if (method == null) {
                return new HttpResponse(ResponseStartLine.NOT_FOUND);
            }
            
            return (HttpResponse) method.invoke(servlet, request);
            
        } catch (CommonException | InvocationTargetException e) {
            
            Throwable cause = e.getCause();
            
            if (cause instanceof CommonException) {
                return new HttpResponse(ResponseStartLine.INTERNAL_SERVER_ERROR);
            }
            
            throw new ServletInitializeException(e);
            
        } catch (IllegalAccessException e) {
            
            throw new ServletInitializeException("Method 접근 권한이 없습니다.", e);
        }
    }
    
    private Method findTargetMethod(Servlet servlet, HttpRequest request) {
        String targetUrl = generateUrl(request);
        HttpMethod targetMethod = request.startLine().method();
        Method result = null;
        
        for (Method method : servlet.getClass().getDeclaredMethods()) {
            
            EndPoint endPoint = method.getAnnotation(EndPoint.class);
            
            if (endPoint == null){
                throw new CommonException("EndPoint 어노테이션이 존재하지 않습니다.");
            }
            
            if (isPathMatch(endPoint.url(), targetUrl) && endPoint.method() == targetMethod) {
                result = method;
                break;
            }
        }
        
        return result;
    }
    
    private boolean isPathMatch(String annotationUrl, String requestUrl) {
        String normalizedAnn = annotationUrl.replaceAll("^/|/$", "");
        String normalizedReq = requestUrl.replaceAll("^/|/$", "");
        return normalizedAnn.equals(normalizedReq);
    }
    
    private String generateUrl(HttpRequest request) {
        String url = request.startLine().url();
        int limit = url.indexOf('?');
        if (limit != -1) url = url.substring(0, limit);
        
        return url;
    }
}