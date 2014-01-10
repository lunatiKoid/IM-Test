package com.alibaba.rfq.sourcingfriends.msgcenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import com.alibaba.rfq.sourcingfriends.R;
import com.alibaba.rfq.sourcingfriends.xmpp.impl.TimeRender;
import com.alibaba.rfq.sourcingfriends.xmpp.impl.XmppConnectionImpl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TradeMessageActivity extends Activity {

    //
    public class TradeMsg {

        public Bitmap photo;
        public String name;
        public String msg;
        public Date   lastMsgDate;

        public TradeMsg(Bitmap photo, String msg, Date lastMsgDate, String name) {
            this.photo = photo;
            this.name = name;
            this.msg = msg;
            this.lastMsgDate = lastMsgDate;
        }
    }

    private static final int    TRADE_MSG_RECEIVE = 3;

    private ChatManager         cm;
    private ChatManagerListener cmListener;

    private Context             gContext;
    private TradeMsgAdapter     tradeMsgAdapter;
    private ListView            tradeMsgListView;
    List<TradeMsg>              listData;

    Map<String, TradeMsg>       tradeMap          = new HashMap<String, TradeMsg>();

    List<TradeMsg> getData() {
        // get data from the openfire server
        List<TradeMsg> tradeMsgList = new ArrayList<TradeMsg>();
        return tradeMsgList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.trade_message);

        gContext = this;

        listData = getData();

        tradeMsgListView = (ListView) findViewById(R.id.TradeMsgListViewId);
        tradeMsgAdapter = new TradeMsgAdapter(this);
        tradeMsgListView.setAdapter(tradeMsgAdapter);

        tradeMsgListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                {
                    Log.i("TradeMessageActivity", "cm.listener.size1=" + cm.getChatListeners().size());
                    cm.removeChatListener(cmListener);
                    Log.i("TradeMessageActivity", "cm.listener.size2=" + cm.getChatListeners().size());

                    // TODO Auto-generated method stub
                    Intent intent = new Intent();
                    intent.putExtra("UserId", listData.get(position).name);
                    intent.setClass(TradeMessageActivity.this, IngMessageActivity.class);
                    startActivity(intent);
                }

                Toast.makeText(getApplicationContext(), "click " + listData.get(position), Toast.LENGTH_SHORT).show();
            }

        });

        // xmpp st
        cm = XmppConnectionImpl.getConnection().getChatManager();

        cmListener = new ChatManagerListener() {

            @Override
            public void chatCreated(Chat chat, boolean able) {

                chat.addMessageListener(new MessageListener() {

                    @Override
                    public void processMessage(Chat chat2, Message message) {
                        Log.v("--tags--", "--tags-form--" + message.getFrom());
                        Log.v("--tags--", "--tags-message--" + message.getBody());

                        // tem
                        Bitmap photo = BitmapFactory.decodeResource(gContext.getResources(),
                                                                    R.drawable.trade_user_photo);
                        // 获取头像，消息、时间、用户
                        TradeMsg one = new TradeMsg(photo, message.getBody(), new Date(), message.getFrom());

                        // 在handler里取出来显示消息
                        android.os.Message msg = handler.obtainMessage();
                        msg.what = TRADE_MSG_RECEIVE;
                        msg.obj = one;
                        msg.sendToTarget();

                        Log.i("TradeMessageActivity", message.getFrom() + "-> msg:" + message.getBody() + " date:"
                                                      + TimeRender.getDate().toString());
                    }
                });
            }
        };
        Log.i("TradeMessageActivity", "add chat listener");
    }

    private Handler handler = new Handler() {

                                public void handleMessage(android.os.Message msg) {

                                    switch (msg.what) {
                                        case TRADE_MSG_RECEIVE:
                                            // 获取消息并显示

                                            TradeMsg one = (TradeMsg) msg.obj;

                                            if (false == tradeMap.containsKey(one.name)) {
                                                tradeMap.put(one.name, one);
                                                listData.add(one);
                                            } else {
                                                Iterator<TradeMsg> it = listData.iterator();
                                                while (it.hasNext()) {
                                                    TradeMsg tem = it.next();
                                                    if (tem.name.equalsIgnoreCase(one.name)) {
                                                        tem.msg = one.msg;
                                                        break;
                                                    }
                                                }
                                            }
                                            // 刷新适配器
                                            tradeMsgAdapter.notifyDataSetChanged();
                                            break;
                                        default:
                                            break;
                                    }
                                };
                            };

    class TradeMsgAdapter extends BaseAdapter {

        private Context context;

        public TradeMsgAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public class ViewHolder {

            ImageView userImageView;
            TextView  nameTextview;
            TextView  MsgTextview;
            TextView  lastMsgDateTextView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder vHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.trade_msg_layout, null);
                vHolder = new ViewHolder();
                vHolder.userImageView = (ImageView) convertView.findViewById(R.id.tradeUserImageViewId);
                vHolder.nameTextview = (TextView) convertView.findViewById(R.id.tradeUserNameTextViewId);
                vHolder.MsgTextview = (TextView) convertView.findViewById(R.id.tradeUserLastMsgTextViewId);
                vHolder.lastMsgDateTextView = (TextView) convertView.findViewById(R.id.tradeUserTimeTextViewId);
                convertView.setTag(vHolder);
            } else {
                vHolder = (ViewHolder) convertView.getTag();
            }

            // get the data of the index position
            TradeMsg one = listData.get(position);

            // show data
            vHolder.userImageView.setImageBitmap(one.photo);
            vHolder.nameTextview.setText(one.name);
            vHolder.MsgTextview.setText(one.msg);
            vHolder.lastMsgDateTextView.setText(one.lastMsgDate.toGMTString());

            return convertView;
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("TradeMessageActivity", "on Start");
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        cm.addChatListener(cmListener);
        Log.i("TradeMessageActivity", "on Resume");
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        Log.i("TradeMessageActivity", "on Pause");
    }

    @Override
    public void onStop() {
        super.onStop();
        // cm.removeChatListener(cmListener);
        Log.i("TradeMessageActivity", "on Stop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("TradeMessageActivity", "on Destroy");
    }

}
