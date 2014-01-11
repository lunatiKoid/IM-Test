package com.alibaba.rfq.sourcingfriends.contactlist;

import com.alibaba.rfq.sourcingfriends.R;
import com.alibaba.rfq.sourcingfriends.db.DatabaseService;
import com.alibaba.rfq.sourcingfriends.dto.UserProfileDTO;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class UserDetailActivity extends Activity {

    private String          userIdStr = "";

    private TextView        userName;
    private TextView        userCompany;
    private ImageView       userImageView;

    private DatabaseService service   = null;
    private UserProfileDTO  user      = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.user_detail);

        Intent intent = getIntent();
        userIdStr = intent.getStringExtra("UserId");

        // db service
        service = new DatabaseService(this);
        // user = service.findById(Integer.parseInt(userIdStr));

        userName = (TextView) findViewById(R.id.NameTextView);
        userCompany = (TextView) findViewById(R.id.companeyTextView);
        userImageView = (ImageView) findViewById(R.id.userPhotoImageView);

        showUserDetail(userIdStr);
    }

    private void showUserDetail(String id) {
        userName.setText(user.getUserName());
        userCompany.setText(user.getCompanyName());
        userImageView.setImageBitmap(user.getPhoto());
    }
}
