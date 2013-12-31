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
 * ���LoginActivity��Ҫ����ֻ��һ�������ӣ�������˵����������Ҫ���ӵ�ʱ��
 * ��ȫ����ͨ����IngMessageActivity����ʱ�����������ӣ�ֻ��Ҫ�����������Ҫ�Ĳ������ݹ�ȥ��ok��
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

	// test node.js
	private Button jGetButton;
	private Button jPostButton;
	private Button aGetButton;
	private Button aPostButton;
	private String result;

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

		// test node.js
		jGetButton = (Button) findViewById(R.id.jGetButton);
		jPostButton = (Button) findViewById(R.id.jPostButton);
		aGetButton = (Button) findViewById(R.id.aGetButton);
		aPostButton = (Button) findViewById(R.id.aPostButton);

		jGetButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new Thread(runnable).start();
			}
		});
		jPostButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			}
		});
		aGetButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			}
		});
		aPostButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
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
			if (val == null)
				val = data.getString("value");

			Toast.makeText(context, "javaGet+" + val, Toast.LENGTH_SHORT)
					.show();

			Log.i("mylog", "������Ϊ-->" + val);
		}
	};

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// TODO: http request.
			Message msg = new Message();
			Bundle data = new Bundle();

			result = NodeJSConstant.javaGet();
			if (null != result)
				data.putString("value", result);
			else
				data.putString("error", "javaGet:err");

			msg.setData(data);
			handler.sendMessage(msg);
		}
	};

}
