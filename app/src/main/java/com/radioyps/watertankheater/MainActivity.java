package com.radioyps.watertankheater;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import static com.radioyps.watertankheater.Constants.CmdGetSwtichStatus;
import static com.radioyps.watertankheater.Constants.CmdGetTemperature;
import static com.radioyps.watertankheater.Constants.CmdSetSwitchOFF;
import static com.radioyps.watertankheater.Constants.CmdSetSwitchON;

public class MainActivity extends AppCompatActivity {

    StateReceiver mStateReceiver;
    private Intent mServiceIntent ;

    public static final String LOG_TAG = "MainActivity";
    public static final String TEST = "MainActivity_test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

          /*
         * Creates an intent filter for DownloadStateReceiver that intercepts broadcast Intents
         */

        // The filter's action is BROADCAST_ACTION
        IntentFilter statusIntentFilter = new IntentFilter(
                Constants.BROADCAST_ACTION);

        // Sets the filter's category to DEFAULT
        statusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);

        // Instantiates a new DownloadStateReceiver
        mStateReceiver = new StateReceiver();

        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mStateReceiver,
                statusIntentFilter);

        mServiceIntent =
                new Intent(this, IntentWorkerService.class)
                           .setData(Uri.parse(TEST));
        startService(mServiceIntent);

        setContentView(R.layout.activity_main);

        startService(new Intent(this, UsbDebuggingMonitorService.class));
        querySwitchStatus();
        queryTemperature();
    }


    public void onDestroy() {

        // If the DownloadStateReceiver still exists, unregister it and set it to null
        if (mStateReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mStateReceiver);
            mStateReceiver = null;
        }



        // Must always call the super method at the end.
        super.onDestroy();
    }



    private void setSwitchOff(){
        Intent intentCmd =
                new Intent(this, IntentWorkerService.class)
                        .setData(Uri.parse(CmdSetSwitchOFF));
        startService(intentCmd);
    }

    private void setSwitchOn(){
        Intent intentCmd =
                new Intent(this, IntentWorkerService.class)
                        .setData(Uri.parse(CmdSetSwitchON));
        startService(intentCmd);
    }

    private void querySwitchStatus(){
        Intent intentCmd =
                new Intent(this, IntentWorkerService.class)
                        .setData(Uri.parse(CmdGetSwtichStatus));
        startService(intentCmd);
    }

    private void queryTemperature(){
        Intent intentCmd =
                new Intent(this, IntentWorkerService.class)
                        .setData(Uri.parse(CmdGetTemperature));
        startService(intentCmd);
    }

    /**
     * This class uses the BroadcastReceiver framework to detect and handle status messages from
     * the service that downloads URLs.
     */
    private class StateReceiver extends BroadcastReceiver {

        private StateReceiver() {

            // prevents instantiation by other packages.
        }
        /**
         *
         * This method is called by the system when a broadcast Intent is matched by this class'
         * intent filters
         *
         * @param context An Android context
         * @param intent The incoming broadcast Intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {

            /*
             * Gets the status from the Intent's extended data, and chooses the appropriate action
             */
            switch (intent.getIntExtra(Constants.EXTENDED_DATA_STATUS,
                    Constants.STATE_ACTION_COMPLETE)) {

                // Logs "started" state
                case Constants.STATE_ACTION_STARTED:
                    if (Constants.LOGD) {

                        Log.d(LOG_TAG, "State: STARTED");
                    }
                    break;
                // Logs "connecting to network" state
                case Constants.STATE_ACTION_CONNECTING:
                    if (Constants.LOGD) {

                        Log.d(LOG_TAG, "State: CONNECTING");
                    }
                    break;
                // Logs "parsing the RSS feed" state
                case Constants.STATE_ACTION_PARSING:
                    if (Constants.LOGD) {

                        Log.d(LOG_TAG, "State: PARSING");
                    }
                    break;
                // Logs "Writing the parsed data to the content provider" state
                case Constants.STATE_ACTION_WRITING:
                    if (Constants.LOGD) {

                        Log.d(LOG_TAG, "State: WRITING");
                    }
                    break;
                // Starts displaying data when the RSS download is complete
                case Constants.STATE_ACTION_COMPLETE:
                    // Logs the status
                    if (Constants.LOGD) {

                        Log.d(LOG_TAG, "State: COMPLETE");
                    }


                    break;
                default:
                    break;
            }
        }
    }

}
