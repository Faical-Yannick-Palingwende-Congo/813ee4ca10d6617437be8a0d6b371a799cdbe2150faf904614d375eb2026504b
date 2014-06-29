package com.parkingchum.frontend.client.android.parkingtrack;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MooveScreen extends Activity implements OnClickListener, OnMyLocationChangeListener, OnCameraChangeListener, OnItemSelectedListener, SensorEventListener{

	Button action;
	Button close;
	Spinner type;
	
	GoogleMap mapView;
	
	public static boolean hide = false;
	
	public static MapUpdater forwarder = null;
	
	public static Handler parkingchumMooveHandler;
	
	Thread parkingchumRecordUpdater;
	Thread parkingchumStartUpdater;
	Thread parkingchumEndUpdater;
	
	public static JSONArray around = null;
	public static String content;
	
	public static Location location = null;
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; 
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000;
	protected LocationManager locationManager;
	public static MyLocationListener listener;
	
	TelephonyManager telephonyManager;
    
    List<String> types;
    
    SensorManager mSensorManager;
    Sensor mAccelerometer;
    Sensor mRotation;
    
    public static Requesthead ready = null;
    
    
	
	@SuppressLint("HandlerLeak")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		
		setContentView(R.layout.activity_moove_screen);
		
		action = (Button) this.findViewById(R.id.action);
		close = (Button) this.findViewById(R.id.close_moove);
		type = (Spinner) this.findViewById(R.id.type);
		
		action.setOnClickListener(this);
		close.setOnClickListener(this);
		
		setProgressBarIndeterminateVisibility(true);
		
		types = new ArrayList<String>();
		types.add("WALKING");
		types.add("JOGGING");
		types.add("RUNNING");
		types.add("SWIMMING");
		types.add("BICYCLING");
		types.add("MOTOCYCLING");
		types.add("BUS");
		types.add("DRIVING");
		types.add("BOATING");
		types.add("FLYING");
		types.add("SITTING");
		types.add("SLEEPING");
		types.add("STANDING");
		types.add("LAYING");
		types.add("FALLING");
		types.add("DEING");
		types.add("METRO");
		types.add("SKYING");
		types.add("SURFING");
		types.add("JUMPING");
		types.add("LANDING");
		types.add("TAKING OFF");
		types.add("TRAMWAY");
		types.add("TRAIN");
		types.add("CRASHING");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, types);
		type.setAdapter(adapter);
		
		type.setOnItemSelectedListener(this);
		
		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
	    mRotation = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED);
	    if(mAccelerometer == null || mRotation == null){
	    	Toast.makeText(this, "Sorry!!!! You miss important sensors for this collections:"+(mAccelerometer == null?" ACCELEROMETER":"")+(mRotation == null?" MAGNETOMETER":""), Toast.LENGTH_LONG).show();
	    }
		
		initilizeMap();
		
		final MapWrapperLayout mapWrapperLayout = (MapWrapperLayout)findViewById(R.id.map_relative_layout);
		
		mapWrapperLayout.init(mapView, getPixelsFromDp(this, 39 + 20)); 
		
		MooveScreen.parkingchumMooveHandler = new Handler() {
			@SuppressLint("HandlerLeak")
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					SplashScreen.STARTED = true;
					action.setText("Stop "+SplashScreen.activity+"["+SplashScreen.activity_number+"]");
					Toast.makeText(MooveScreen.this, "Activity Started", Toast.LENGTH_LONG).show();
					MooveScreen.forwarder.startUpdates();
					action.setEnabled(true);
					action.setClickable(true);
					MooveScreen.ready = new Requesthead(SplashScreen.dateFormat.format(new Date()), ""+SplashScreen.activity_number, ""+SplashScreen.step, ""+SplashScreen.ax, ""+SplashScreen.ay, ""+SplashScreen.az,""+SplashScreen.pitch, ""+SplashScreen.roll, ""+SplashScreen.azimuth, ""+SplashScreen.speed, ""+SplashScreen.longitudeE6, ""+SplashScreen.latitudeE6, ""+SplashScreen.altitudeE6);
					mSensorManager.registerListener(MooveScreen.this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
					mSensorManager.registerListener(MooveScreen.this, mRotation, SensorManager.SENSOR_DELAY_NORMAL);
					break;
				case 1:
					setProgressBarIndeterminateVisibility(true);
					break;
				case 2:
					setProgressBarIndeterminateVisibility(false);
					break;
				case 3:
					Toast.makeText(MooveScreen.this, MooveScreen.content, Toast.LENGTH_LONG).show();
					MooveScreen.forwarder.stopUpdates();
					action.setText("Start");
					action.setEnabled(true);
					action.setClickable(true);
					MooveScreen.ready = null;
					SplashScreen.STARTED = false;
					SplashScreen.step = 1;
					SplashScreen.pushData();
					mSensorManager.unregisterListener(MooveScreen.this);
					break;
				case 4:
					record();
					break;
				case 5:
					SplashScreen.step++;
					MooveScreen.ready = new Requesthead(SplashScreen.dateFormat.format(new Date()), ""+SplashScreen.activity_number, ""+SplashScreen.step, ""+SplashScreen.ax, ""+SplashScreen.ay, ""+SplashScreen.az,""+SplashScreen.pitch, ""+SplashScreen.roll, ""+SplashScreen.azimuth, ""+SplashScreen.speed, ""+SplashScreen.longitudeE6, ""+SplashScreen.latitudeE6, ""+SplashScreen.altitudeE6);
					break;
				case 6:
					end();
					break;
				case 7:
					setProgressBarIndeterminateVisibility(false);
					SplashScreen.busy = false;
					break;
				default:
					break;
				}
			}
        };
        
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		MooveScreen.listener = new MyLocationListener();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MINIMUM_TIME_BETWEEN_UPDATES,
				MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, MooveScreen.listener);
        
		MooveScreen.forwarder = new MapUpdater(new Runnable() {

			@Override
			public void run() {
				if(SplashScreen.STARTED){
					if(!SplashScreen.busy){
						SplashScreen.busy = true;
						sendMessage(4);
					}else{
						if(MooveScreen.ready != null) MooveScreen.ready = null;
					}
				}
			}

			private void sendMessage(int what) {
				Message msg = Message.obtain();
				msg.what = what;
				MooveScreen.parkingchumMooveHandler.sendMessage(msg);
			}
		}, 2000);
		
		setProgressBarIndeterminateVisibility(false);
		
		if(SplashScreen.STARTED){
			action.setText("Stop "+SplashScreen.activity+"["+SplashScreen.activity_number+"]");
			MooveScreen.forwarder.startUpdates();
		}
		
	}
	
	public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }
	
	private void initilizeMap() {
        if (mapView == null) {
        	mapView = ((MapFragment) getFragmentManager().findFragmentById(R.id.moove_map)).getMap();
 
            // check if map is created successfully or not
            if (mapView == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }else{
            	mapView.setMyLocationEnabled(true);
            	mapView.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            	mapView.setMyLocationEnabled(true);
            	mapView.setOnMyLocationChangeListener(this);
            	mapView.setOnCameraChangeListener(this);
            }
        }
    }
	
	public void record() {
		Log.i("PARKINGCHUM_TRACKER", "RECORD...");
		this.parkingchumRecordUpdater = new Thread() {
			public void run() {
				try {
					sendMessage(1);
					sleep(300);
					
					Log.i("PARKINGCHUM_TRACKER", "Request Content: "+MooveScreen.ready.toJson().toString());

					AbstractRequest requesterLogin = new AbstractRequest(
							RequestType.ACTIVITY_RECORD, MooveScreen.ready.toJson());
					if (requesterLogin.send() == true) {
						String stamp = requesterLogin.getStamp();
						JSONObject payload = requesterLogin.getpayload();
						MooveScreen.content = payload.getString("details");
						Log.i("PARKINGCHUM_TRACKER", "Response Time: "+stamp);
						Log.i("PARKINGCHUM_TRACKER", "Response Content: "+payload.toString());
						sendMessage(5);
					} else{
						String stamp = requesterLogin.getStamp();
						JSONObject payload = requesterLogin.getpayload();
						Log.i("PARKINGCHUM_TRACKER", "Response Time: "+stamp);
						Log.i("PARKINGCHUM_TRACKER", "Response Content: "+payload.toString());
						MooveScreen.content = payload.getString("details");
					}
					
				} catch (Exception e) {
					Log.i("PARKINGCHUM_TRACKER", Log.getStackTraceString(e));
				}
				sendMessage(7);
			}

			private void sendMessage(int what) {
				Message msg = Message.obtain();
				msg.what = what;
				MooveScreen.parkingchumMooveHandler.sendMessage(msg);
			}
		};
		if (this.parkingchumRecordUpdater.isAlive()) {
			try {
				this.parkingchumRecordUpdater.interrupt();
				this.parkingchumRecordUpdater.join();
				this.parkingchumRecordUpdater.start();
			} catch (InterruptedException e) {
			}
		} else
			this.parkingchumRecordUpdater.start();
	}
	
	public void start() {
		Log.i("PARKINGCHUM_TRACKER", "START...");
		this.parkingchumStartUpdater = new Thread() {
			public void run() {
				try {
					sendMessage(1);
					sleep(300);
					JSONObject lockRequest = new JSONObject();
					lockRequest.put("stamp", SplashScreen.dateFormat.format(new Date()));
					JSONObject payloadRequest = new JSONObject();
					payloadRequest.put("token", SplashScreen.token);
					payloadRequest.put("type", ""+SplashScreen.activity);
					lockRequest.put("payload", payloadRequest);
					
					Log.i("PARKINGCHUM_TRACKER", "Request Content: "+lockRequest.toString());

					AbstractRequest requesterLogin = new AbstractRequest(
							RequestType.ACTIVITY_START, lockRequest);
					if (requesterLogin.send() == true) {
						String stamp = requesterLogin.getStamp();
						JSONObject payload = requesterLogin.getpayload();
						Log.i("PARKINGCHUM_TRACKER", "Response Time: "+stamp);
						Log.i("PARKINGCHUM_TRACKER", "Response Content: "+payload.toString());
						MooveScreen.content = payload.getString("details");
						SplashScreen.activity_number = Long.parseLong(MooveScreen.content);
						sendMessage(0);
					} else{
						String stamp = requesterLogin.getStamp();
						JSONObject payload = requesterLogin.getpayload();
						Log.i("PARKINGCHUM_TRACKER", "Response Time: "+stamp);
						Log.i("PARKINGCHUM_TRACKER", "Response Content: "+payload.toString());
						MooveScreen.content = payload.getString("details");
					}
				} catch (Exception e) {
					Log.i("PARKINGCHUM_TRACKER", Log.getStackTraceString(e));
				}
				sendMessage(2);
			}

			private void sendMessage(int what) {
				Message msg = Message.obtain();
				msg.what = what;
				MooveScreen.parkingchumMooveHandler.sendMessage(msg);
			}
		};
		if (this.parkingchumStartUpdater.isAlive()) {
			try {
				this.parkingchumStartUpdater.interrupt();
				this.parkingchumStartUpdater.join();
				this.parkingchumStartUpdater.start();
			} catch (InterruptedException e) {
			}
		} else
			this.parkingchumStartUpdater.start();
	}
	
	public void end() {
		Log.i("PARKINGCHUM_TRACKER", "END...");
		this.parkingchumEndUpdater = new Thread() {
			public void run() {
				try {
					sendMessage(1);
					sleep(300);
					JSONObject lockRequest = new JSONObject();
					lockRequest.put("stamp", SplashScreen.dateFormat.format(new Date()));
					JSONObject payloadRequest = new JSONObject();
					payloadRequest.put("token", SplashScreen.token);
					payloadRequest.put("activity", ""+SplashScreen.activity_number);
					lockRequest.put("payload", payloadRequest);
					
					Log.i("PARKINGCHUM_TRACKER", "Request Content: "+lockRequest.toString());

					AbstractRequest requesterLogin = new AbstractRequest(
							RequestType.ACTIVITY_END, lockRequest);
					if (requesterLogin.send() == true) {
						String stamp = requesterLogin.getStamp();
						JSONObject payload = requesterLogin.getpayload();
						Log.i("PARKINGCHUM_TRACKER", "Response Time: "+stamp);
						Log.i("PARKINGCHUM_TRACKER", "Response Content: "+payload.toString());
						MooveScreen.content = payload.getString("details");
						sendMessage(3);
					} else{
						String stamp = requesterLogin.getStamp();
						JSONObject payload = requesterLogin.getpayload();
						Log.i("PARKINGCHUM_TRACKER", "Response Time: "+stamp);
						Log.i("PARKINGCHUM_TRACKER", "Response Content: "+payload.toString());
						MooveScreen.content = payload.getString("details");
					}
				} catch (Exception e) {
					Log.i("PARKINGCHUM_TRACKER", Log.getStackTraceString(e));
				}
				sendMessage(2);
			}

			private void sendMessage(int what) {
				Message msg = Message.obtain();
				msg.what = what;
				MooveScreen.parkingchumMooveHandler.sendMessage(msg);
			}
		};
		if (this.parkingchumEndUpdater.isAlive()) {
			try {
				this.parkingchumEndUpdater.interrupt();
				this.parkingchumEndUpdater.join();
				this.parkingchumEndUpdater.start();
			} catch (InterruptedException e) {
			}
		} else
			this.parkingchumEndUpdater.start();
	}

	@Override
	public void onClick(View v) {
		if(v == action){
			//Sent the locking and make sure it succeed before doing what is done lower
			action.setEnabled(false);
			action.setClickable(false);
			if(SplashScreen.STARTED){
				Toast.makeText(this, "Ending...", Toast.LENGTH_LONG).show();
				end();
			}else{
				Toast.makeText(this, "Starting...", Toast.LENGTH_LONG).show();
				start();
			}
		}else if(v == close){
			SplashScreen.pushData();
			finish();
		}else{
			
		}
		
	}
	
	@Override
	protected void onResume() {
		MooveScreen.listener = new MyLocationListener();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MINIMUM_TIME_BETWEEN_UPDATES,
				MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, MooveScreen.listener);
		super.onResume();
	}

	@Override
	protected void onStop() {
		try {
			if (this.parkingchumRecordUpdater != null) {
				this.parkingchumRecordUpdater.interrupt();
				this.parkingchumRecordUpdater = null;
			}
			if (this.parkingchumStartUpdater != null) {
				this.parkingchumStartUpdater.interrupt();
				this.parkingchumStartUpdater = null;
			}
			if (this.parkingchumEndUpdater != null) {
				this.parkingchumEndUpdater.interrupt();
				this.parkingchumEndUpdater = null;
			}
			locationManager.removeUpdates(MooveScreen.listener);
		} catch (Exception ex) {
			Log.d("PARKINGCHUM_TRACKER", Log.getStackTraceString(ex));
		}
		super.onStop();
	}

	@Override
	public void onMyLocationChange(Location location) {
		SplashScreen.longitudeE6 = location.getLongitude();
		SplashScreen.latitudeE6 = location.getLatitude();
		SplashScreen.altitudeE6 = location.getAltitude();
		
		SplashScreen.speed = location.getSpeed();
		
		
		CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(location.getLatitude(), location.getLongitude())).zoom(SplashScreen.Zoom).build();
		
		mapView.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}

	@Override
	public void onCameraChange(CameraPosition position) {
		SplashScreen.Zoom = position.zoom;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		SplashScreen.activity = types.get(position);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		SplashScreen.activity = "WALKING";
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		Sensor sensor = event.sensor;
		if(sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
			SplashScreen.ax = event.values[0];
			SplashScreen.ay = event.values[1];
			SplashScreen.az = event.values[2];
			Log.i("PARKINGCHUM_TRACKER", "ACCELEROMETER: ["+SplashScreen.ax+","+SplashScreen.ay+","+SplashScreen.az+"]");
		}
		if(sensor.getType() == Sensor.TYPE_GYROSCOPE_UNCALIBRATED){
			SplashScreen.pitch = event.values[3];
			SplashScreen.roll = event.values[4];
			SplashScreen.azimuth = event.values[5];
			Log.i("PARKINGCHUM_TRACKER", "GYROSCOPE: ["+SplashScreen.ax+","+SplashScreen.ay+","+SplashScreen.az+"]");
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
}
