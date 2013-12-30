package com.alibaba.rfq.sourcingfriends;

import com.alibaba.rfq.sourcingfriends.service.BluetoothChatService;
import com.alibaba.rfq.sourcingfriends.service.DeviceListActivity;
import com.alibaba.rfq.sourcingfriends.tarbar.ManagerCenterActivity;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


	/**
	 *  这里，LoginActivity需要做的只是一个假连接，或者来说，真正的需要连接的时候，
	 *  完全可以通过在IngMessageActivity调用时，真正的连接，只需要把相关连接需要的参数传递过去就ok了
	 * 
	 * */

public class LoginActivity extends Activity {
  
	private Context context ;
    
    // login
 	Button loginButton ;
 	EditText accountEditText ;
 	EditText passwdEditText;
 	
    // Bluetooth service is ok
    private boolean isServiceOk = false ;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        // Set up the window layout
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.user_login);
		
        //
		context = this;
		
		loginButton = (Button) findViewById(R.id.loginButton);
		accountEditText = (EditText) findViewById(R.id.accountEditText);
		passwdEditText = (EditText) findViewById(R.id.passwdEditText);
		     
        isServiceOk = true; 
        
		loginButton.setOnClickListener( new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if( true == isServiceOk )
				{
					// use accountEditText & passwdEditText to test the user is valid ???
					{
					Intent intent = new Intent();
			        intent.setClass(LoginActivity.this, ManagerCenterActivity.class);
			        startActivity(intent);
					}
				}
				else
				{
					Toast.makeText(context, "service is not ok,pls be sure linked to a device,try connect a device", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

//	@Override
//    public void onStart() {
//        super.onStart();
//        if(D) Log.e(TAG, "++ ON START ++");
//
//        // If BT is not on, request that it be enabled.
//        // setupChat() will then be called during onActivityResult
//        if (!mBluetoothAdapter.isEnabled()) {
//            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
//        // Otherwise, setup the chat session
//        } else {
//            //if (mChatService == null) setupChat();
//        }
//    }
//
//    @Override
//    public synchronized void onResume() {
//        super.onResume();
//        if(D) Log.e(TAG, "+ ON RESUME +");
//
//        // Performing this check in onResume() covers the case in which BT was
//        // not enabled during onStart(), so we were paused to enable it...
//        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
////        if (mChatService != null) {
////            // Only if the state is STATE_NONE, do we know that we haven't started already
////            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
////              // Start the Bluetooth chat services
////              mChatService.start();
////            }
////        }
//    }
//
//	/**
//     *  Menu settup
//     *  select a way to connect server
//     * */
//    
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.option_menu, menu);
//        return true;
//    }
//    
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        return onItemSelected(this,item.getItemId());
//    }
//
//    
//    public boolean onItemSelected(Context context,int id) {
//    	
//    	Intent serverIntent = null;
//        switch (id) {
//        case R.id.secure_connect_scan:
//            // Launch the DeviceListActivity to see devices and do scan
//            serverIntent = new Intent(context, DeviceListActivity.class);
//            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
//            return true;
//        case R.id.insecure_connect_scan:
//            // Launch the DeviceListActivity to see devices and do scan
//            serverIntent = new Intent(context, DeviceListActivity.class);
//            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
//            return true;
//        case R.id.discoverable:
//            // Ensure this device is discoverable by others
//            ensureDiscoverable();
//            return true;
//        }
//        return false;
//        
//    }
//    
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(D) Log.d(TAG, "onActivityResult " + resultCode);
//        switch (requestCode) {
//        case REQUEST_CONNECT_DEVICE_SECURE:
//            // When DeviceListActivity returns with a device to connect
//            if (resultCode == Activity.RESULT_OK) {
//            	setConnectDeviceAddress(data, true);
//            }
//            break;
//        case REQUEST_CONNECT_DEVICE_INSECURE:
//            // When DeviceListActivity returns with a device to connect
//            if (resultCode == Activity.RESULT_OK) {
//            	setConnectDeviceAddress(data, false);
//            }
//            break;
//        case REQUEST_ENABLE_BT:
//            // When the request to enable Bluetooth returns
//            if (resultCode == Activity.RESULT_OK) {
//                // Bluetooth is now enabled
//                setupChat();	// liang.yaol
//            } else {
//                // User did not enable Bluetooth or an error occured
//                Log.d(TAG, "BT not enabled");
//                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        }
//    }
//    
//    
//    
//    private void setupChat() {
//        Log.d(TAG, "setupChat()");
//        
//        isServiceOk=true;
//    }	
//    
//  	private void setConnectDeviceAddress(Intent data, boolean secure) {
//        // Get the device MAC address
//        this.deviceAddress = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
//        this.isSecure = secure ;
//        
//        setupChat();
//        Toast.makeText(this, "connect to device:"+deviceAddress, Toast.LENGTH_SHORT).show();
////        // Get the BLuetoothDevice object
////        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
////        
////        // Attempt to connect to the device
////        mChatService.connect(device, secure);
//    }
//	
//	public String getConnectDeviceAddress() {
//		return this.deviceAddress;
//	}
//	
//	private void ensureDiscoverable() {
//        if(D) Log.d(TAG, "ensure discoverable");
//        if (mBluetoothAdapter.getScanMode() !=
//            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
//            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
//            startActivity(discoverableIntent);
//        }
//    }
}
