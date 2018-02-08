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
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import static com.radioyps.watertankheater.Constants.CmdGetSwtichStatus;
import static com.radioyps.watertankheater.Constants.CmdGetTemperature;
import static com.radioyps.watertankheater.Constants.CmdSetSwitchOFF;
import static com.radioyps.watertankheater.Constants.CmdSetSwitchON;
import static com.radioyps.watertankheater.Constants.HAVE_NETWORK_ERROR;
import static com.radioyps.watertankheater.Constants.POWER_BUTTON_STATUS_OFF;
import static com.radioyps.watertankheater.Constants.POWER_BUTTON_STATUS_ON;
import static com.radioyps.watertankheater.Constants.POWER_BUTTON_STATUS_UNKNOWN;
import static com.radioyps.watertankheater.Constants.PROGRESS_BAR_OFF;
import static com.radioyps.watertankheater.Constants.PROGRESS_BAR_ON;
import static com.radioyps.watertankheater.Constants.STATE_SWITCH_OFF;
import static com.radioyps.watertankheater.Constants.STATE_SWITCH_ON;
import static com.radioyps.watertankheater.Constants.YEP_UNKNOWN_ERROR;

public class MainActivity extends AppCompatActivity {

    StateReceiver mStateReceiver;

    private Button mPowerButton;
    private TextView mRelayTemperatureView;
    private TextView mWaterTemperatureViewBig;
    private TextView mWaterTemperatureViewSmall;
    private TextView mSwitchStatus;
    private int mCount = 0;
    private ProgressBar mProgressBar;

    private int mPowerButtonStatus = POWER_BUTTON_STATUS_UNKNOWN;

    public static final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        mPowerButton = (Button) findViewById(R.id.power_button);
        mRelayTemperatureView = (TextView)findViewById(R.id.relay_temperature);
        mWaterTemperatureViewSmall = (TextView)findViewById(R.id.water_temperature);
        mWaterTemperatureViewBig = (TextView)findViewById(R.id.water_temperature_big);
        mSwitchStatus = (TextView)findViewById(R.id.switch_state);

        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        mPowerButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                mCount ++;
                if(mPowerButtonStatus == POWER_BUTTON_STATUS_ON){
                    setSwitchOff();
                }else if(mPowerButtonStatus == POWER_BUTTON_STATUS_OFF){
                    setSwitchOn();
                }else{
                    Log.i(LOG_TAG, "onClick()>> power button is unknown state, do nothing " + "ZIHAN =" + mCount);
                }

            }
        });
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




        startService(new Intent(this, UsbDebuggingMonitorService.class));
//        querySwitchStatus();
//        queryTemperature();
        Main.dimDisplay(true);
    }


    /*
    private void updateResponseForSwitchStatus() {
        Log.i(LOG_TAG, "updateResponseForSwitchStatus()>> response: " + response);

        if(response.equalsIgnoreCase(SEVER_REPLY_SWITCH_ON)){

            mSwitchStatus.setText(getString(R.string.current_switch_status)
                    + getString(R.string.power_on));
            mPowerButton.setText(getString(R.string.button_power_off));
            mPowerButtonStatus = BUTTON_STATUS_OFF;

        }else if(response.equalsIgnoreCase(SEVER_REPLY_SWITCH_OFF)){

            mSwitchStatus.setText(getString(R.string.current_switch_status)
                    + getString(R.string.power_off));
            mPowerButton.setText(getString(R.string.button_power_on));
            mPowerButtonStatus = BUTTON_STATUS_ON;
        }else{

            mProgressBar.setVisibility(View.GONE);
            mSwitchStatus.setText(getString(R.string.unknow_state));
            mPowerButton.setText(getString(R.string.disable_button_state));
            mPowerButtonStatus = BUTTON_STATUS_UNKNOWN;
        }

    }
    */

    @Override
    protected void onStop() {
        // If the DownloadStateReceiver still exists, unregister it and set it to null
        if (mStateReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mStateReceiver);
            mStateReceiver = null;
        }
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mStateReceiver == null){

            mStateReceiver = new StateReceiver();
            // The filter's action is BROADCAST_ACTION
            IntentFilter statusIntentFilter = new IntentFilter(
                    Constants.BROADCAST_ACTION);

            // Sets the filter's category to DEFAULT
            statusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);
            // Registers the DownloadStateReceiver and its intent filters
            LocalBroadcastManager.getInstance(this).registerReceiver(
                    mStateReceiver,
                    statusIntentFilter);

        }



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



    public void setSwitchOff(){
        Intent intentCmd =
                new Intent(this, IntentWorkerService.class)
                        .setData(Uri.parse(CmdSetSwitchOFF));
        startService(intentCmd);
    }

    public void setSwitchOn(){
        Intent intentCmd =
                new Intent(this, IntentWorkerService.class)
                        .setData(Uri.parse(CmdSetSwitchON));
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
            int value;

            /* PROGRESS BAR */
            value =  intent.getIntExtra(Constants.EXTENDED_QUERY_PROGRESS_BAR_STATUS, YEP_UNKNOWN_ERROR);
            if(value == PROGRESS_BAR_OFF){
                Log.i(LOG_TAG, "onReceive()>> progress bar is off " );
                mProgressBar.setVisibility(View.GONE);
            }else if(value == PROGRESS_BAR_ON){
                Log.i(LOG_TAG, "onReceive()>> progress bar is on " );
                mProgressBar.setVisibility(View.VISIBLE);
            }

            /* WATER_TEMPERATURE */
            value =  intent.getIntExtra(Constants.EXTENDED_WATER_TEMPERATURE, HAVE_NETWORK_ERROR);
            if(value != HAVE_NETWORK_ERROR){
                Log.i(LOG_TAG, "onReceive()>> temperatur is " + value);
                mWaterTemperatureViewBig.setText(String.valueOf(value/1000));
                mWaterTemperatureViewSmall.setText(getString(R.string.sensor_on_water)+" "+String.valueOf(value/1000.0));
            }


            /* SWITCH_STATUS */
            value =  intent.getIntExtra(Constants.EXTENDED_SWITCH_STATUS, HAVE_NETWORK_ERROR);
            if(value != HAVE_NETWORK_ERROR){
             if(value == STATE_SWITCH_ON){
                 Log.i(LOG_TAG, "onReceive()>> Switch is on " );
                 mSwitchStatus.setText(getString(R.string.current_switch_status) + getString(R.string.power_on));
                 mPowerButtonStatus = POWER_BUTTON_STATUS_ON;
                 mPowerButton.setText(getString(R.string.button_power_available_off));


             }else if(value == STATE_SWITCH_OFF){
                 Log.i(LOG_TAG, "onReceive()>> Switch is OFF " );
                 mSwitchStatus.setText(getString(R.string.current_switch_status) +getString(R.string.power_off));
                 mPowerButtonStatus = POWER_BUTTON_STATUS_OFF;
                 mPowerButton.setText(getString(R.string.button_power_available_on));

             }
            }


            /**/


            /* RELAY_TEMPERATURE */
            value =  intent.getIntExtra(Constants.EXTENDED_RELAY_TEMPERATURE, HAVE_NETWORK_ERROR);
            if(value != HAVE_NETWORK_ERROR){

            }

        }
    }

}
