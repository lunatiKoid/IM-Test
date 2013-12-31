package com.alibaba.rfq.sourcingfriends.service.nodejs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

public class NodeJSConstant {
	public static final String NODEJS_SERVER_URL = "http://10.17.200.22:8080/";

	public static String javaGet() {
		String str = "";
		try {
			str = URLEncoder.encode("◊•Õ€", "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		URL url = null;
		try {
			url = new URL(NODEJS_SERVER_URL + "?name=javaGet" + str);
		} catch (MalformedURLException e) {
		}

		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			//textView1.setText(e.getMessage());
			Log.i("NODE_JS","javaGet+1"+e.getMessage());
			return null;
		}
		// method The default value is "GET".
		return getResponseJava(urlConnection);
	}

	public static String javaPost() {
		URL url = null;
		try {
			url = new URL(NODEJS_SERVER_URL);
		} catch (MalformedURLException e) {
		}

		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			//textView1.setText(e.getMessage());
			Log.i("NODE_JS","javaPost+1"+e.getMessage());
			return null;
		}
		try {
			urlConnection.setRequestMethod("POST");
		} catch (ProtocolException e) {
		}
		urlConnection.setDoOutput(true);
		urlConnection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");

		OutputStream out = null;
		try {
			out = new BufferedOutputStream(urlConnection.getOutputStream());// «Î«Û
		} catch (IOException e) {
			urlConnection.disconnect();
			//textView1.setText(e.getMessage());
			Log.i("NODE_JS","javaPost+2"+e.getMessage());
			return null;
		}

		String str = "";
		try {
			str = URLEncoder.encode("◊•Õ€", "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		Writer writer = null;
		try {
			writer = new OutputStreamWriter(out, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
		}
		try {
			writer.write("name=javaPost" + str);
		} catch (IOException e) {
			urlConnection.disconnect();
			//textView1.setText(e.getMessage());
			Log.i("NODE_JS","javaPost+3"+e.getMessage());
			return null;
		} finally {
			try {
				writer.flush();
				writer.close();
			} catch (IOException e) {
			}
		}

		return getResponseJava(urlConnection);
	}

	public static String apacheGet() {
		HttpGet request = new HttpGet(NODEJS_SERVER_URL + "?name=apacheGet∞¢≈¡∆Ê");
		return getResponseApache(request);
	}

	public static String apachePost() {
		HttpPost request = new HttpPost(NODEJS_SERVER_URL);

		List<NameValuePair> params = new ArrayList<NameValuePair>(1);
		params.add(new BasicNameValuePair("name", "apachePost∞¢≈¡∆Ê"));
		HttpEntity formEntity = null;
		try {
			formEntity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
		}
		request.setEntity(formEntity);
		return getResponseApache(request);
	}

	private static String getResponseJava(HttpURLConnection urlConnection) {
		InputStream in = null;
		try {
			in = new BufferedInputStream(urlConnection.getInputStream());// œÏ”¶
		} catch (IOException e) {
			urlConnection.disconnect();
			Log.i("NODE_JS","getResponseJava+1"+e.getMessage());
			return null;
		}

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
		}
		StringBuilder result = new StringBuilder();
		String tmp = null;
		try {
			while ((tmp = reader.readLine()) != null) {
				result.append(tmp);
			}
		} catch (IOException e) {
			Log.i("NODE_JS","getResponseJava+2"+e.getMessage());
			return null;
		} finally {
			try {
				reader.close();
				urlConnection.disconnect();
			} catch (IOException e) {
			}
		}
		return result.toString();
	}

	private static String getResponseApache(HttpUriRequest request) {
		

		HttpClient client = new DefaultHttpClient();
		HttpResponse response = null;
		try {
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			Log.i("NODE_JS","getResponseApache+1"+e.getMessage());
		} catch (IOException e) {
			Log.i("NODE_JS","getResponseApache+2"+e.getMessage());
		}

		if (response == null) {
			return null;
		}

		String result = null;
		if (response.getStatusLine().getStatusCode() == 200) {
			try {
				result = EntityUtils.toString(response.getEntity(), "UTF-8");
			} catch (ParseException e) {
				result = e.getMessage();
			} catch (IOException e) {
				result = e.getMessage();
			}
		} else {
			result = "error response" + response.getStatusLine().toString();
		}
		
		return result;
		
	}	
}
