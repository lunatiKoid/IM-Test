package com.alibaba.rfq.sourcingfriends.msgcenter;

import java.util.List;

import com.alibaba.rfq.sourcingfriends.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class IngMessageAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<String> listData ;
	private Context context;
	
	public IngMessageAdapter(Context context,List<String> listData) {
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.listData = listData;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listData.size() ;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position ;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		
		
		TextView itemText = null ;
		
		if( convertView == null ) {
			convertView = LayoutInflater.from(context).inflate(R.layout.main_list_view_item, null);
			itemText = (TextView) convertView.findViewById( R.id.ItemText ) ;
			convertView.setTag(itemText);
		} else {
			itemText = (TextView)convertView.getTag();
		}
		
		String sData = (String) getItem(position);
		
		itemText.setText( sData );
		
/*		//对ListView中的每一行信息配置OnClick事件  
        convertView.setOnClickListener( new OnClickListener(){  
        	
            public void onClick(View v) {  
            	Toast.makeText(context,"点击�?,Toast.LENGTH_SHORT).show();  
            }    
        });  
          
        //对ListView中的每一行信息配置OnLongClick事件  
        convertView.setOnLongClickListener(new OnLongClickListener(){  
            public boolean onLongClick(View v) {  
            	Toast.makeText(context,"long点击�?,Toast.LENGTH_SHORT).show(); 
                return true;  
            }  
        }); */
		
		return convertView;
	}

}