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

public class MainActivity extends AppCompatActivity {

    StateReceiver mStateReceiver;

    private Button mPowerButton;
    private TextView mRelayTemperatureView;
    private TextView mWaterTemperatureView;
    private ProgressBar mProgressBar;

    private int mPowerButtonStatus = POWER_BUTTON_STATUS_UNKNOWN;

    public static final String LOG_TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mPowerButton = (Button) findViewById(R.id.power_button);
        mRelayTemperatureView = (TextView)findViewById(R.id.relay_temperature);
        mWaterTemperatureView = (TextView)findViewById(R.id.water_temperature);

        mPowerButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){

                if(mPowerButtonStatus == POWER_BUTTON_STATUS_ON){
                    setSwitchOff();
                }else if(mPowerButtonStatus == POWER_BUTTON_STATUS_OFF){
                    setSwitchOn();
                }else{
                    Log.i(LOG_TAG, "onClick()>> power button is unknown state, do nothing " );
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


        querySwitchStatus();
        queryTemperature();
    }


    private void updateResponseForSwitchStatus() {
        Log.i(LOG_TAG, "updateResponseForSwitchStatus()>> response: " + response);
            /* set SwitchStatusTextView*/
        if(response.equalsIgnoreCase(SEVER_REPLY_SWITCH_ON)){

            switchStatus.setText(getString(R.string.current_switch_status)
                    + getString(R.string.power_on));
            power_button.setText(getString(R.string.button_power_off));
            button_status = BUTTON_STATUS_OFF;

        }else if(response.equalsIgnoreCase(SEVER_REPLY_SWITCH_OFF)){

            switchStatus.setText(getString(R.string.current_switch_status)
                    + getString(R.string.power_off));
            power_button.setText(getString(R.string.button_power_on));
            button_status = BUTTON_STATUS_ON;
        }else{
            cmdStatus.setText("Error on cmd");
            // setProgressBarVisibility(false);
            mProgressBar.setVisibility(View.GONE);
            switchStatus.setText(getString(R.string.unknow_state));
            power_button.setText(getString(R.string.disable_button_state));
            button_status = BUTTON_STATUS_UNKNOWN;
        }
            /* set Button status*/
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
            int value;

            value =  intent.getIntExtra(Constants.EXTENDED_NETWORK_ERROR, HAVE_NETWORK_ERROR);
            if(value == HAVE_NETWORK_ERROR){
                 mPowerButtonStatus = POWER_BUTTON_STATUS_UNKNOWN;
            }else{

            }

        }
    }

}
