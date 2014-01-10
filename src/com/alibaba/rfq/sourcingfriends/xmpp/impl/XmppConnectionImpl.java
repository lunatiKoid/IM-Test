package com.alibaba.rfq.sourcingfriends.xmpp.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.OfflineMessageManager;

import android.util.Log;

public class XmppConnectionImpl {

    private static XMPPConnection con      = null;
    private static String         serverIp = "192.168.0.121";

    public static void setServerIp(String ip) {
        serverIp = ip;
    }

    public static void openOfflineConnection() {
        ConnectionConfiguration connConfig = new ConnectionConfiguration(serverIp);
        connConfig.setSendPresence(false); // set offline login to get msgs off line
        con = new XMPPConnection(connConfig);
        try {
            con.connect();
        } catch (XMPPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static XMPPConnection getOfflineConnection() {
        // Log.i("XmppConnectionImpl",serverIp);
        if (con == null) {
            openOfflineConnection();
        }
        return con;
    }

    public static void closeOfflineConnection() {
        con.disconnect();
        con = null;
    }

    public static List<org.jivesoftware.smack.packet.Message> ThenGetOffMsg(XMPPConnection connection) {

        List<org.jivesoftware.smack.packet.Message> offMsgsList = new ArrayList<org.jivesoftware.smack.packet.Message>();

        OfflineMessageManager offlineManager = new OfflineMessageManager(connection);

        try {
            Iterator<org.jivesoftware.smack.packet.Message> it = offlineManager.getMessages();

            Log.i("XmppConnectionImpl", offlineManager.supportsFlexibleRetrieval() + "");
            Log.i("XmppConnectionImpl", "离线消息数量: " + offlineManager.getMessageCount());

            Map<String, ArrayList<Message>> offlineMsgs = new HashMap<String, ArrayList<Message>>();

            while (it.hasNext()) {
                org.jivesoftware.smack.packet.Message message = it.next();
                Log.i("XmppConnectionImpl",
                      "收到离线消息, Received from 【" + message.getFrom() + "】 message: " + message.getBody());
                String fromUser = message.getFrom().split("/")[0];

                if (offlineMsgs.containsKey(fromUser)) {
                    offlineMsgs.get(fromUser).add(message);
                } else {
                    ArrayList<Message> temp = new ArrayList<Message>();
                    temp.add(message);
                    offlineMsgs.put(fromUser, temp);
                }
            }

            // // 在这里进行处理离线消息集合......
            // Set<String> keys = offlineMsgs.keySet();
            // Iterator<String> offIt = keys.iterator();
            // while (offIt.hasNext()) {
            // String key = offIt.next();
            // ArrayList<Message> ms = offlineMsgs.get(key);
            // TelFrame tel = new TelFrame(key);
            // ChatFrameThread cft = new ChatFrameThread(key, null);
            // cft.setTel(tel);
            // cft.start();
            // for (int i = 0; i < ms.size(); i++) {
            // tel.messageReceiveHandler(ms.get(i));
            // }
            // }

            offlineManager.deleteMessages();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 删除离线消息，即通知服务器删除离线消息
        try {
            offlineManager.deleteMessages();
        } catch (XMPPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return offMsgsList;
    }

    private static void openConnection() {
        try {
            // url、端口，也可以设置连接的服务器名字，地址，端口，用户。
            ConnectionConfiguration connConfig = new ConnectionConfiguration(serverIp, 5222);
            con = new XMPPConnection(connConfig);
            con.connect();
        } catch (XMPPException xe) {
            xe.printStackTrace();
        }
    }

    public static XMPPConnection getConnection() {
        // Log.i("XmppConnectionImpl",serverIp);
        if (con == null) {
            openConnection();
        }
        return con;
    }

    public static void closeConnection() {
        con.disconnect();
        con = null;
    }
}
