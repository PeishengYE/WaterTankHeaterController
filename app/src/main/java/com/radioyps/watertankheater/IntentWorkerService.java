package com.radioyps.watertankheater;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.net.URL;

/**
 * Created by yep on 18/12/17.
 */

public class IntentWorkerService extends IntentService {

    public static final String LOG_TAG = "IntentWorkerService";

    // Defines and instantiates an object for handling status updates.
    private BroadcastNotifier mBroadcaster = new BroadcastNotifier(this);

    /**
     * An IntentService must always have a constructor that calls the super constructor. The
     * string supplied to the super constructor is used to give a name to the IntentService's
     * background thread.
     */
    public IntentWorkerService() {

        super("IntentWorkerService");
        Log.i(LOG_TAG, "IntentWorkerService()>> ");
    }

    /**
     * In an IntentService, onHandleIntent is run on a background thread.  As it
     * runs, it broadcasts its current status using the LocalBroadcastManager.
     * @param workIntent The Intent that starts the IntentService. This Intent contains the
     * URL of the web site from which the RSS parser gets data.
     */
    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets a URL to read from the incoming Intent's "data" value
        String localUrlString = workIntent.getDataString();

        Log.i(LOG_TAG, "IntentWorkerService()>> getting intent with:  "+ localUrlString);

    }

}
