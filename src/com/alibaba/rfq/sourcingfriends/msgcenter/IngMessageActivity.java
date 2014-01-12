package com.alibaba.rfq.sourcingfriends.msgcenter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.XMPPException;
import com.alibaba.rfq.sourcingfriends.LoginActivity;
import com.alibaba.rfq.sourcingfriends.R;
import com.alibaba.rfq.sourcingfriends.contactlist.UserDetailActivity;
import com.alibaba.rfq.sourcingfriends.db.DatabaseService;
import com.alibaba.rfq.sourcingfriends.db.DbConstant;
import com.alibaba.rfq.sourcingfriends.dto.UserProfileDTO;
import com.alibaba.rfq.sourcingfriends.msgcenter.TradeMessageActivity.TradeMsg;
import com.alibaba.rfq.sourcingfriends.service.XmppService;
import com.alibaba.rfq.sourcingfriends.xmpp.impl.TimeRender;
import com.alibaba.rfq.sourcingfriends.xmpp.impl.XmppConnectionImpl;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class IngMessageActivity extends Activity {

    class Msg {

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

    private static final String  SERVER_DOMAIN   = "sf.alibaba.com";
    private static final int     ING_MSG_RECEIVE = 4;
    // Debugging
    private static final String  TAG             = "IngMessageActivity";
    private static final boolean D               = true;

    private Context              gContext;
    private TextView             user2TalkTextView;
    private ListView             mConversationView;
    private EditText             mOutEditText;
    private Button               mSendButton;

    // Array adapter for the conversation thread
    private IngMessageAdapter    mConversationIngMsgAdapter;

    private DatabaseService      db              = null;
    private UserProfileDTO       she             = null;
    private UserProfileDTO       me              = null;

    // xmpp
    private List<Msg>            listMsg         = null;
    private IngMsgReceiver       receiver;
    private ChatManager          cm;
    private String               userName2Talk;
    private static Chat          user2Chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ing_message);

        gContext = this;

        Intent intent = getIntent();
        userName2Talk = intent.getStringExtra(TradeMessageActivity.USER_NAME_TO_TALK);

        listMsg = getData();

        //
        MsgIng();

        // xmpp st to userIdStr
        cm = XmppConnectionImpl.getConnection().getChatManager();
        Log.i("IngMessageActivity", userName2Talk + "<->" + LoginActivity.loginAct.getAccount());
        user2Chat = cm.createChat(userName2Talk, null);

        // register xmpp service com.alibaba.rfq.sourcingfriends.service.XmppService
        receiver = new IngMsgReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(XmppService.actionName);
        registerReceiver(receiver, filter);

        // tem for add person to sqlite
        Bitmap shePhoto = BitmapFactory.decodeResource(this.getResources(), R.drawable.user_she_photo);
        Bitmap mePhoto = BitmapFactory.decodeResource(this.getResources(), R.drawable.user_me_photo);

        db = new DatabaseService(this);
        Cursor cursor = null;
        Boolean flag = false;
        // set she from db
        cursor = db.select2DO(DbConstant.UP_TABLE_NAME, new String[] { DbConstant.UP_NAME, DbConstant.UP_PHOTO,
                                      DbConstant.UP_COMPANY, DbConstant.UP_MAIN_PRODUCTS },
                              new String[] { DbConstant.UP_NAME },
                              new String[] { userName2Talk });

        flag = false;
        while (cursor.moveToNext()) {
            flag = true;

            String name = cursor.getString(cursor.getColumnIndex(DbConstant.UP_NAME));
            String company = cursor.getString(cursor.getColumnIndex(DbConstant.UP_COMPANY));
            String mainProducts = cursor.getString(cursor.getColumnIndex(DbConstant.UP_MAIN_PRODUCTS));
            byte[] in = cursor.getBlob(cursor.getColumnIndex(DbConstant.UP_PHOTO));
            Bitmap bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
            she = new UserProfileDTO(name, bmpout, company, mainProducts);
            break;
        }
        if (false == flag) {
            she = new UserProfileDTO(userName2Talk, shePhoto, "MS", "Mp3、Mp4、iphone11...");
            Log.i(TAG, "There is not user:" + userName2Talk + "in the db");
        }

        // set me from db
        cursor = db.select2DO(DbConstant.UP_TABLE_NAME, new String[] { DbConstant.UP_NAME, DbConstant.UP_PHOTO,
                                      DbConstant.UP_COMPANY, DbConstant.UP_MAIN_PRODUCTS },
                              new String[] { DbConstant.UP_NAME },
                              new String[] { LoginActivity.loginAct.getAccount() });
        flag = false;
        while (cursor.moveToNext()) {
            flag = true;
            String name = cursor.getString(cursor.getColumnIndex(DbConstant.UP_NAME));
            String company = cursor.getString(cursor.getColumnIndex(DbConstant.UP_COMPANY));
            String mainProducts = cursor.getString(cursor.getColumnIndex(DbConstant.UP_MAIN_PRODUCTS));
            byte[] in = cursor.getBlob(cursor.getColumnIndex(DbConstant.UP_PHOTO));
            Bitmap bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
            me = new UserProfileDTO(name, bmpout, company, mainProducts);
            break;
        }
        if (false == flag) {
            me = new UserProfileDTO(LoginActivity.loginAct.getAccount(), mePhoto, "alibaba-inc", "IT、Coder...");
            Log.i(TAG, "There is not user:" + LoginActivity.loginAct.getAccount() + "in the db");
        }

    }

    private List<Msg> getData() {
        // get data from the openfire server
        listMsg = new ArrayList<Msg>();
        Log.i("IngMessageActivity", "start get the history ing msgs ");
        // db
        DatabaseService dbService = new DatabaseService(gContext);
        Cursor cursor;
        // 使用按照时间排序...
        cursor = dbService.select2DO(DbConstant.IM_TABLE_NAME, new String[] { DbConstant.IM_MSG_CONTENT,
                DbConstant.IM_MSG_IN_OR_OUT, DbConstant.IM_MSG_RECEIVED_TIME }, new String[] {
                DbConstant.IM_MSG_SENDER_NAME, DbConstant.IM_MSG_RECEIVER_NAME }, new String[] { userName2Talk,
                LoginActivity.loginAct.getAccount() });

        while (cursor.moveToNext()) {
            String aMsg = cursor.getString(cursor.getColumnIndex(DbConstant.IM_MSG_CONTENT));
            Long myDate = cursor.getLong(cursor.getColumnIndex(DbConstant.IM_MSG_RECEIVED_TIME));
            String msgInOut = cursor.getString(cursor.getColumnIndex(DbConstant.IM_MSG_IN_OR_OUT));
            Date receiveTime = new Date(myDate);

            Msg one = new Msg(userName2Talk, aMsg, receiveTime.toLocaleString(), msgInOut);
            listMsg.add(one);
            Log.i("IngMessageActivity", "history ing msgs :" + one.msg);
        }
        return listMsg;
    }

    /**
     * 广播接收器
     */
    private class IngMsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();

            String aMsg = bundle.getString(XmppService.SENDED_MSG);
            String userName = bundle.getString(XmppService.SENDERS_NAME);
            Date receiveTime = new Date(bundle.getLong(XmppService.RECEIVE_TIME));
            if (userName.equalsIgnoreCase(userName2Talk)) {
                // 获取用户、消息、时间、IN
                Msg one = new Msg(userName, aMsg, receiveTime.toLocaleString(), "IN");
                // 在handler里取出来显示消息
                android.os.Message msg = handler.obtainMessage();
                msg.what = ING_MSG_RECEIVE;
                msg.obj = one;
                msg.sendToTarget();
            }
        }
    }

    private Handler handler = new Handler() {

                                public void handleMessage(android.os.Message msg) {

                                    switch (msg.what) {
                                        case ING_MSG_RECEIVE:
                                            // 获取消息并显示
                                            Msg one = (Msg) msg.obj;
                                            listMsg.add(one);
                                            // 刷新适配器
                                            mConversationIngMsgAdapter.notifyDataSetChanged();
                                            break;
                                        default:
                                            break;
                                    }
                                };
                            };

    class IngMessageAdapter extends BaseAdapter {

        private Context context;

        public IngMessageAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return listMsg.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return listMsg.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public class ViewHolder {

            ImageView userImageView;
            TextView  theiMessage;
            ImageView myImageView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            ViewHolder viewHolder = null;

            // if (convertView == null) {
            viewHolder = new ViewHolder();

            if (listMsg.get(position).from.equalsIgnoreCase("in")) {
                convertView = LayoutInflater.from(context).inflate(R.layout.msging_in_item, null);
                viewHolder.theiMessage = (TextView) convertView.findViewById(R.id.TheInMsgTextView);
                viewHolder.userImageView = (ImageView) convertView.findViewById(R.id.UserImageView);

                viewHolder.userImageView.setImageBitmap(she.getPhoto());

                viewHolder.userImageView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent();
                        intent.putExtra(UserDetailActivity.USER_PROFILE_NAME, she.getUserName());
                        intent.setClass(context, UserDetailActivity.class);
                        context.startActivity(intent);
                    }
                });
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.msging_out_item, null);
                viewHolder.theiMessage = (TextView) convertView.findViewById(R.id.TheOutMsgTextView);

                viewHolder.myImageView = (ImageView) convertView.findViewById(R.id.MyImageView);
                viewHolder.myImageView.setImageBitmap(me.getPhoto());

                viewHolder.myImageView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent();
                        intent.putExtra(UserDetailActivity.USER_PROFILE_NAME, me.getUserName());
                        intent.setClass(context, UserDetailActivity.class);
                        context.startActivity(intent);
                    }
                });
            }

            String msg = listMsg.get(position).msg;
            viewHolder.theiMessage.setText(msg);

            return convertView;
        }
    }

    private void MsgIng() {

        user2TalkTextView = (TextView) findViewById(R.id.User2TalkTextView);
        user2TalkTextView.setText(userName2Talk);

        // Initialize the array adapter for the conversation thread
        mConversationIngMsgAdapter = new IngMessageAdapter(this);
        mConversationView = (ListView) findViewById(R.id.IngMsgListViewId);
        mConversationView.setAdapter(mConversationIngMsgAdapter);

        // Initialize the compose field with a listener for the return key
        mOutEditText = (EditText) findViewById(R.id.Text4SendEditText);
        // mOutEditText.setOnEditorActionListener(mWriteListener);

        // Initialize the send button with a listener that for click events
        mSendButton = (Button) findViewById(R.id.button_send);
        mSendButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // Send a message using content of the edit text widget
                String msg = mOutEditText.getText().toString();

                if (msg.length() > 0) {
                    if (userName2Talk.equalsIgnoreCase(SERVER_DOMAIN)) {
                        Toast.makeText(IngMessageActivity.this, "you can not send msg to " + SERVER_DOMAIN,
                                       Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // 发送消息
                    listMsg.add(new Msg(userName2Talk, msg, TimeRender.getDate(), "OUT"));
                    // 刷新适配器
                    mConversationIngMsgAdapter.notifyDataSetChanged();

                    try {
                        // 发送消息给 user2SendId [liang]
                        // db insert
                        DatabaseService dbService = new DatabaseService(gContext);
                        ContentValues value2intoIM = new ContentValues();

                        final ByteArrayOutputStream os = new ByteArrayOutputStream();
                        Bitmap bmp = she.getPhoto();
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, os);

                        value2intoIM.put(DbConstant.IM_PHOTO, os.toByteArray());
                        value2intoIM.put(DbConstant.IM_MSG_SENDER_NAME, userName2Talk);
                        value2intoIM.put(DbConstant.IM_MSG_RECEIVER_NAME, LoginActivity.loginAct.getAccount());
                        value2intoIM.put(DbConstant.IM_MSG_CONTENT, msg);
                        value2intoIM.put(DbConstant.IM_MSG_RECEIVED_TIME, new Date().getTime());
                        value2intoIM.put(DbConstant.IM_MSG_IN_OR_OUT, "OUT");

                        dbService.insertByDO(DbConstant.IM_TABLE_NAME, value2intoIM);

                        user2Chat.sendMessage(msg);
                    } catch (XMPPException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(IngMessageActivity.this, "请输入信息", Toast.LENGTH_SHORT).show();
                }
                // 清空text
                mOutEditText.setText("");
                // sendMessage;
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("IngMessageActivity", "on Start");
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        Log.i("IngMessageActivity", "on Resume");
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        Log.i("IngMessageActivity", "on Pause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("IngMessageActivity", "on Stop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        Log.i("IngMessageActivity", "on Destroy");
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.option_menu, menu);
//        return true;
//    }

}
