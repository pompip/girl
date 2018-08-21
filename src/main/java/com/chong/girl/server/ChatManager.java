package com.chong.girl.server;

import com.chong.girl.bean.ChatUserInfo;
import com.chong.girl.bean.MsgCMD;
import com.chong.girl.bean.MsgEntity;
import com.chong.girl.util.LogUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatManager {

    private ObjectMapper mapper = new ObjectMapper();
    private ConcurrentHashMap<WebSocketSession, ChatUserInfo> userMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();


    public void handleReceivedMsg(WebSocketSession session, String msg) throws Exception {
        LogUtils.logger.info(msg);
        MsgCMD msgCMD = mapper.readValue(msg, MsgCMD.class);
        if (msgCMD.cmd == 1) {
            MsgEntity msgEntity = mapper.readValue(msgCMD.body, MsgEntity.class);
            if ("all".equals(msgEntity.toUserID)) {
                sendMsgToAll(session, msgEntity.msgData);
            } else {
                WebSocketSession toSession = sessionMap.get(msgEntity.toUserID);
                if (toSession != null) {
                    sendMsgToUser(session, toSession, msgEntity.msgData);
                }

            }
        } else if (msgCMD.cmd == 2) {
            String userName = msgCMD.body;
            ChatUserInfo userInfo = new ChatUserInfo(session.getId(), userName);
            session.sendMessage(MsgCMD.createRegisterMsg(userInfo));
            userMap.put(session, userInfo);
        }
    }

    private void sendMsgToAll(WebSocketSession fromSession, String msg) {

        MsgEntity entity = new MsgEntity();
        entity.toUserID = "all";
        entity.fromUserID = fromSession.getId();
        entity.msgData = msg;
        entity.time = System.currentTimeMillis();

        sessionMap.forEach((id, session) -> {
            if (!session.equals(fromSession)) {
                try {
                    session.sendMessage(MsgCMD.createChatMsg(entity));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendMsgToUser(WebSocketSession fromSession, WebSocketSession toSession, String msg) throws Exception {
        MsgEntity entity = new MsgEntity();
        entity.toUserID = toSession.getId();
        entity.fromUserID = fromSession.getId();
        entity.msgData = msg;
        entity.time = System.currentTimeMillis();
        toSession.sendMessage(MsgCMD.createChatMsg(entity));

    }

    private void sendOnlineUserRefreshMessage() {
        List<String> allUserList = getAllUserList();
        sessionMap.forEach((id, current) -> {
            try {
                current.sendMessage(MsgCMD.createOnlineUserRefreshMsg(allUserList));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void userConnected(WebSocketSession session) {
        sessionMap.put(session.getId(), session);
        userMap.put(session, new ChatUserInfo(session.getId()));
        sendOnlineUserRefreshMessage();
    }

    public void userDisConnected(WebSocketSession session) {
        sessionMap.remove(session.getId());
        userMap.remove(session);
        sendOnlineUserRefreshMessage();
    }

    public List<String> getAllUserList() {
        List<String> userList = new ArrayList<>();
        sessionMap.forEach((id, session) -> {
            userList.add(session.getId());
        });
        return userList;
    }

    public List<ChatUserInfo> getAllUserInfo() {
        List<ChatUserInfo> userInfoList = new ArrayList<>(userMap.values());
        return userInfoList;
    }

    private List<String> lastList;

    @Scheduled(cron = "0/15 * * * * *")
    public void refreshOnlineUser() {
        LogUtils.logger.info(new Date(System.currentTimeMillis()).toString());
        if (lastList == null) {
            sendOnlineUserRefreshMessage();
            lastList = getAllUserList();
        } else {
            List<String> allUserList = getAllUserList();
            if (lastList.size() != allUserList.size()) {
                sendOnlineUserRefreshMessage();
            } else {
                Collections.sort(lastList);
                Collections.sort(allUserList);
                for (int i = 0; i < lastList.size(); i++) {
                    if (!lastList.get(i).equals(allUserList.get(i))) {
                        sendOnlineUserRefreshMessage();
                        return;
                    }
                }

            }
        }
    }
}
