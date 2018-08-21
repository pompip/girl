package com.chong.girl.control;

import com.chong.girl.server.ChatManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebSocketController {

    @Autowired
    ChatManager chatManager;

    private ObjectMapper mapper;

    @Autowired
    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting(String message) {
        return "hello" + message;
    }

    @MessageMapping("/message")
    //@SendTo("/topic/greetings")
    //接收/app/message发来的value，然后将value转发到/topic/greetings客户端
    public String message(String message) {
        //通过convertAndSendToUser 向用户发送信息,
        // 第一个参数是接收消息的用户,第二个参数是浏览器订阅的地址,第三个参数是消息本身
        //messagingTemplate.convertAndSendToUser();
//        messagingTemplate.convertAndSend("/topic/greetings",message+ "hello world");
        return null;
    }

    @RequestMapping("/websocket/getUsers")
    @ResponseBody
    public String findAllUser() throws JsonProcessingException {
        return mapper.writeValueAsString(chatManager.getAllUserList());
    }

    @RequestMapping("/websocket/getAllUserInfo")
    @ResponseBody
    public String findAllUserInfo() throws JsonProcessingException {
        return mapper.writeValueAsString(chatManager.getAllUserList());
    }
}
