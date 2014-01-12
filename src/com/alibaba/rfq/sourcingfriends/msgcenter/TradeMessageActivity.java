package com.alibaba.rfq.sourcingfriends.msgcenter;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.rfq.sourcingfriends.LoginActivity;
import com.alibaba.rfq.sourcingfriends.R;
import com.alibaba.rfq.sourcingfriends.db.DatabaseService;
import com.alibaba.rfq.sourcingfriends.db.DbConstant;
import com.alibaba.rfq.sourcingfriends.service.XmppService;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
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
        public int    msgUnreadNum;
        public Date   lastMsgDate;

        public TradeMsg(Bitmap photo, String msg, int num, Date lastMsgDate, String name) {
            this.photo = photo;
            this.name = name;
            this.msg = msg;
            this.msgUnreadNum = num;
            this.lastMsgDate = lastMsgDate;
        }
    }

    public static final String USER_NAME_TO_TALK = "USER_NAME_TO_TALK";
    private static final int   TRADE_MSG_RECEIVE = 3;
    private static final int   MAX_SHOW_LENGTH   = 22;
    private TradeMsgReceiver   receiver;

    private Context            gContext;
    private TradeMsgAdapter    tradeMsgAdapter;
    private ListView           tradeMsgListView;
    private List<TradeMsg>     listData;

    Map<String, TradeMsg>      tradeMap          = new HashMap<String, TradeMsg>();

    List<TradeMsg> getData() {
        // get data from the openfire server
        List<TradeMsg> tradeMsgList = new ArrayList<TradeMsg>();

        // db
        DatabaseService dbService = new DatabaseService(gContext);
        Cursor cursor;
        cursor = dbService.select2DO(DbConstant.TM_TABLE_NAME,
                                     new String[] { DbConstant.TM_MSG_SENDER_NAME, DbConstant.TM_LASTED_MSG_CONTENT,
                                             DbConstant.TM_MSG_UNREAD_NUM, DbConstant.TM_LASTED_MSG_RECEIVED_TIME },
                                     new String[] { DbConstant.TM_MSG_RECEIVER_NAME },
                                     new String[] { LoginActivity.loginAct.getAccount() });

        while (cursor.moveToNext()) {
            String senderName = cursor.getString(cursor.getColumnIndex(DbConstant.TM_MSG_SENDER_NAME));
            String senderMsg = cursor.getString(cursor.getColumnIndex(DbConstant.TM_LASTED_MSG_CONTENT));
            Long myDate = cursor.getLong(cursor.getColumnIndex(DbConstant.TM_LASTED_MSG_RECEIVED_TIME));

            Date lastedMsgTime = new Date(myDate);
            Log.i("TradeMessageActivity", lastedMsgTime.toLocaleString());

            int msgUnreadNum = cursor.getInt(cursor.getColumnIndex(DbConstant.TM_MSG_UNREAD_NUM));
            // to be changed
            Bitmap photo = BitmapFactory.decodeResource(this.getResources(), R.drawable.user_she_photo);
            TradeMsg one = new TradeMsg(photo, senderMsg, msgUnreadNum, lastedMsgTime, senderName);
            tradeMap.put(one.name, one);
            tradeMsgList.add(one);
            Log.i("TradeMessageActivity", "history username:" + one.name);
        }
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
                    // TODO Auto-generated method stub
                    Intent intent = new Intent();
                    intent.putExtra(USER_NAME_TO_TALK, listData.get(position).name);
                    intent.setClass(TradeMessageActivity.this, IngMessageActivity.class);
                    startActivity(intent);
                }

                // Toast.makeText(getApplicationContext(), "click " + listData.get(position),
                // Toast.LENGTH_SHORT).show();
            }

        });

        // register xmpp service com.alibaba.rfq.sourcingfriends.service.XmppService
        receiver = new TradeMsgReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(XmppService.actionName);
        registerReceiver(receiver, filter);
    }

    /**
     * 广播接收器
     */
    private class TradeMsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();

            String aMsg = bundle.getString(XmppService.SENDED_MSG);
            String userName = bundle.getString(XmppService.SENDERS_NAME);
            Date receiveTime = new Date(bundle.getLong(XmppService.RECEIVE_TIME));
            // tem
            Bitmap photo = BitmapFactory.decodeResource(gContext.getResources(), R.drawable.user_she_photo);

            TradeMsg one = new TradeMsg(photo, aMsg, 1, receiveTime, userName);
            android.os.Message msg = handler.obtainMessage();
            msg.what = TRADE_MSG_RECEIVE;
            msg.obj = one;
            msg.sendToTarget();
        }
    }

    private Handler handler = new Handler() {

                                public void handleMessage(android.os.Message msg) {

                                    switch (msg.what) {
                                        case TRADE_MSG_RECEIVE:
                                            // 获取消息并显示

                                            TradeMsg one = (TradeMsg) msg.obj;
                                            // db
                                            DatabaseService dbService = new DatabaseService(gContext);
                                            ContentValues value2intoIM = new ContentValues();

                                            // Bitmap.CompressFormat.JPEG 和 Bitmap.CompressFormat.PNG JPEG 与 PNG
                                            // 的是区别在于 JPEG是有损数据图像，PNG使用从LZ77派生的无损数据压缩算法。
                                            // 这里建议使用PNG格式保存 100 表示的是质量为100%。当然，也可以改变成你所需要的百分比质量。 os 是定义的字节输出流
                                            // .compress() 方法是将Bitmap压缩成指定格式和质量的输出流
                                            final ByteArrayOutputStream os = new ByteArrayOutputStream();
                                            Bitmap bmp = one.photo;
                                            bmp.compress(Bitmap.CompressFormat.PNG, 100, os);

                                            value2intoIM.put(DbConstant.IM_PHOTO, os.toByteArray());
                                            value2intoIM.put(DbConstant.IM_MSG_SENDER_NAME, one.name);
                                            value2intoIM.put(DbConstant.IM_MSG_RECEIVER_NAME,
                                                             LoginActivity.loginAct.getAccount());
                                            value2intoIM.put(DbConstant.IM_MSG_CONTENT, one.msg);
                                            value2intoIM.put(DbConstant.IM_MSG_RECEIVED_TIME, one.lastMsgDate.getTime());
                                            value2intoIM.put(DbConstant.IM_MSG_IN_OR_OUT, "IN");

                                            dbService.insertByDO(DbConstant.IM_TABLE_NAME, value2intoIM);

                                            if (false == tradeMap.containsKey(one.name)) { // new msg

                                                tradeMap.put(one.name, one);
                                                listData.add(one);

                                                ContentValues values = new ContentValues();

                                                values.put(DbConstant.TM_PHOTO, os.toByteArray());
                                                values.put(DbConstant.TM_MSG_SENDER_NAME, one.name);
                                                values.put(DbConstant.TM_MSG_RECEIVER_NAME,
                                                           LoginActivity.loginAct.getAccount());
                                                values.put(DbConstant.TM_LASTED_MSG_CONTENT, one.msg);
                                                values.put(DbConstant.TM_LASTED_MSG_RECEIVED_TIME,
                                                           one.lastMsgDate.getTime());
                                                values.put(DbConstant.TM_MSG_UNREAD_NUM, one.msgUnreadNum);

                                                dbService.insertByDO(DbConstant.TM_TABLE_NAME, values);

                                            } else { // update nums,msgcontent, time

                                                Iterator<TradeMsg> it = listData.iterator();
                                                TradeMsg tem = null;
                                                while (it.hasNext()) {
                                                    tem = it.next();
                                                    if (tem.name.equalsIgnoreCase(one.name)) {
                                                        tem.msg = one.msg;
                                                        tem.lastMsgDate = one.lastMsgDate;
                                                        tem.msgUnreadNum += 1;
                                                        break;
                                                    }
                                                }

                                                ContentValues values = new ContentValues();
                                                values.put(DbConstant.TM_LASTED_MSG_CONTENT, tem.msg);
                                                values.put(DbConstant.TM_MSG_UNREAD_NUM, tem.msgUnreadNum);
                                                values.put(DbConstant.TM_LASTED_MSG_RECEIVED_TIME,
                                                           tem.lastMsgDate.getTime());

                                                String whereClause = (DbConstant.TM_MSG_SENDER_NAME + "=? AND ");
                                                whereClause += (DbConstant.TM_MSG_RECEIVER_NAME + "=?");
                                                String[] whereArgs = new String[] { one.name,
            LoginActivity.loginAct.getAccount() };

                                                if (1 == dbService.update(DbConstant.TM_TABLE_NAME, values,
                                                                          whereClause, whereArgs)) {
                                                    Log.i("TradeMessageActivity", "update success");
                                                } else {
                                                    Log.i("TradeMessageActivity", "update err");
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
            Log.i("TradeMessageActivity","MAX_SHOW_LENGTH"+one.msg.length());
            if (one.msg.length() >= MAX_SHOW_LENGTH) {
                vHolder.MsgTextview.setText(one.msg.substring(0, MAX_SHOW_LENGTH) + "...");
            } else {
                vHolder.MsgTextview.setText(one.msg);
            }
            vHolder.lastMsgDateTextView.setText(one.lastMsgDate.toLocaleString());

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
        // if (cm.getChatListeners().size() <= 0) cm.addChatListener(cmListener);
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
        // 不要忘了这一步
        unregisterReceiver(receiver);
        Log.i("TradeMessageActivity", "on Destroy");
    }

}
