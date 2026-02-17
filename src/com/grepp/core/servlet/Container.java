package com.grepp.core.servlet;

import com.grepp.core.http.error.CommonException;
import com.grepp.core.http.request.HttpRequest;
import com.grepp.core.http.response.HttpResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class Container implements Runnable {
    
    private final LinkedBlockingQueue<Socket> taskQueue;
    
    public Container(LinkedBlockingQueue<Socket> taskQueue) {
        this.taskQueue = taskQueue;
    }
    
    @Override
    public void run() {
        RequestParser requestParser = new RequestParser();
        HandlerMapper handlerMapper = new HandlerMapper();
        HandlerAdapter handlerAdapter = new HandlerAdapter();
        
        while(true){
            try (
                Socket client = taskQueue.take();
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
                PrintWriter writer = new PrintWriter(client.getOutputStream());
                BufferedOutputStream bos = new BufferedOutputStream(client.getOutputStream());
            ) {
                
                System.out.println("take : " + taskQueue.size());
                System.out.println("take : " + taskQueue);
                
                String line = reader.readLine();
                
                if (line == null) {
                    return;
                }
                
                HttpRequest request = requestParser.parseRequest(line, reader);
                Servlet servlet = handlerMapper.getHandler(request.startLine().url());
                
                if (servlet == null) {
                    writer.print("http/1.1 404 Not Found \n");
                    writer.flush();
                    continue;
                }
                
                HttpResponse response = handlerAdapter.handle(servlet, request);
                sendResponseHeader(response, writer);
                sendResponseBody(response, bos);
                
            } catch (CommonException | IOException e) {
                System.err.println(e.getMessage());
            } catch (InterruptedException ignored) { }
        }
    }
    
    private static void sendServerErrorResponse(CommonException e, PrintWriter writer) {
        writer.print("http/1.1 500 Internal Server Error \n\n");
        writer.print(e.getMessage());
        writer.flush();
    }
    
    private void sendResponseBody(HttpResponse response, BufferedOutputStream bos) {
        try {
            bos.write(response.body().getBody());
            bos.flush();
        } catch (IOException e) {
            throw new CommonException("서버 에러 입니다.");
        }
    }
    
    private static void sendResponseHeader(HttpResponse response, PrintWriter writer) {
        writer.print(response.startLine());
        writer.print(response.header());
        writer.flush();
    }
}
