package com.alibaba.rfq.sourcingfriends.msgcenter;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.rfq.sourcingfriends.R;
import com.alibaba.rfq.sourcingfriends.db.DatabaseService;
import com.alibaba.rfq.sourcingfriends.dto.UserProfileDTO;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class IngMessageActivity extends Activity {

	// Debugging
	private static final String TAG = "IngMsg";
	private static final boolean D = true;

	private TextView user2TalkTextView;
	private ListView mConversationView;
	private EditText mOutEditText;
	private Button mSendButton;

	private String userIdStr = "";

	// Array adapter for the conversation thread
	// private ArrayAdapter<String> mConversationArrayAdapter;
	private IngMessageAdapter mConversationIngMsgAdapter;

	
	private DatabaseService service = null;
	private UserProfileDTO she = null ;
	private UserProfileDTO me = null ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ing_message);

		// tem for add person to sqlite
		Bitmap photo = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.yliang);
		
		UserProfileDTO user = new UserProfileDTO("yliang", "1111",
				"alibaba-inc", photo);

		service = new DatabaseService(this);
		if (null == service.findByNamePasswd(user.getUserName(),
				user.getPasswd()))
			service.insertUserDO(user);
		she = service.findByNamePasswd("yliang", "1111");
		me = service.findByNamePasswd("yliang", "1111");
		Log.i(TAG,""+she.getUserName()+" "+me.getId());
		// end

		Intent intent = getIntent();
		userIdStr = intent.getStringExtra("UserId");
		
		MsgIng();
	}

	private List<String> getMsgData(int id) {
		List<String> data = new ArrayList<String>();

		data.add("she|1");
		data.add("she|2");
		data.add("me|3");
		data.add("she|4");
		data.add("me|5");
		data.add("me|6|hello,test");
		return data;
	}

	private void MsgIng() {

		user2TalkTextView = (TextView) findViewById(R.id.User2TalkTextView);
		user2TalkTextView.setText("user:" + userIdStr);

		// Initialize the array adapter for the conversation thread
		mConversationIngMsgAdapter = new IngMessageAdapter(this, getMsgData(0),
				she, me);
		mConversationView = (ListView) findViewById(R.id.IngMsgListViewId);
		mConversationView.setAdapter(mConversationIngMsgAdapter);

		// Initialize the compose field with a listener for the return key
		mOutEditText = (EditText) findViewById(R.id.Text4SendEditText);
		//mOutEditText.setOnEditorActionListener(mWriteListener);

		// Initialize the send button with a listener that for click events
		mSendButton = (Button) findViewById(R.id.button_send);
		mSendButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Send a message using content of the edit text widget

				//sendMessage;
			}
		});

	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public synchronized void onResume() {
		super.onResume();
	}

	@Override
	public synchronized void onPause() {
		super.onPause();
		if (D)
			Log.e(TAG, "- ON PAUSE -");
	}

	@Override
	public void onStop() {
		super.onStop();
		if (D)
			Log.e(TAG, "-- ON STOP --");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
//	// The action listener for the EditText widget, to listen for the return key
//	private TextView.OnEditorActionListener mWriteListener = new TextView.OnEditorActionListener() {
//		public boolean onEditorAction(TextView view, int actionId,
//				KeyEvent event) {
//			// If the action is a key-up event on the return key, send the
//			// message
//			if (actionId == EditorInfo.IME_NULL
//					&& event.getAction() == KeyEvent.ACTION_UP) {
//				String message = view.getText().toString();
//				//sendMessage(message);
//			}
//			if (D)
//				Log.i(TAG, "END onEditorAction");
//			return true;
//		}
//	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

}
