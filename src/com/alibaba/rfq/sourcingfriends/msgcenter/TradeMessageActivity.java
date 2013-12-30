package com.alibaba.rfq.sourcingfriends.msgcenter;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.rfq.sourcingfriends.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TradeMessageActivity extends Activity {

	private ListView tradeMsgListView;
	List<String> listData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.trade_message);

		
		// 
		
		listData = getData();
		tradeMsgListView = (ListView) findViewById(R.id.TradeMsgListViewId );
		TradeMsgAdapter tradeMsgAdapter = new TradeMsgAdapter(this, getData());
		tradeMsgListView.setAdapter(tradeMsgAdapter);
		tradeMsgListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				// TODO Auto-generated method stub

				{
					Intent intent = new Intent();
					intent.putExtra("UserId", listData.get(position));
					intent.setClass(TradeMessageActivity.this,
							IngMessageActivity.class);
					startActivity(intent);
				}

				Toast.makeText(getApplicationContext(),
						"click " + listData.get(position), Toast.LENGTH_SHORT)
						.show();
			}

		});
	}

	private List<String> getData() {
		List<String> data = new ArrayList<String>();

		data.add("1");
		data.add("2");
		data.add("3");
		data.add("4");
		data.add("5");
		data.add("6");
		data.add("7");
		data.add("8");
		data.add("9");
		data.add("10");
		data.add("11");
		data.add("12");
		data.add("13");
		return data;
	}
}
