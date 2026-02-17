package com.grepp.core.http.request;

import com.grepp.core.http.util.MultiValueMap;
import java.util.List;
import java.util.Optional;

public class RequestParameter extends MultiValueMap<String, String> {
    
    public void add(String paramTxt) {
        String[] token = paramTxt.split("=");
        String key = token[0];
        String value = token[1];
        super.add(key, value);
    }
    
    public Optional<List<String>> getParameter(String key) {
        return Optional.ofNullable(super.get(key));
    }
}
