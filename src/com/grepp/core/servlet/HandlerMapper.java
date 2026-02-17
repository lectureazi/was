package com.grepp.core.servlet;


public class HandlerMapper {
    
    private final ServletStorage servletStorage = ServletStorage.getInstance();
    
    public HandlerMapper() {}
    
    public Servlet getHandler(String url) {
        String[] path = url.split("/");
        return servletStorage.getServlet(path.length > 1 ? path[1] : "");
    }
}
