package com.sky.websocket;

import org.springframework.stereotype.Component;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket Service
 */
@Component
@ServerEndpoint("/ws/{sid}")
public class WebSocketServer {

    // save session object
    private static Map<String, Session> sessionMap = new HashMap();

    /**
     * call when connection build successfully
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        System.out.println("client " + sid + " connection built");
        sessionMap.put(sid, session);
    }

    /**
     * call when receive message from client side
     *
     * @param message
     */
    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        System.out.println("received message from client " + sid + ": " + message);
    }

    /**
     * call when close connection
     *
     * @param sid
     */
    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        System.out.println("closed connection :" + sid);
        sessionMap.remove(sid);
    }

    /**
     * send message to all clients
     *
     * @param message
     */
    public void sendToAllClient(String message) {
        Collection<Session> sessions = sessionMap.values();
        for (Session session : sessions) {
            try {
                // server send message to client
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
