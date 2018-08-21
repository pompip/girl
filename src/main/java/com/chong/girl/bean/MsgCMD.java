package com.chong.girl.bean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;

import java.util.List;

public class MsgCMD {
    private static ObjectMapper mapper = new ObjectMapper();
    public int cmd;
    public String body;

    public static TextMessage createChatMsg(MsgEntity msgBody)
            throws JsonProcessingException {
        MsgCMD msgCMD = new MsgCMD();
        msgCMD.cmd = 1;
        msgCMD.body = mapper.writeValueAsString(msgBody);
        return new TextMessage(mapper.writeValueAsString(msgCMD));
    }

    public static TextMessage createRegisterMsg(ChatUserInfo userInfo)
            throws JsonProcessingException {
        MsgCMD msgCMD = new MsgCMD();
        msgCMD.cmd = 2;
        msgCMD.body = mapper.writeValueAsString(userInfo);
        return new TextMessage(mapper.writeValueAsString(msgCMD));
    }

    public static TextMessage createOnlineUserRefreshMsg(List<String> onlineUser) throws JsonProcessingException {
        MsgCMD msgCMD = new MsgCMD();
        msgCMD.cmd = 3;

        msgCMD.body = mapper.writeValueAsString(onlineUser);
        return new TextMessage(mapper.writeValueAsString(msgCMD));


    }
}
