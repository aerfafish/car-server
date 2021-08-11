package com.aerfafish.carserver.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@ServerEndpoint(value = "/websocket/client") //接受websocket请求路径
@Component  //注册到spring容器中
public class ClientWebSocket {


    //保存所有在线socket连接
    private static Map<String, ClientWebSocket> webSocketMap = new LinkedHashMap<>();

    //记录当前在线数目
    private static int count=0;

    //当前连接（每个websocket连入都会创建一个MyWebSocket实例
    private Session session;

    private final static Logger log = LoggerFactory.getLogger(ClientWebSocket.class);
    //处理连接建立
    @OnOpen
    public void onOpen(Session session){
        this.session=session;
        webSocketMap.put(session.getId(), this);
        addCount();
        log.info("新的用户连接加入：{}",session.getId());
    }

    //接受消息
    @OnMessage
    public void onMessage(String message,Session session){
        log.info("收到用户{}消息：{}",session.getId(), message);
        try{
            ServerWebSocket.broadcastControl(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //接受消息
    @OnMessage
    public void onMessage(byte[] message, Session session){
        log.info("收到用户{}二进制消息",session.getId());
        try{
            ServerWebSocket.broadcastControl(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //处理错误
    @OnError
    public void onError(Throwable error,Session session){
        log.info("发生错误{},{}",session.getId(),error.getMessage());
    }

    //处理连接关闭
    @OnClose
    public void onClose(){
        webSocketMap.remove(this.session.getId());
        reduceCount();
        log.info("连接关闭:{}",this.session.getId());
    }

    //群发消息

    //发送消息
    public void sendMessage(byte[] message) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(message);
        this.session.getBasicRemote().sendBinary(buffer);
    }

    //发送消息
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    //广播消息
    public static void broadcast(byte[] message){
        ClientWebSocket.webSocketMap.forEach((k, v)->{
            try{
                v.sendMessage(message);
            }catch (Exception e){
                log.error("broadcast fail", e);
            }
        });
    }

    //广播消息
    public static void broadcast(String message){
        ClientWebSocket.webSocketMap.forEach((k, v)->{
            try{
                v.sendMessage(message);
            }catch (Exception e){
                log.error("broadcast fail", e);
            }
        });
    }

    //获取在线连接数目
    public static int getCount(){
        return count;
    }

    //操作count，使用synchronized确保线程安全
    public static synchronized void addCount(){
        ClientWebSocket.count++;
    }

    public static synchronized void reduceCount(){
        ClientWebSocket.count--;
    }

    public static void broadcastPicture(byte[] picture) {
        broadcast(picture);
    }

    public static void broadcastPicture(String picture) {
        broadcast(picture);
    }

}
