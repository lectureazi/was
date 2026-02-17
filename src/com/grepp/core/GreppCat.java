package com.grepp.core;

import com.grepp.core.servlet.Servlet;
import com.grepp.core.servlet.ServletStorage;
import com.grepp.core.servlet.ThreadPool;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

public class GreppCat {
    
    private ServerSocket serverSocket;
    private ThreadPool threadPool;
    
    public GreppCat(int port, int threadCount, List<Servlet> servlets) {
        try {
            serverSocket = new ServerSocket(port);
            ServletStorage.init(servlets);
            threadPool = ThreadPool.getInstance(threadCount);
            System.out.println("server start(port : " + port + ")");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    
    public void start() {
        while (true) {
            try {
                threadPool.addTask(serverSocket.accept());
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}