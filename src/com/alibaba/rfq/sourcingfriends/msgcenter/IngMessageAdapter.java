package com.alibaba.rfq.sourcingfriends.msgcenter;

import java.util.List;

import com.alibaba.rfq.sourcingfriends.R;
import com.alibaba.rfq.sourcingfriends.dto.UserProfileDTO;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class IngMessageAdapter extends BaseAdapter {

	private LayoutInflater mInflater;

	private List<String> listData;
	private Context context;

	private UserProfileDTO useri;
	private UserProfileDTO myself;

	public IngMessageAdapter(Context context, List<String> listData,
			UserProfileDTO she, UserProfileDTO me) {
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.listData = listData;
		this.useri = she;
		this.myself = me;
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

	public class ViewHolder {
		ImageView userImageView;
		TextView theiMessage;
		ImageView myImageView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder viewHolder = null;
		// TextView itemText = null;

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

		viewHolder.userImageView.setImageBitmap(useri.getPhoto());
		viewHolder.myImageView.setImageBitmap(myself.getPhoto());
		viewHolder.theiMessage.setText(listData.get(position));

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