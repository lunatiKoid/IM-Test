package com.alibaba.rfq.sourcingfriends;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

    ImageView homeBackGroundImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        RelativeLayout homeLayout = (RelativeLayout) findViewById(R.id.home_background_layout);
        Bitmap photo = BitmapFactory.decodeResource(this.getResources(), R.drawable.sf_home_background);
        homeLayout.setBackgroundDrawable(new BitmapDrawable(photo));

        new Thread(new Runnable() {

            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();

        Intent intent = new Intent();
        intent.setClass(MainActivity.this, LoginActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
