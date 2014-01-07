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
 * ���LoginActivity��Ҫ����ֻ��һ�������ӣ�������˵����������Ҫ���ӵ�ʱ��
 * ��ȫ����ͨ����IngMessageActivity����ʱ�����������ӣ�ֻ��Ҫ�����������Ҫ�Ĳ������ݹ�ȥ��ok��
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
