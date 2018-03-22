package com.chong.girl.server;

import com.chong.girl.aspect.HttpAspect;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;
@Deprecated
//@ServerEndpoint("/websocket/hello")
//@Component
public class WebSocketServer {
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(HttpAspect.class);
    private static HashMap<String,Session> sessionMap = new HashMap<>();
    private Session session;
    private String userName;
    private String id;

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        this.id = session.getId();
        logger.info("open");
        sessionMap.put(session.getId(),session);


    }

    @OnClose
    public void onClose(){
        logger.info("onClose");
        sessionMap.remove(id);
    }

    @OnMessage
    public void onMessage(String message){
        logger.info(message);

    }


}
