package com.softeng.dingtalk.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author zhanyeye
 * @description
 * @create 2/5/2020 6:26 PM
 */
@Slf4j
@Component
@ServerEndpoint("/websocket")
public class WebSocketController {

    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    // concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketController对象。
    private static CopyOnWriteArraySet<WebSocketController> webSocketSet = new CopyOnWriteArraySet<WebSocketController>();
    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     * @param session
     * @return void
     * @Date 6:49 PM 2/5/2020
     **/
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);  //加入set中
        addOnlineCount(); //添加在线人数
        log.info("新连接接入。当前在线人数为："+getOnlineCount());
    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this); //从set中删除
        subOnlineCount(); //在线数减1
        log.info("有连接关闭。当前在线人数为："+getOnlineCount());
    }

    /**
     * 收到客户端消息后调用
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("客户端发送的消息："+message);
        sendAll(message);
    }

    /**
     * 暴露给外部的群发
     * @param message
     * @throws IOException
     */
    public static void sendInfo(String message) throws IOException {
        sendAll(message);
    }

    /**
     * 群发
     * @param message
     */
    private static void sendAll(String message) {
        Arrays.asList(webSocketSet.toArray()).forEach(item -> {
            WebSocketController customWebSocket = (WebSocketController) item;
            //群发
            try {
                customWebSocket.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("有异常啦");
        error.printStackTrace();
    }

    /**
     * 减少在线人数
     */
    private synchronized void subOnlineCount() {
        WebSocketController.onlineCount--;
    }

    /**
     * 添加在线人数
     */
    private synchronized void addOnlineCount() {
        WebSocketController.onlineCount++;
    }

    /**
     * 当前在线人数
     * @return
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 发送信息
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        //获取session远程基本连接发送文本消息
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }


}
