package com.example.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Service
public class ChatHandler extends TextWebSocketHandler {

    
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private List<String> webMessages = new ArrayList<String>();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        super.afterConnectionEstablished(session);
        for(int i = 0; i < webMessages.size(); i++) {
        	session.sendMessage(new TextMessage(webMessages.get(i)));
        }
    }
 
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        super.afterConnectionClosed(session, status);
    }
 
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        sessions.forEach(webSocketSession -> {
            try {
                webSocketSession.sendMessage(message);
                webMessages.add(message.getPayload());
                String clientMessage = message.getPayload();
                if("Websocket".equals(clientMessage)) {
                 session.sendMessage(new TextMessage("Server: WebSocket es un protocolo de comunicacion, brinda una forma de intercambiar datos entre el navegador y el servidor por medio de una conexión persistente."
                			+ " y trabaja en los puertos 80 y 443"));
                }
            } catch (IOException e) {
                
            }
        });
    }
    }

