package com.alibaba.rfq.sourcingfriends;

import java.util.Collection;
import java.util.Properties;

import com.alibaba.rfq.sourcingfriends.db.DatabaseService;
import com.alibaba.rfq.sourcingfriends.db.DbConstant;
import com.alibaba.rfq.sourcingfriends.service.XmppService;
import com.alibaba.rfq.sourcingfriends.tarbar.ManagerCenterActivity;
import com.alibaba.rfq.sourcingfriends.xmpp.impl.XmppConnectionImpl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

public class LoginActivity extends Activity {

    public static LoginActivity loginAct;
    public static final String  USER_NAME_LOGINED = "USER_NAME_LOGINED";
    private Context             gContext;

    private static int          LOGIN_SUCCESS     = 1;
    private static int          LOGIN_ERROR       = 2;

    // server string
    private EditText            serverIpEditText;
    private String              serverIp;
    private String              account;
    private String              passwd;

    // login view
    private Button              loginButton;
    private EditText            accountEditText;
    private EditText            passwdEditText;
    private Properties          props;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the window layout
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.user_login);
        loginAct = this;
        //
        gContext = this;

        loginButton = (Button) findViewById(R.id.loginButton);
        accountEditText = (EditText) findViewById(R.id.accountEditText);
        passwdEditText = (EditText) findViewById(R.id.passwdEditText);

        // get serverIp
        serverIpEditText = (EditText) findViewById(R.id.serverIpEditTextId);

        loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                // judge the internet is connect
//                if (!isWifiConnected(gContext) ) {
//                    Toast.makeText(gContext, "ÍøÂçÎ´Á¬½Ó£¬µÇÂ¼Ê§°Ü£¬ÇëÈ·¶¨ÒÑ¾­Á¬½ÓÍøÂçÁË£¡...", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                serverIp = serverIpEditText.getText().toString();
                account = accountEditText.getText().toString();
                passwd = passwdEditText.getText().toString();

                Log.i("LoginActivity", "Login to start");
                if (!account.isEmpty() && !passwd.isEmpty()) {

                    loginThread.start();
                }
            }
        });

        if (isWifiConnected(gContext)) {
            DatabaseService dbService = new DatabaseService(gContext);
            Cursor cursor;
            cursor = dbService.select2DO(DbConstant.UL_TABLE_NAME, new String[] { DbConstant.UL_NAME,
                                                 DbConstant.UL_PASSWD }, new String[] { DbConstant.UL_ISLOGIN },
                                         new String[] { Boolean.toString(true) });
            while (cursor.moveToNext()) {
                account = cursor.getString(cursor.getColumnIndex(DbConstant.UL_NAME));
                passwd = cursor.getString(cursor.getColumnIndex(DbConstant.UL_PASSWD));
                Log.i("LoginActivity", "auto login ...");
                Toast.makeText(gContext, "×Ô¶¯µÇÂ¼...", Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                loginThread.start();

                break;
            }

        }
    }

    private Thread loginThread = new Thread(new Runnable() {

                                   public void run() {

                                       try {
                                           // Toast.makeText(gContext, "µÇÂ¼ÖÐ...", Toast.LENGTH_SHORT).show();
                                           // Á¬½Ó
                                           XmppConnectionImpl.setServerIp(serverIp);
                                           // XmppConnectionImpl.getConnection().login(account, passwd);

                                           // get the offline msgs
                                           XmppConnectionImpl.getOfflineConnection().login(account, passwd);

                                           XmppConnectionImpl.ThenGetOffMsg(XmppConnectionImpl.getConnection());

                                           // ×´Ì¬
                                           Presence presence = new Presence(Presence.Type.available);
                                           XmppConnectionImpl.getConnection().sendPacket(presence);

                                           //
                                           Intent intent = new Intent();
                                           intent.setClass(LoginActivity.this, ManagerCenterActivity.class);
                                           intent.putExtra(USER_NAME_LOGINED, account);
                                           LoginActivity.this.startActivity(intent);
                                           LoginActivity.this.finish();
                                           loginHandler.sendEmptyMessage(LOGIN_SUCCESS);

                                       } catch (XMPPException e) {
                                           XmppConnectionImpl.closeConnection();
                                           loginHandler.sendEmptyMessage(LOGIN_ERROR);
                                       }
                                   }
                               });

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public boolean isWifiConnected(Context context) {

        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    private Handler loginHandler = new Handler() {

                                     public void handleMessage(android.os.Message msg) {

                                         if (msg.what == LOGIN_SUCCESS) {

                                             // start xmpp service
                                             Intent intent = new Intent(gContext, XmppService.class);
                                             startService(intent);

                                             Toast.makeText(gContext, "µÇÂ¼...£¡", Toast.LENGTH_SHORT).show();

                                         } else if (msg.what == LOGIN_ERROR) {

                                             Toast.makeText(gContext, "µÇÂ¼Ê§°Ü£¡", Toast.LENGTH_SHORT).show();
                                         }
                                     };
                                 };

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
