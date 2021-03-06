package com.radioyps.watertankheater;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by yep on 18/12/17.
 */


    public class BroadcastNotifier {

        private LocalBroadcastManager mBroadcaster;

        /**
         * Creates a BroadcastNotifier containing an instance of LocalBroadcastManager.
         * LocalBroadcastManager is more efficient than BroadcastManager; because it only
         * broadcasts to components within the app, it doesn't have to do parceling and so forth.
         *
         * @param context a Context from which to get the LocalBroadcastManager
         */
        public BroadcastNotifier(Context context) {

            // Gets an instance of the support library local broadcastmanager
            mBroadcaster = LocalBroadcastManager.getInstance(context);

        }

        /**
         *
         * Uses LocalBroadcastManager to send an {@link Intent} containing {@code status}. The
         * {@link Intent} has the action {@code BROADCAST_ACTION} and the category {@code DEFAULT}.
         *
         * @param status {@link Integer} denoting a work request status
         */
        public void broadcastIntentWithSwitchState(int status) {

            Intent localIntent = new Intent();

            // The Intent contains the custom broadcast action for this app
            localIntent.setAction(Constants.BROADCAST_ACTION);

            // Puts the status into the Intent
            localIntent.putExtra(Constants.EXTENDED_SWITCH_STATUS, status);
            localIntent.addCategory(Intent.CATEGORY_DEFAULT);

            // Broadcasts the Intent
            mBroadcaster.sendBroadcast(localIntent);

        }


    public void broadcastIntentWithRelayTemperature(int status) {

        Intent localIntent = new Intent();

        // The Intent contains the custom broadcast action for this app
        localIntent.setAction(Constants.BROADCAST_ACTION);

        // Puts the status into the Intent
        localIntent.putExtra(Constants.EXTENDED_RELAY_TEMPERATURE, status);
        localIntent.addCategory(Intent.CATEGORY_DEFAULT);

        // Broadcasts the Intent
        mBroadcaster.sendBroadcast(localIntent);

    }

    public void broadcastIntentWithWaterTemperature(int status) {

        Intent localIntent = new Intent();

        // The Intent contains the custom broadcast action for this app
        localIntent.setAction(Constants.BROADCAST_ACTION);

        // Puts the status into the Intent
        localIntent.putExtra(Constants.EXTENDED_WATER_TEMPERATURE, status);
        localIntent.addCategory(Intent.CATEGORY_DEFAULT);

        // Broadcasts the Intent
        mBroadcaster.sendBroadcast(localIntent);

    }


    public void broadcastIntentWithProgressBarStatus( int state) {

        Intent localIntent = new Intent();

        // The Intent contains the custom broadcast action for this app
        localIntent.setAction(Constants.BROADCAST_ACTION);

        // Puts the status into the Intent
        localIntent.putExtra(Constants.EXTENDED_QUERY_PROGRESS_BAR_STATUS, state);
        localIntent.addCategory(Intent.CATEGORY_DEFAULT);

        // Broadcasts the Intent
        mBroadcaster.sendBroadcast(localIntent);

    }

    public void broadcastIntentWithError( int state) {

        Intent localIntent = new Intent();

        // The Intent contains the custom broadcast action for this app
        localIntent.setAction(Constants.BROADCAST_ACTION);

        // Puts the status into the Intent
        localIntent.putExtra(Constants.EXTENDED_NETWORK_ERROR, state);
        localIntent.addCategory(Intent.CATEGORY_DEFAULT);

        // Broadcasts the Intent
        mBroadcaster.sendBroadcast(localIntent);

    }
    }
