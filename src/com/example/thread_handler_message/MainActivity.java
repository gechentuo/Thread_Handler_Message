package com.example.thread_handler_message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

 

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final int LOAD = 1;
	private TextView textview;
	private Dialog dialog;
	private Button btn;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOAD:
				// update UI
				byte[] result = (byte[])msg.obj;
				textview.setText(String.valueOf(result.length));
				dialog.show();
				break;
			}
		};
	};

	public void loadData(View view) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				byte[] result = getContent();
				mHandler.sendMessage(Message.obtain(mHandler, LOAD, result));
			}
		}).start();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		textview = new TextView(MainActivity.this);
		dialog = builder.setTitle("www.baidu.com").setView(textview).create();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private byte[] getContent() {
		URL url;
		String content = "";
		try {
			url = new URL("http://baidu.com");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			content = readStream(con.getInputStream());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content.getBytes();
	}

	private String readStream(InputStream in) {
		BufferedReader reader = null;
		StringBuffer all = new StringBuffer();

		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String data = "";
			while ((data = reader.readLine()) != null) {

				all.append(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return all.toString();

	}
}
