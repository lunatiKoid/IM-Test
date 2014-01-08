package com.alibaba.rfq.sourcingfriends;

import java.util.Collection;
import java.util.Properties;
import com.alibaba.rfq.sourcingfriends.tarbar.ManagerCenterActivity;
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

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;


public class LoginActivity extends Activity {

	private Context gContext;

	// server string
	private EditText serverIpEditText;
	private String serverIp;
	private String account;
	private String passwd;
	
	// login view
	private Button loginButton;
	private EditText accountEditText;
	private EditText passwdEditText;
	private Properties props;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set up the window layout
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_login);

		//
		gContext = this;

		loginButton = (Button) findViewById(R.id.loginButton);
		accountEditText = (EditText) findViewById(R.id.accountEditText);
		passwdEditText = (EditText) findViewById(R.id.passwdEditText);

		// get serverIp
		serverIpEditText = (EditText) findViewById(R.id.serverIpEditTextId);

		loginButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				Log.i("LoginActivity", "It's Here");
				// Intent intent = new Intent();
				// intent.setClass(LoginActivity.this,
				// ManagerCenterActivity.class);
				// startActivity(intent);
			}
		});

	}
}
