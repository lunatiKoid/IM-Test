package com.alibaba.rfq.sourcingfriends;

import java.util.Properties;

import org.androidpn.client.ServiceManager;
import com.alibaba.rfq.sourcingfriends.tarbar.ManagerCenterActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 这里，LoginActivity需要做的只是一个假连接，或者来说，真正的需要连接的时候，
 * 完全可以通过在IngMessageActivity调用时，真正的连接，只需要把相关连接需要的参数传递过去就ok了
 * 
 * */

public class LoginActivity extends Activity {

	private Context gContext;

	// server
	private EditText serverIpEditText;

	// login
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

		// connect to the server
		serverIpEditText = (EditText) findViewById(R.id.serverIpEditTextId);

		loginButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				props = new Properties();
				props.setProperty("xmppHost", serverIpEditText.getText()
						.toString());
				props.setProperty("apiKey", accountEditText.getText()
						.toString());
				props.setProperty("xmppPort", "5222");

				class myThread extends Thread {
					public myThread() {
						Log.i("LoginActivity", "create a Thread");
					}

					public void run() {
						// connect to server
						ServiceManager serviceManager = new ServiceManager(
								gContext, props);
						serviceManager
								.setNotificationIcon(R.drawable.notification);
						serviceManager.startService();
					}
				};

				myThread aThread = new myThread();
				aThread.run();
				Log.i("LoginActivity", "It's Here");
				// Intent intent = new Intent();
				// intent.setClass(LoginActivity.this,
				// ManagerCenterActivity.class);
				// startActivity(intent);

			}
		});

		// ended

	}
}
