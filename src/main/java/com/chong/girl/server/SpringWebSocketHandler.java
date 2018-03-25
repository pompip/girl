package com.chong.girl.server;

import com.chong.girl.bean.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;
import org.springframework.web.socket.adapter.AbstractWebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class SpringWebSocketHandler extends AbstractWebSocketHandler {


    private  ConcurrentHashMap<String, WebSocketSession> sessionMap ;

    @Bean
    public  ConcurrentHashMap<String, WebSocketSession> getSessionMap() {
        return new  ConcurrentHashMap<String, WebSocketSession> ();
    }

    @Autowired
    public void setSessionMap(ConcurrentHashMap<String,WebSocketSession> sessionMap) {
        this.sessionMap = sessionMap;
    }

    private ObjectMapper mapper ;
    @Autowired
    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String msg = message.getPayload();
        System.out.println(msg);
        MsgUser msgEntity;
        String toUserID;
        try { //兼容测试
//            msgEntity = userAdapter.fromJson(msg);
            msgEntity = mapper.readValue(msg,MsgUser.class);
            toUserID = msgEntity.toUserID;
        } catch (Exception e) {

            MsgUser msgUser = new MsgUser();
            msgUser.fromUserID = session.getId();
            msgUser.toUserID = "all";
            msgUser.msgData = "testMessage" + msg;
            TextMessage textMessage = stringToMsg(mapper.writeValueAsString(msgUser));
            for (WebSocketSession s : sessionMap.values()) {
                if (s == session) {
                    continue;
                }
                s.sendMessage(textMessage);
            }
            return;
        }


        if ("all".equals(toUserID)) {//群聊
            MsgUser msgUser = new MsgUser();
            msgUser.fromUserID = session.getId();
            msgUser.toUserID = "all";
            msgUser.msgData = msgEntity.msgData;
            TextMessage textMessage = stringToMsg(mapper.writeValueAsString(msgUser));
            for (WebSocketSession s : sessionMap.values()) {
                if (s == session) {
                    continue;
                }
                s.sendMessage(textMessage);
            }
        } else {//私聊
            WebSocketSession toUserSession = sessionMap.get(toUserID);
            if (toUserSession == null) {
                session.sendMessage(new TextMessage(mapper.writeValueAsString(MsgSystem.createMessage(3))));
            } else {
                MsgUser msgUser = new MsgUser();
                msgUser.fromUserID = session.getId();
                msgUser.toUserID = toUserID;
                msgUser.msgData = msgEntity.msgData;
                toUserSession.sendMessage(new TextMessage(mapper.writeValueAsString(msgUser)));
            }
        }


    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sessionMap.put(session.getId(), session);
        sendOnlineUserMsg();
    }


    private void sendOnlineUserMsg() throws Exception {
        MsgSystem msgSystem = MsgSystem.createMessage(2);

        for (WebSocketSession s : sessionMap.values()) {
            msgSystem.onlineUser = new ArrayList<>(sessionMap.keySet());
            msgSystem.meID= s.getId();
            TextMessage msg = stringToMsg(mapper.writeValueAsString(msgSystem));
            s.sendMessage(msg);
        }
    }

    private TextMessage stringToMsg(String msg) {
        return new TextMessage(msg);
    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
        sessionMap.remove(session.getId());
        sendOnlineUserMsg();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessionMap.remove(session.getId());
        sendOnlineUserMsg();
    }

}
