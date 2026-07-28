package com.quotation.interfaces.websocket;

import org.springframework.stereotype.Component;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws/{userId}")
@Component
public class WebSocketServer {
    private static final ConcurrentHashMap<String, Session> SESSION_POOL = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        SESSION_POOL.put(userId, session);
        System.out.println("用户 [" + userId + "] 连接 WebSocket");
    }

    @OnClose
    public void onClose(@PathParam("userId") String userId) {
        SESSION_POOL.remove(userId);
        System.out.println("用户 [" + userId + "] 断开连接");
    }

    @OnMessage
    public void onMessage(String message, @PathParam("userId") String userId) {
        System.out.println("收到用户 [" + userId + "] 消息: " + message);
    }

    public static void sendToUser(String userId, String message) {
        Session session = SESSION_POOL.get(userId);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
                System.out.println("推送消息给 [" + userId + "]: " + message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
