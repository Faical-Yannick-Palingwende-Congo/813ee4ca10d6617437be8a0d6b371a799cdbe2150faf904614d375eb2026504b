package com.parkingchum.frontend.client.android.parkingtrack;

import android.os.Handler;

public class MapUpdater {
    private Handler mHandler = new Handler(); // TODO Don't know if this is created in the UI thread
    private Runnable mStatusChecker;
    private int UPDATE_INTERVAL = 2000;

    /**
     * Creates an UIUpdater object, that can be used to
     * perform UIUpdates on a specified time interval.
     * 
     * @param uiUpdater A runnable containing the update routine.
     */
    public MapUpdater(final Runnable uiUpdater){
        mStatusChecker = new Runnable() {
            @Override
            public void run() {
                // Run the passed runnable
                uiUpdater.run();

                // Re-run it after the update interval
                mHandler.postDelayed(this, UPDATE_INTERVAL);
            }
        };
    }

    /**
     * The same as the default constructor, but specifying the
     * intended update interval.
     * 
     * @param uiUpdater A runnable containing the update routine.
     * @param interval  The interval over which the routine
     *                  should run (milliseconds).
     */
    public MapUpdater(Runnable uiUpdater, int interval){
        this(uiUpdater);
        UPDATE_INTERVAL = interval;
    }

    /**
     * Starts the periodical update routine (mStatusChecker 
     * adds the callback to the handler).
     */
    public void startUpdates(){
        mStatusChecker.run();
    }

    /**
     * Stops the periodical update routine from running,
     * by removing the callback.
     */
    public void stopUpdates(){
        mHandler.removeCallbacks(mStatusChecker);
    }
}
