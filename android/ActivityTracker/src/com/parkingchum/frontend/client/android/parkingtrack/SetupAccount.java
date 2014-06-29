package com.parkingchum.frontend.client.android.parkingtrack;

import java.util.Date;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetupAccount extends Activity implements OnClickListener{

	EditText nameEdit;
	EditText emailEdit;
	EditText passEditText;
	//EditText passAgainEditText;
	
	Button registerButton;
	Button closeButton;
	
	public static JSONObject payload;
	public static String stamp;
	public static String content;
	
	public static Handler parkingchumSetupHandler;
	Thread parkingchumSetupUpdater;
	
	
	@SuppressLint("HandlerLeak")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		
		setContentView(R.layout.activity_setup_screen);
		
		nameEdit = (EditText) this.findViewById(R.id.account_name);
		emailEdit = (EditText) this.findViewById(R.id.account_email);
		passEditText = (EditText) this.findViewById(R.id.account_password);
		//passAgainEditText = (EditText) this.findViewById(R.id.account_password_again);
		
		registerButton = (Button) this.findViewById(R.id.account_register);
		closeButton = (Button) this.findViewById(R.id.account_close);
		
		registerButton.setOnClickListener(this);
		closeButton.setOnClickListener(this);
		
		//passAgainEditText.setEnabled(false);
		//passAgainEditText.setClickable(false);
		
		setProgressBarIndeterminateVisibility(true);
		
		SetupAccount.parkingchumSetupHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					if(SplashScreen.token.equals("0000000000000000000000000")){
	        			Toast.makeText(SetupAccount.this, SetupAccount.content, Toast.LENGTH_LONG).show();
	        			registerButton.setEnabled(true);
						registerButton.setClickable(true);
	        		}else{
	        			SplashScreen.LOGIN = true;
	        			Toast.makeText(SetupAccount.this, SetupAccount.content, Toast.LENGTH_LONG).show();
	        			Intent homeActivity = new Intent(getApplicationContext(), MooveScreen.class);
	                	startActivityForResult(homeActivity, 100);
	        		}
					break;
				case 1:
					setProgressBarIndeterminateVisibility(true);
					break;
				case 2:
					setProgressBarIndeterminateVisibility(false);
					break;
				case 3:
					login();
					break;
				case 4:
					registerButton.setEnabled(true);
					registerButton.setClickable(true);
					Toast.makeText(SetupAccount.this, SetupAccount.content, Toast.LENGTH_LONG).show();
					break;
				default:
					break;
				}
			}
        };
        
        if(SplashScreen.SETUP){
        	emailEdit.setText(SplashScreen.email);
        	nameEdit.setText(SplashScreen.name);
        	passEditText.setText(SplashScreen.password);
        	//passAgainEditText.setText(SplashScreen.password);
        }
        
        setProgressBarIndeterminateVisibility(true);
	}
	
	public void login() {
		Log.i("PARKINGCHUM_TRACKER", "LOGIN...");
		this.parkingchumSetupUpdater = new Thread() {
			public void run() {
				try {
					sendMessage(1);
					sleep(300);
					JSONObject loginRequest = new JSONObject();
					loginRequest.put("stamp", SplashScreen.dateFormat.format(new Date()));
					JSONObject payloadRequest = new JSONObject();
					payloadRequest.put("email", SplashScreen.email);
					payloadRequest.put("password", SplashScreen.password);
					loginRequest.put("payload", payloadRequest);
					
					Log.i("PARKINGCHUM_TRACKER", "Request Content: "+loginRequest.toString());
					
					AbstractRequest requesterLogin = new AbstractRequest(
							RequestType.ACCOUNT_LOGIN, loginRequest);
					if (requesterLogin.send() == true) {
						String stamp = requesterLogin.getStamp();
						JSONObject payload = requesterLogin.getpayload();
						
						Log.i("PARKINGCHUM_TRACKER", "Response Time: "+stamp);
						Log.i("PARKINGCHUM_TRACKER", "Response Content: "+payload.toString());
						SplashScreen.token = payload.getString("details");
						SetupAccount.content = payload.getString("details");
						sendMessage(2);
						sendMessage(0);
					} else{
						String stamp = requesterLogin.getStamp();
						JSONObject payload = requesterLogin.getpayload();
						
						Log.i("PARKINGCHUM_TRACKER", "Response Time: "+stamp);
						Log.i("PARKINGCHUM_TRACKER", "Response Content: "+payload.toString());
						SplashScreen.token = "0000000000000000000000000";
						SetupAccount.content = payload.getString("details");
						sendMessage(2);
						sendMessage(4);
					}

				} catch (Exception e) {
					Log.i("PARKINGCHUM_TRACKER", Log.getStackTraceString(e));
					sendMessage(2);
					sendMessage(4);
				}
			}

			private void sendMessage(int what) {
				Message msg = Message.obtain();
				msg.what = what;
				SetupAccount.parkingchumSetupHandler.sendMessage(msg);
			}
		};
		if (this.parkingchumSetupUpdater.isAlive()) {
			try {
				this.parkingchumSetupUpdater.interrupt();
				this.parkingchumSetupUpdater.join();
				this.parkingchumSetupUpdater.start();
			} catch (InterruptedException e) {
			}
		} else
			this.parkingchumSetupUpdater.start();
	}
	
	public void register() {
		Log.i("PARKINGCHUM_TRACKER", "REGISTER...");
		this.parkingchumSetupUpdater = new Thread() {
			public void run() {
				try {
					sendMessage(1);
					sleep(300);
					SplashScreen.name = "" + nameEdit.getText();
					SplashScreen.email = "" + emailEdit.getText();
					SplashScreen.password = "" + passEditText.getText();
					
					JSONObject registerRequest = new JSONObject();
					
					registerRequest.put("stamp", SplashScreen.dateFormat.format(new Date()));
					JSONObject payloadRequest = new JSONObject();
					payloadRequest.put("name", SplashScreen.name);
					payloadRequest.put("email", SplashScreen.email);
					payloadRequest.put("password", SplashScreen.password);
					registerRequest.put("payload", payloadRequest);
					
					Log.i("PARKINGCHUM_TRACKER", "Request Content: "+registerRequest.toString());
					
					AbstractRequest requesterRegister = new AbstractRequest(
							RequestType.ACCOUNT_REGISTER, registerRequest);
					
					if (requesterRegister.send() == true) {
						String stamp = requesterRegister.getStamp();
						JSONObject payload = requesterRegister.getpayload();
						
						Log.i("PARKINGCHUM_TRACKER", "Response Time: "+stamp);
						Log.i("PARKINGCHUM_TRACKER", "Response Content: "+payload.toString());
						SetupAccount.content = payload.getString("details");
						sendMessage(2);
						sendMessage(3);
					} else{
						String stamp = requesterRegister.getStamp();
						JSONObject payload = requesterRegister.getpayload();
						
						Log.i("PARKINGCHUM_TRACKER", "Response Time: "+stamp);
						Log.i("PARKINGCHUM_TRACKER", "Response Content: "+payload.toString());
						String code = requesterRegister.getCode();
						Log.i("PARKINGCHUM_TRACKER", "REGISTER FAILED...");
						if(code.equals("200")){
							SetupAccount.content = payload.getString("details");
							sendMessage(2);
							sendMessage(3);
						}else{
							SetupAccount.content = payload.getString("details");
							sendMessage(2);
							sendMessage(4);
						}
					}
					// TransportClient.load = false;
				} catch (Exception e) {
					Log.i("PARKINGCHUM_TRACKER", Log.getStackTraceString(e));
					sendMessage(2);
					sendMessage(4);
				}
			}

			private void sendMessage(int what) {
				Message msg = Message.obtain();
				msg.what = what;
				SetupAccount.parkingchumSetupHandler.sendMessage(msg);
			}
		};
		if (this.parkingchumSetupUpdater.isAlive()) {
			try {
				this.parkingchumSetupUpdater.interrupt();
				this.parkingchumSetupUpdater.join();
				this.parkingchumSetupUpdater.start();
			} catch (InterruptedException e) {
			}
		} else
			this.parkingchumSetupUpdater.start();
	}
	
	public void stop() {
		this.finish();
	}

	@Override
	public void onClick(View v) {
		if(v == registerButton){
			registerButton.setEnabled(false);
			registerButton.setClickable(false);
			register();
			//startActivityForResult(new Intent(this, HomeScreen.class), 100);
		}else if(v == closeButton){
			this.finish();
		}else{
			
		}
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	finish();
    }
}
