package com.parkingchum.frontend.client.android.parkingtrack;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class MyLocationListener  implements LocationListener {

	public void onLocationChanged(Location location) {
		String message = String.format(
				"New Location \n Longitude: %1$s \n Latitude: %2$s",
				location.getLongitude(), location.getLatitude());
		Log.i("PARKINGCHUM_TRACKER", message);
	}

	public void onStatusChanged(String s, int i, Bundle b) {
		Log.i("PARKINGCHUM_TRACKER", "Provider status changed");
	}

	public void onProviderDisabled(String s) {
		Log.i("PARKINGCHUM_TRACKER",
				"Provider disabled by the user. GPS turned off");
	}

	public void onProviderEnabled(String s) {
		Log.i("PARKINGCHUM_TRACKER",
				"Provider enabled by the user. GPS turned on");
	}

}
