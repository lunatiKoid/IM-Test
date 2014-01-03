package com.alibaba.rfq.sourcingfriends;

import com.alibaba.rfq.sourcingfriends.service.BluetoothChatService;
import com.alibaba.rfq.sourcingfriends.service.DeviceListActivity;
import com.alibaba.rfq.sourcingfriends.tarbar.ManagerCenterActivity;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.alibaba.rfq.sourcingfriends.service.nodejs.NodeJSConstant;

/**
 * 这里，LoginActivity需要做的只是一个假连接，或者来说，真正的需要连接的时候，
 * 完全可以通过在IngMessageActivity调用时，真正的连接，只需要把相关连接需要的参数传递过去就ok了
 * 
 * */

public class LoginActivity extends Activity {

	private Context context;

	// login
	private Button loginButton;
	private EditText accountEditText;
	private EditText passwdEditText;

	// Bluetooth service is ok
	private boolean isServiceOk = false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set up the window layout
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_login);

		//
		context = this;

		loginButton = (Button) findViewById(R.id.loginButton);
		accountEditText = (EditText) findViewById(R.id.accountEditText);
		passwdEditText = (EditText) findViewById(R.id.passwdEditText);

		isServiceOk = true;

		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (true == isServiceOk) {
					// use accountEditText & passwdEditText to test the user is
					// valid ???
					{
						Intent intent = new Intent();
						intent.setClass(LoginActivity.this,
								ManagerCenterActivity.class);
						startActivity(intent);
					}
				} else {
					Toast.makeText(
							context,
							"service is not ok,pls be sure linked to a device,try connect a device",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	// thread
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String val = data.getString("error");
			if (val != null) {
				Toast.makeText(context, "err", Toast.LENGTH_SHORT).show();
			} else if ((val = data.getString("jGet:value")) != null) {
				Toast.makeText(context, val, Toast.LENGTH_SHORT).show();
			} else if ((val = data.getString("jPost:value")) != null) {
				Toast.makeText(context, val, Toast.LENGTH_SHORT).show();
			} else if ((val = data.getString("aGet:value")) != null) {
				Toast.makeText(context, val, Toast.LENGTH_SHORT).show();
			} else if ((val = data.getString("aPost:value")) != null) {
				Toast.makeText(context, val, Toast.LENGTH_SHORT).show();
			} else
				Toast.makeText(context, "err", Toast.LENGTH_SHORT).show();

			// Toast.makeText(context, "javaGet+" + val,
			// Toast.LENGTH_SHORT).show();

			Log.i("login", "请求结果为-->" + val);
		}
	};

}
