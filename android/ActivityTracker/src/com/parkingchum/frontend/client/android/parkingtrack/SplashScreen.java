package com.parkingchum.frontend.client.android.parkingtrack;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.Toast;

public class SplashScreen extends Activity {

	public static boolean SETUP = false;
	public static boolean LOGIN = false;
	public static final String PREFS_NAME = "ParkingTrackerPrefsFile";
	public static SharedPreferences preferences;
	
	public static String token;
	public static String content;
	public static String email;
	public static String password;
	public static String name;
	public static String profile;
	TelephonyManager telephonyManager;
	public static Handler parkingchumClientHandler;
	
	public static double latitudeE6 = 0;
	public static double longitudeE6 = 0;
	public static double altitudeE6 = 0;
    
    public static float Zoom = 10;
    
    public static boolean STARTED = false;
    
    public static long step = 0;
    public static String activity = "WALKING";
    public static long activity_number = 0;
    
    public static float ax;
    public static float ay;
    public static float az;
    public static float speed;
    
    public static float pitch;
    public static float roll;
    public static float azimuth;
    
    public static boolean busy = false;
	
	@SuppressLint("SimpleDateFormat")
	public static DateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmssSSS");
	
	Thread parkingchumClientUpdater;
	
	public static double distance(double lat1, double lon1, double lat2, double lon2,
			String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit.equals("K")) {
			dist = dist * 1.609344;
		} else if (unit.equals("N")) {
			dist = dist * 0.8684;
		}
		return (dist);
	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}
    
	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		
		setContentView(R.layout.activity_splash_screen);
		
		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		
		preferences = this.getSharedPreferences(PREFS_NAME, 0);
        
        SplashScreen.loadData();
        
        SplashScreen.parkingchumClientHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					if(token.equals("0000000000000000000000000")){
						Toast.makeText(SplashScreen.this, SplashScreen.content, Toast.LENGTH_LONG).show();
	        			Toast.makeText(SplashScreen.this, SplashScreen.content, Toast.LENGTH_LONG).show();
	        			Intent setupActivity = new Intent(getApplicationContext(), SetupAccount.class);
	        			startActivityForResult(setupActivity, 200);
	        		}else{
	        			LOGIN = true;
	        			Toast.makeText(SplashScreen.this, SplashScreen.content, Toast.LENGTH_LONG).show();
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
				default:
					break;
				}
			}
        };
        
        if(SplashScreen.email.equals("")){
        	SETUP = false;
        	//Go to Setup
        	Intent setupActivity = new Intent(getApplicationContext(), SetupAccount.class);
        	startActivityForResult(setupActivity, 200);
        }else{
        	SETUP = true;
        	if(SplashScreen.token.equals("0000000000000000000000000")){
        		LOGIN = false;
        		login();
            }else{
            	LOGIN = true;
            	//Got to Home
            	Intent homeActivity = new Intent(getApplicationContext(), MooveScreen.class);
            	startActivityForResult(homeActivity, 100);
            }
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screen, menu);
		return true;
	}
	
	public static void loadData(){
	    token = SplashScreen.preferences.getString("token", "0000000000000000000000000");
	    email = SplashScreen.preferences.getString("email", "");
	    password = SplashScreen.preferences.getString("password", "");
	    name = SplashScreen.preferences.getString("name", "");
	    profile = SplashScreen.preferences.getString("profile", "");
	    
	    longitudeE6 = SplashScreen.preferences.getFloat("longitudeE6", 0);
	    latitudeE6 = SplashScreen.preferences.getFloat("latitudeE6", 0);
	    altitudeE6 = SplashScreen.preferences.getFloat("altitudeE6", 0);

	    Zoom = SplashScreen.preferences.getFloat("zoom", 10);
	    STARTED = SplashScreen.preferences.getBoolean("started", false);
	    step = SplashScreen.preferences.getLong("step", 0);
	    activity = SplashScreen.preferences.getString("activity", "WALKING");
	    activity_number = SplashScreen.preferences.getLong("activity_number", 1);
	    
	    ax = SplashScreen.preferences.getFloat("ax", 0);
	    ay = SplashScreen.preferences.getFloat("ay", 0);
	    az = SplashScreen.preferences.getFloat("az", 0);
	    
	    pitch = SplashScreen.preferences.getFloat("pitch", 0);
	    roll = SplashScreen.preferences.getFloat("roll", 0);
	    azimuth = SplashScreen.preferences.getFloat("azimuth", 0);
	    
	    speed = SplashScreen.preferences.getFloat("speed", 0);
	}
	
	public static void pushData(){
		SharedPreferences.Editor editor = SplashScreen.preferences.edit();
		editor.putString("token", token);
		editor.putString("email", email);
		editor.putString("password", password);
		editor.putString("name", name);
		editor.putString("profile", profile);
		
		editor.putFloat("longitudeE6", (float) longitudeE6);
		editor.putFloat("latitudeE6", (float) latitudeE6);
		editor.putFloat("altitudeE6", (float) altitudeE6);
		
		editor.putFloat("zoom", SplashScreen.Zoom);
		editor.putBoolean("started", SplashScreen.STARTED);
		editor.putLong("step", SplashScreen.step);
		editor.putString("activity", SplashScreen.activity);
		editor.putLong("activity_number", SplashScreen.activity_number);
		
		editor.putFloat("ax", SplashScreen.ax);
		editor.putFloat("ay", SplashScreen.ay);
		editor.putFloat("az", SplashScreen.az);
		
		editor.putFloat("pitch", SplashScreen.pitch);
		editor.putFloat("roll", SplashScreen.roll);
		editor.putFloat("azimuth", SplashScreen.azimuth);
		
		editor.putFloat("speed", SplashScreen.speed);
		
		editor.commit();
	}
	
	public void login() {
		Log.i("PARKINGCHUM_TRACKER", "LOGIN...");
		this.parkingchumClientUpdater = new Thread() {
			public void run() {
				try {
					sendMessage(1);
					sleep(300);
					JSONObject loginRequest = new JSONObject();
					loginRequest.put("stamp", dateFormat.format(new Date()));
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
						Log.i("PARKINGCHUM_TRACKER", "Request Content: "+loginRequest.toString());
						SplashScreen.token = payload.getString("details");
						SplashScreen.content = payload.getString("details");
					} else{
						String stamp = requesterLogin.getStamp();
						JSONObject payload = requesterLogin.getpayload();
						
						Log.i("PARKINGCHUM_TRACKER", "Response Time: "+stamp);
						Log.i("PARKINGCHUM_TRACKER", "Request Content: "+loginRequest.toString());
						SplashScreen.token = "0000000000000000000000000";
						SplashScreen.content = payload.getString("details");
					}

				} catch (Exception e) {
					Log.i("PARKINGCHUM_TRACKER", Log.getStackTraceString(e));
					sendMessage(2);
				}
				sendMessage(2);
				sendMessage(0);
			}

			private void sendMessage(int what) {
				Message msg = Message.obtain();
				msg.what = what;
				SplashScreen.parkingchumClientHandler.sendMessage(msg);
			}
		};
		if (this.parkingchumClientUpdater.isAlive()) {
			try {
				this.parkingchumClientUpdater.interrupt();
				this.parkingchumClientUpdater.join();
				this.parkingchumClientUpdater.start();
			} catch (InterruptedException e) {
			}
		} else
			this.parkingchumClientUpdater.start();
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	pushData();
    	finish();
    }
}