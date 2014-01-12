package com.alibaba.rfq.sourcingfriends.contactlist;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.rfq.sourcingfriends.R;
import com.alibaba.rfq.sourcingfriends.msgcenter.IngMessageActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ContactListActivity extends Activity {

    ListView     contactListView;
    List<String> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.contact_list);

        contactListView = (ListView) findViewById(R.id.ContactListViewId);

        listData = getContactData();
        ContactListAdapter contactListAdapter = new ContactListAdapter(this, listData);
        contactListView.setAdapter(contactListAdapter);
        contactListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // TODO Auto-generated method stub
                // Intent intent = new Intent();
                // intent.putExtra("UserId", listData.get(position));
                // intent.setClass(ContactListActivity.this, IngMessageActivity.class);
                // startActivity(intent);

                Toast.makeText(getApplicationContext(), "sorry,此功能还未实现...", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private List<String> getContactData() {
        List<String> data = new ArrayList<String>();
        data.add("客户A");
        data.add("客户B");
        data.add("客户C");
        data.add("客户D");
        data.add("客户E");
        data.add("客户F");
        data.add("客户G");
        data.add("客户H");
        data.add("客户I");
        data.add("客户JJ");
        data.add("客户K");
        return data;
    }

    class ContactListAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private List<String>   listData;
        private Context        context;

        public ContactListAdapter(Context context, List<String> listData) {
            this.context = context;
            this.mInflater = LayoutInflater.from(context);
            this.listData = listData;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            TextView itemText = null;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.contact_list_item, null);
                itemText = (TextView) convertView.findViewById(R.id.contactUserNameTextView);
                convertView.setTag(itemText);
            } else {
                itemText = (TextView) convertView.getTag();
            }

            String sData = (String) getItem(position);

            itemText.setText(sData);

            return convertView;
        }

    }
}
