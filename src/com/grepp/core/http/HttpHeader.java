package com.grepp.core.http;

import com.grepp.core.http.util.MultiValueMap;
import java.util.List;
import java.util.Map.Entry;

public class HttpHeader extends MultiValueMap<String, String> {
    
    public HttpHeader add(String headerTxt) {
        String[] token = headerTxt.split(":");
        String key = token[0];
        String value = token[1];
        super.add(key, value);
        return this;
    }
    
    @Override
    public HttpHeader add(String key, String value) {
        return (HttpHeader) super.add(key, value);
    }
    
    public List<String> getHeader(String key) {
        return super.get(key);
    }
    
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        
        for (Entry<String, List<String>> entry : super.entrySet()) {
            for (String value : entry.getValue()) {
                sb.append(entry.getKey()).append(": ").append(value).append("\n");
            }
        }
        
        sb.append("\n");
        return sb.toString();
    }
}
