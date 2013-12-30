package com.alibaba.rfq.sourcingfriends.contactlist;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.rfq.sourcingfriends.R;
import com.alibaba.rfq.sourcingfriends.msgcenter.IngMessageActivity;
import com.alibaba.rfq.sourcingfriends.msgcenter.TradeMessageActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ContactListActivity extends Activity {
	
	ListView contactListView;
	List<String> listData ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_list);
		

		contactListView = (ListView) findViewById(R.id.ContactListViewId);
        
        listData = getContactData();
        ContactListAdapter contactListAdapter = new ContactListAdapter(this,listData);
        contactListView.setAdapter(contactListAdapter);
        contactListView.setOnItemClickListener( new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id ) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("UserId", listData.get(position));
				intent.setClass(ContactListActivity.this, UserDetailActivity.class);
				startActivity(intent);
				Toast.makeText(getApplicationContext(),"click "+listData.get(position),Toast.LENGTH_SHORT).show();
			} 
        	
        }) ;
	}

	private List<String> getContactData() {
		List<String> data = new ArrayList<String>() ;
    	
    	data.add("user 1");
    	data.add("user 2");
    	data.add("user 3");
    	data.add("user 4");
    	data.add("user 5");
    	data.add("user 6");
    	data.add("user 7");
    	data.add("user 8");
    	data.add("user 9");
    	data.add("user 10");
    	data.add("user 11");
    	data.add("user 12");
    	data.add("user 13");
    	return data;	
	}
	
}
