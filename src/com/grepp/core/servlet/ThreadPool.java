package com.grepp.core.servlet;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool {
    
    private final List<Thread> threadPool = new ArrayList<>();
    private final LinkedBlockingQueue<Socket> taskQueue = new LinkedBlockingQueue<>();
    
    private static final int THREAD_CNT = 16;
    private static ThreadPool instance;
    
    public static ThreadPool getInstance() {
        return getInstance(THREAD_CNT);
    }
    
    public static ThreadPool getInstance(int threadCnt) {
        if (instance == null) {
            instance = new ThreadPool(threadCnt);
        }
        return instance;
    }
    
    private ThreadPool(int threadCnt) {
        for (int i = 0; i < threadCnt; i++) {
            threadPool.add(new Thread(new Container(taskQueue)));
        }
        
        threadPool.forEach(Thread::start);
    }
    
    public void addTask(Socket socket) {
        taskQueue.add(socket);
        System.out.println("addTask : " + taskQueue);
    }
}
