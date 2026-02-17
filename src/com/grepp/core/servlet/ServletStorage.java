package com.grepp.core.servlet;

import com.grepp.core.http.error.ServletInitializeException;
import com.grepp.core.servlet.annotation.RequestMapping;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ServletStorage {
    
    private final Map<String, Servlet> servletMap = new LinkedHashMap<>();
    
    private static ServletStorage instance;
    
    public static ServletStorage getInstance() {
        if (instance == null) throw new ServletInitializeException("ServletStorage not initialized");
        return instance;
    }
    
    public static ServletStorage init(List<Servlet> servlets) {
        if (instance == null) {
            instance = new ServletStorage(servlets);
        }
        
        return instance;
    }
    
    private ServletStorage(List<Servlet> servlets) {
        registServlet(servlets);
    }
    
    public Servlet getServlet(String url) {
        return servletMap.get(url);
    }
    
    private void registServlet(List<Servlet> servlets){
        for(Servlet servlet : servlets){
            RequestMapping request = servlet.getClass().getAnnotation(RequestMapping.class);
            
            if(request == null){
                throw new ServletInitializeException(
                    servlet.getClass().getName()
                        + " is not annotated with @"
                        + RequestMapping.class.getSimpleName());
            }
            
            if(servletMap.containsKey(request.url())){
                throw new ServletInitializeException(request.url() + "가 중복되어 등록되었습니다.");
            }
            
            servletMap.put(request.url().replaceFirst("/", ""), servlet);
        }
    };

}
