package com.alibaba.rfq.sourcingfriends.contactlist;

import java.util.Date;

import com.alibaba.rfq.sourcingfriends.R;
import com.alibaba.rfq.sourcingfriends.db.DatabaseService;
import com.alibaba.rfq.sourcingfriends.db.DbConstant;
import com.alibaba.rfq.sourcingfriends.dto.UserProfileDTO;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class UserDetailActivity extends Activity {

    public final static String USER_PROFILE_NAME = "USER_PROFILE_NAME";

    private String             userProfileName   = "";

    private TextView           userNameTextView;
    private TextView           userCompanyTextView;
    private ImageView          userPhotoImageView;
    private TextView           userAddressTextView;
    private TextView           userMiniSiteTextView;
    private TextView           userAddedTextView;
    private TextView           userMainProductsTextView;
    private TextView           userRecentQuotedByRequestTextView;
    private TextView           userQuotesTotalLastMonthTextView;
    private TextView           userQuotesAcceptedLastMonthTextView;

    private DatabaseService    db                = null;
    private UserProfileDTO     user              = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.user_detail);

        Intent intent = getIntent();
        userProfileName = intent.getStringExtra(UserDetailActivity.USER_PROFILE_NAME);

        userNameTextView = (TextView) findViewById(R.id.userNameTextView);
        userCompanyTextView = (TextView) findViewById(R.id.userCompanyTextView);
        userPhotoImageView = (ImageView) findViewById(R.id.userPhotoImageView);
        userAddressTextView = (TextView) findViewById(R.id.userAddressTextView);
        userMiniSiteTextView = (TextView) findViewById(R.id.userMiniSiteTextView);
        userAddedTextView = (TextView) findViewById(R.id.userAddedTextView);
        userMainProductsTextView = (TextView) findViewById(R.id.userMainProductsTextView);
        userRecentQuotedByRequestTextView = (TextView) findViewById(R.id.userRecQuoRequestTextView);
        userQuotesTotalLastMonthTextView = (TextView) findViewById(R.id.userQuotesTotalLastMonthTextView);
        userQuotesAcceptedLastMonthTextView = (TextView) findViewById(R.id.userQuoAcLastMonthTextView);

        showUserDetail(userProfileName);
    }

    private void showUserDetail(String userProfileName) {
        // db service
        db = new DatabaseService(this);

        // tem for add person to sqlite
        Bitmap mePhoto = BitmapFactory.decodeResource(this.getResources(), R.drawable.user_me_photo);

        db = new DatabaseService(this);
        Cursor cursor = null;
        Boolean flag = false;
        // set she from db
        cursor = db.select2DO(DbConstant.UP_TABLE_NAME, new String[] { DbConstant.UP_NAME, DbConstant.UP_PHOTO,
                                      DbConstant.UP_COMPANY, DbConstant.UP_MAIN_PRODUCTS },
                              new String[] { DbConstant.UP_NAME },
                              new String[] { userProfileName });

        flag = false;
        while (cursor.moveToNext()) {
            flag = true;

            String name = cursor.getString(cursor.getColumnIndex(DbConstant.UP_NAME));
            String company = cursor.getString(cursor.getColumnIndex(DbConstant.UP_COMPANY));
            String mainProducts = cursor.getString(cursor.getColumnIndex(DbConstant.UP_MAIN_PRODUCTS));
            byte[] in = cursor.getBlob(cursor.getColumnIndex(DbConstant.UP_PHOTO));
            Bitmap bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
            user = new UserProfileDTO(name, bmpout, company, mainProducts);
            break;
        }
        if (false == flag) {
            user = new UserProfileDTO(userProfileName, mePhoto, "--------", "---------");
            Log.i("UserDetailActivity", "There is not this user:" + userProfileName
                                        + "in the db,pls implement download service");
        }

        userNameTextView.setText(userProfileName);
        userCompanyTextView.setText(user.getCompanyName());
        userPhotoImageView.setImageBitmap(user.getPhoto());
        userAddressTextView.setText(user.getAddress());
        userMiniSiteTextView.setText(user.getMiniSite());
        userAddedTextView.setText(new Date(user.getAdded()).toLocaleString());
        userMainProductsTextView.setText(user.getMainProducts());
        userRecentQuotedByRequestTextView.setText(user.getRecentlyQuotedBRequest());
        userQuotesTotalLastMonthTextView.setText(user.getQuoTotalLastMonth());
        userQuotesAcceptedLastMonthTextView.setText(user.getQuoAcLastMonth());

    }
}
