package com.chong.girl.bean;

import java.util.List;

public class MsgSystem {
    /**
     * 1.有新的聊天消息
     * 2.有新用户上线
     * 3.发送的消息失败
     */
    public int cmd;
    public Long time;
    public String cmdDes;
    public String meID;
    public List<String> onlineUser;

    public MsgSystem() {
    }

    private MsgSystem(int cmd, Long time, String cmdDes) {
        this.cmd = cmd;
        this.time = time;
        this.cmdDes = cmdDes;
    }

    public static MsgSystem createMessage(int cmd) {
        String cmdDes;
        switch (cmd) {
            case 2:
                cmdDes = "有新用户上线";
                break;
            case 3:
                cmdDes = "发送的消息失败";
                break;
            default:
                cmdDes = "";

        }
        return new MsgSystem(cmd, System.currentTimeMillis(), cmdDes);
    }


}
