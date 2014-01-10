package com.alibaba.rfq.sourcingfriends.msgcenter;


public class Msg {

    String userid;
    String msg;
    String date;
    String from;

    public Msg(String userid, String msg, String date, String from) {
        this.userid = userid;
        this.msg = msg;
        this.date = date;
        this.from = from;
    }
}