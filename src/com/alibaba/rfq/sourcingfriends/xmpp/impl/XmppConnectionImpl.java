package com.alibaba.rfq.sourcingfriends.xmpp.impl;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.util.Log;

public class XmppConnectionImpl {
	
	private static XMPPConnection con = null;
	private static String serverIp = "192.168.0.121" ;
	
	public static void setServerIp(String ip){
		serverIp=ip;
	}
	
	private static void openConnection() {
		try {
			//url���˿ڣ�Ҳ�����������ӵķ��������֣���ַ���˿ڣ��û���
			ConnectionConfiguration connConfig = new ConnectionConfiguration(serverIp, 5222);
			con = new XMPPConnection(connConfig);
			con.connect();
		}
		catch (XMPPException xe) 
		{
			xe.printStackTrace();
		}
	}

	public static XMPPConnection getConnection() {
		//Log.i("XmppConnectionImpl",serverIp);
		if (con == null) {
			openConnection();
		}
		return con;
	}

	public static void closeConnection() {
		con.disconnect();
		con = null;
	}
}
