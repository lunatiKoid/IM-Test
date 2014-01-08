package com.alibaba.rfq.sourcingfriends.msgcenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransfer.Status;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;

import com.alibaba.rfq.sourcingfriends.LoginActivity;
import com.alibaba.rfq.sourcingfriends.R;
import com.alibaba.rfq.sourcingfriends.contactlist.UserDetailActivity;
import com.alibaba.rfq.sourcingfriends.db.DatabaseService;
import com.alibaba.rfq.sourcingfriends.dto.UserProfileDTO;
import com.alibaba.rfq.sourcingfriends.xmpp.impl.TimeRender;
import com.alibaba.rfq.sourcingfriends.xmpp.impl.XmppConnectionImpl;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class IngMessageActivity extends Activity {

	public class Msg {
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

	private List<Msg> listMsg = new ArrayList<Msg>();

	private static final String SERVER_DOMAIN = "sf.alibaba.com";
	// Debugging
	private static final String TAG = "IngMsg";
	private static final boolean D = true;

	private TextView user2TalkTextView;
	private ListView mConversationView;
	private EditText mOutEditText;
	private Button mSendButton;

	private String userIdStr = "";
	private String user2SendId ="liang@sf.alibaba.com";
	private static Chat user2Chat;
	// Array adapter for the conversation thread
	private IngMessageAdapter mConversationIngMsgAdapter;

	private DatabaseService service = null;
	private UserProfileDTO she = null;
	private UserProfileDTO me = null;

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
		Log.i(TAG, "" + she.getUserName() + " " + me.getId());
		// end

		Log.i("IngMessageActivity", LoginActivity.loginAct.getAccount());

		Intent intent = getIntent();
		userIdStr = intent.getStringExtra("UserId");

		MsgIng();

		ChatManager cm = XmppConnectionImpl.getConnection().getChatManager();
		// 发送消息给water-pc服务器water（获取自己的服务器，和好友）
		user2Chat = cm.createChat( user2SendId , null);
		
		cm.addChatListener(new ChatManagerListener() {
			@Override
			public void chatCreated(Chat chat, boolean able) {
				chat.addMessageListener(new MessageListener() {
					@Override
					public void processMessage(Chat chat2, Message message) {
						Log.v("--tags--", "--tags-form--" + message.getFrom());
						Log.v("--tags--",
								"--tags-message--" + message.getBody());

						// 收到来自 服务器 的消息
						if (message.getFrom().equalsIgnoreCase(SERVER_DOMAIN)) {
							// 获取用户、消息、时间、IN
							String[] args = new String[] { message.getFrom(),
									message.getBody(), TimeRender.getDate(),
									"IN" };

							// 在handler里取出来显示消息
							android.os.Message msg = handler.obtainMessage();
							msg.what = 1;
							msg.obj = args;
							msg.sendToTarget();

						} else {
							// message.getFrom().cantatins(获取列表上的用户，组，管理消息);
							// 获取用户、消息、时间、IN
							String[] args = new String[] { message.getFrom(),
									message.getBody(), TimeRender.getDate(),
									"IN" };

							// 在handler里取出来显示消息
							android.os.Message msg = handler.obtainMessage();
							msg.what = 1;
							msg.obj = args;
							msg.sendToTarget();

						}
						Log.i("FormClient", message.getFrom() + "-> msg:"
								+ message.getBody() + " date:"
								+ TimeRender.getDate().toString());
					}
				});
			}
		});

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 1:
				// 获取消息并显示
				String[] args = (String[]) msg.obj;
				listMsg.add(new Msg(args[0], args[1], args[2], args[3]));
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

		private UserProfileDTO useri;
		private UserProfileDTO myself;

		public IngMessageAdapter(Context context, UserProfileDTO she,
				UserProfileDTO me) {
			this.context = context;
			this.useri = she;
			this.myself = me;
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
			TextView theiMessage;
			ImageView myImageView;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			ViewHolder viewHolder = null;

			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.msging_list_view_item, null);
				viewHolder = new ViewHolder();

				viewHolder.userImageView = (ImageView) convertView
						.findViewById(R.id.UserImageView);

				viewHolder.theiMessage = (TextView) convertView
						.findViewById(R.id.TheiMsgTextView);

				viewHolder.myImageView = (ImageView) convertView
						.findViewById(R.id.MyImageView);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			String msg = listMsg.get(position).msg;
			viewHolder.theiMessage.setText(msg);

			if (listMsg.get(position).from.equalsIgnoreCase("in")) {
				viewHolder.userImageView.setImageBitmap(useri.getPhoto());
				viewHolder.userImageView
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent intent = new Intent();
								intent.putExtra("UserId", "" + useri.getId());
								intent.setClass(context,
										UserDetailActivity.class);
								context.startActivity(intent);
							}
						});
				viewHolder.theiMessage.setGravity(Gravity.LEFT);
				viewHolder.userImageView.setVisibility(View.VISIBLE);
				viewHolder.myImageView.setVisibility(View.INVISIBLE);
			} else {
				viewHolder.myImageView.setImageBitmap(myself.getPhoto());
				viewHolder.myImageView
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent intent = new Intent();
								intent.putExtra("UserId", "" + myself.getId());
								intent.setClass(context,
										UserDetailActivity.class);
								context.startActivity(intent);
							}
						});
				viewHolder.theiMessage.setGravity(Gravity.RIGHT);
				viewHolder.userImageView.setVisibility(View.INVISIBLE);
				viewHolder.myImageView.setVisibility(View.VISIBLE);
			}

			// convertView.setOnClickListener( new OnClickListener(){
			//
			// public void onClick(View v) {
			// Toast.makeText(context,"click",Toast.LENGTH_SHORT).show();
			// }
			// });
			//
			//
			// convertView.setOnLongClickListener(new OnLongClickListener(){
			// public boolean onLongClick(View v) {
			// Toast.makeText(context,"long click",Toast.LENGTH_SHORT).show();
			// return true;
			// }
			// });

			return convertView;
		}

	}

	private void MsgIng() {

		user2TalkTextView = (TextView) findViewById(R.id.User2TalkTextView);
		user2TalkTextView.setText("user:" + userIdStr);

		// Initialize the array adapter for the conversation thread
		mConversationIngMsgAdapter = new IngMessageAdapter(this, she, me);
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
					// 发送消息
					listMsg.add(new Msg( "", msg, TimeRender.getDate(),
							"OUT"));
					// 刷新适配器
					mConversationIngMsgAdapter.notifyDataSetChanged();

					try {
						// 发送消息给 user2SendId [liang] 
						user2Chat.sendMessage(msg);

					} catch (XMPPException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(IngMessageActivity.this, "请输入信息", Toast.LENGTH_SHORT)
							.show();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

}
