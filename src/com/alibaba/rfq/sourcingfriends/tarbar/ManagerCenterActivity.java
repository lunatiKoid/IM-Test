package com.alibaba.rfq.sourcingfriends.tarbar;


import java.util.ArrayList;
import java.util.List;

import com.alibaba.rfq.sourcingfriends.R;
import com.alibaba.rfq.sourcingfriends.R.id;
import com.alibaba.rfq.sourcingfriends.R.layout;
import com.alibaba.rfq.sourcingfriends.contactlist.ContactListActivity;
import com.alibaba.rfq.sourcingfriends.msgcenter.TradeMessageActivity;

import android.app.ActivityGroup;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;


public class ManagerCenterActivity extends ActivityGroup implements OnCheckedChangeListener {

	private int btnWidth = 64;
	private LinearLayout contentViewLayout;
	private RadioGroup tabBar;
	private List<TabBarButton> buttonList;
	
	private RadioGroup.LayoutParams buttonLayoutParams;
	   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tabbar);
		
		
		// 
		contentViewLayout = (LinearLayout) findViewById(R.id.contentViewLayout);
		tabBar = (RadioGroup) findViewById(R.id.tabBar);
		tabBar.setOnCheckedChangeListener(this);
		
		buttonList = new ArrayList<TabBarButton>();
		
		addTabButton(R.string.message_center, R.drawable.tabbar_msg, new Intent(this, TradeMessageActivity.class));	
		addTabButton(R.string.contact_list, R.drawable.tabbar_contact, new Intent(this,ContactListActivity.class));
		
		commit();
		
	}
	
	public void addTabButton(int label, int imageId, Intent intent) {
		TabBarButton btn = new TabBarButton(this);
		btn.setState(imageId, label, intent);
		buttonList.add(btn);
	}

	public void commit() {
		tabBar.removeAllViews();

		WindowManager windowManager = getWindowManager();
		int windowWidth = windowManager.getDefaultDisplay().getWidth();

		Log.i("ManagerCenterActivity",""+windowWidth);
		
		int btnNum = windowWidth / 64;

		if (buttonList.size() < btnNum) {
			btnWidth = windowWidth / buttonList.size();
		}
		ButtonStateDrawable.WIDTH = btnWidth;
		
		buttonLayoutParams = new RadioGroup.LayoutParams(btnWidth,
				LayoutParams.WRAP_CONTENT);

		for (int i = 0; i < buttonList.size(); i++) {
			TabBarButton btn = buttonList.get(i);
			btn.setId(i);
			tabBar.addView(btn, i, buttonLayoutParams);
		}
		setCurrentTab(0);
	}

	public void setCurrentTab(int index) {

		tabBar.check(index);

		contentViewLayout.removeAllViews();
		TabBarButton btn = (TabBarButton) tabBar.getChildAt(index);
		
		View tabView = getLocalActivityManager().startActivity(
				getResources().getString(btn.getLabel()), btn.getIntent()
				).getDecorView();

		contentViewLayout.addView(tabView, new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}

	public void onCheckedChanged(RadioGroup group, int checkedId) {
		setCurrentTab(checkedId);
	}

	class ChangeTabBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			int curIndex = intent.getExtras().getInt("CurIndex");
			setCurrentTab(curIndex);
		}

	}

}
