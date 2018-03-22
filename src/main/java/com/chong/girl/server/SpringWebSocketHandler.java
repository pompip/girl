package com.chong.girl.server;

import com.chong.girl.bean.*;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class SpringWebSocketHandler extends AbstractWebSocketHandler {
    private static ConcurrentHashMap<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    private  TypeAdapter<MsgUser> userAdapter = new Gson().getAdapter(MsgUser.class);
    private  TypeAdapter<MsgSystem> systemAdapter = new Gson().getAdapter(MsgSystem.class);

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String msg = message.getPayload();
        System.out.println(msg);
        MsgUser msgEntity;
        String toUserID;
        try { //兼容测试
            msgEntity = userAdapter.fromJson(msg);
            toUserID = msgEntity.toUserID;
        } catch (Exception e) {

            MsgUser msgUser = new MsgUser();
            msgUser.fromUserID = session.getId();
            msgUser.toUserID = "all";
            msgUser.msgData = "testMessage" + msg;
            TextMessage textMessage = stringToMsg(userAdapter.toJson(msgUser));
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
            TextMessage textMessage = stringToMsg(userAdapter.toJson(msgUser));
            for (WebSocketSession s : sessionMap.values()) {
                if (s == session) {
                    continue;
                }
                s.sendMessage(textMessage);
            }
        } else {//私聊
            WebSocketSession toUserSession = sessionMap.get(toUserID);
            if (toUserSession == null) {
                session.sendMessage(new TextMessage(systemAdapter.toJson(MsgSystem.createMessage(3))));
            } else {
                MsgUser msgUser = new MsgUser();
                msgUser.fromUserID = session.getId();
                msgUser.toUserID = toUserID;
                msgUser.msgData = msgEntity.msgData;
                toUserSession.sendMessage(new TextMessage(userAdapter.toJson(msgUser)));
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
            TextMessage msg = stringToMsg(systemAdapter.toJson(msgSystem));
            s.sendMessage(msg);
        }
        System.out.println("sendOnlineUserMsg  " +msgSystem.onlineUser.toString());
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
