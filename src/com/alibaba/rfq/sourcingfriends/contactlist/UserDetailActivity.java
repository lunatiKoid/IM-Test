package com.alibaba.rfq.sourcingfriends.contactlist;

import com.alibaba.rfq.sourcingfriends.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class UserDetailActivity extends Activity {

	private String userIdStr ="" ;
	
	private TextView userName;
	private TextView userCompany;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_detail);
	
		Intent intent=getIntent();
        userIdStr =intent.getStringExtra("UserId");

        userName = (TextView) findViewById(R.id.NameTextView);
        userCompany = (TextView) findViewById(R.id.companeyTextView );
        
        showUserDetail(userIdStr);
	}
	
	private void showUserDetail(String id) {
		userName.setText("user"+id);
		userCompany.setText("Alibaba");
	}
}
