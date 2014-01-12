package com.alibaba.rfq.sourcingfriends.service;

import java.util.Date;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import com.alibaba.rfq.sourcingfriends.xmpp.impl.TimeRender;
import com.alibaba.rfq.sourcingfriends.xmpp.impl.XmppConnectionImpl;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class XmppService extends Service {

    // xmpp
    private ChatManager         cm;
    private ChatManagerListener cmListener;
    public final static String  actionName   = "com.alibaba.rfq.sourcingfriends.service.XmppService";
    public final static String  SENDERS_NAME = "USERNAME";
    public final static String  SENDED_MSG   = "MSGS";
    public final static String  RECEIVE_TIME = "RECEIVETIME";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("XmppService", "on Create");

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.i("XmppService", "[on Start] add chat listener...");

        new Thread() {

            public void run() {
                cm = XmppConnectionImpl.getConnection().getChatManager();

                cmListener = new ChatManagerListener() {

                    @Override
                    public void chatCreated(Chat chat, boolean able) {

                        chat.addMessageListener(new MessageListener() {

                            @Override
                            public void processMessage(Chat chat2, Message message) {
                                Log.i("XmppService", message.getFrom() + "-> msg:" + message.getBody() + " date:"
                                                     + TimeRender.getDate().toString());

                                // 发送特定action的广播
                                Intent intent = new Intent();
                                intent.setAction(actionName);
                                intent.putExtra(SENDED_MSG, message.getBody());
                                intent.putExtra(SENDERS_NAME, message.getFrom());
                                intent.putExtra(RECEIVE_TIME, new Date().getTime());
                                sendBroadcast(intent);
                            }
                        });
                    }
                };
                if (cm.getChatListeners().size() <= 0) cm.addChatListener(cmListener);
            }
        }.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("XmppService", "[on Destroy] remove chat listener...");
        if (cm.getChatListeners().size() > 0) cm.removeChatListener(cmListener);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        Log.i("XmppService", "on Bind");
        return null;
    }

}
