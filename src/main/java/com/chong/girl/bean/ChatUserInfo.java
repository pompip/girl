package com.chong.girl.bean;

public class ChatUserInfo {
    public String userID;
    public String userName = "unknow";

    public ChatUserInfo(String userID) {
        this.userID = userID;
    }

    public ChatUserInfo(String userID, String userName) {
        this.userID = userID;
        this.userName = userName;
    }
}
