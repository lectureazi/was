package com.grepp.core.http.response;

public enum ResponseStartLine {
    
    OK(200, "OK"),
    NOT_FOUND(404, "Not Found"),
    BAD_REQUEST(400, "Bad Request"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");
    
    private final String protocol;
    private final int statusCode;
    private final String statusMsg;
    
    ResponseStartLine(int statusCode, String statusMsg) {
        this.protocol = "HTTP/1.1";
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
    }
    
    @Override
    public String toString() {
        return protocol + " " + statusCode + " " + statusMsg + "\n";
    }
}
