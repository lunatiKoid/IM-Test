package com.alibaba.rfq.sourcingfriends;

import java.util.Collection;
import java.util.Properties;
import com.alibaba.rfq.sourcingfriends.tarbar.ManagerCenterActivity;
import com.alibaba.rfq.sourcingfriends.xmpp.impl.XmppConnectionImpl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

    private Context             gContext;

    private static int          LOGIN_SUCCESS = 1;
    private static int          LOGIN_ERROR   = 2;

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

                serverIp = serverIpEditText.getText().toString();
                account = accountEditText.getText().toString();
                passwd = passwdEditText.getText().toString();

                Log.i("LoginActivity", "It's Here");
                if (!account.isEmpty() && !passwd.isEmpty()) {
                    new Thread(new Runnable() {

                        public void run() {

                            try {
                                // Á¬½Ó
                                XmppConnectionImpl.setServerIp(serverIp);
                                // XmppConnectionImpl.getConnection().login(account, passwd);

                                // get the offline msgs
                                XmppConnectionImpl.getOfflineConnection().login(account, passwd);

                                XmppConnectionImpl.ThenGetOffMsg(XmppConnectionImpl.getConnection());

                                // ×´Ì¬
                                Presence presence = new Presence(Presence.Type.available);
                                XmppConnectionImpl.getConnection().sendPacket(presence);

                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this, ManagerCenterActivity.class);
                                intent.putExtra("USERID", account);
                                LoginActivity.this.startActivity(intent);
                                LoginActivity.this.finish();
                                loginHandler.sendEmptyMessage(LOGIN_SUCCESS);
                            } catch (XMPPException e) {
                                XmppConnectionImpl.closeConnection();
                                loginHandler.sendEmptyMessage(LOGIN_ERROR);
                            }
                        }
                    }).start();
                }
            }
        });
    }

    private Handler loginHandler = new Handler() {

                                     public void handleMessage(android.os.Message msg) {

                                         if (msg.what == LOGIN_SUCCESS) {
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
