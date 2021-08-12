package com.aerfafish.carserver.socket;

import com.aerfafish.carserver.config.ServerConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 1、小车持续连接服务端
 * 2、连接服务端后推送视频
 */
@ServerEndpoint(value = "/websocket/server", configurator = ServerConfigurator.class) //接受websocket请求路径
@Component  //注册到spring容器中
public class ServerWebSocket {

    //保存所有在线socket连接
    private static Map<String, ServerWebSocket> webSocketMap = new LinkedHashMap<>();

    //记录当前在线数目
    private static int count=0;

    //当前连接（每个websocket连入都会创建一个MyWebSocket实例
    private Session session;

    private final static Logger log = LoggerFactory.getLogger(ServerWebSocket.class);
    //处理连接建立
    @OnOpen
    public void onOpen(Session session){
        this.session=session;
        webSocketMap.put(session.getId(),this);
        addCount();
        log.info("新的小车连接加入：{}",session.getId());
    }

    //接受消息
    @OnMessage
    public void onMessage(byte[] message,Session session){
        log.info("收到小车{}二进制消息",session.getId());
        try{
            ClientWebSocket.broadcastPicture(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //接受消息
    @OnMessage
    public void onMessage(String message,Session session){
        log.info("收到小车{}消息：{}",session.getId(), message);
        try{
            ClientWebSocket.broadcastPicture(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //处理错误
    @OnError
    public void onError(Throwable error,Session session){
        log.info("小车发生错误{},{}",session.getId(),error.getMessage());
    }

    //处理连接关闭
    @OnClose
    public void onClose(){
        webSocketMap.remove(this.session.getId());
        reduceCount();
        log.info("小车连接关闭:{}",this.session.getId());
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
        ServerWebSocket.webSocketMap.forEach((k, v)->{
            try{
                v.sendMessage(message);
            }catch (Exception e){
                log.error("broadcast fail", e);
            }
        });
    }

    //广播消息
    public static void broadcast(String message){
        ServerWebSocket.webSocketMap.forEach((k, v)->{
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
        ServerWebSocket.count++;
    }

    public static synchronized void reduceCount(){
        ServerWebSocket.count--;
    }

    public static void broadcastControl(byte[] control) {
        broadcast(control);
    }

    public static void broadcastControl(String control) {
        broadcast(control);
    }
}
