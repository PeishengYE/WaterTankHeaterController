package com.radioyps.watertankheater;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

          /*
         * Creates an intent filter for DownloadStateReceiver that intercepts broadcast Intents
         */

        // The filter's action is BROADCAST_ACTION
        IntentFilter statusIntentFilter = new IntentFilter(
                Constants.BROADCAST_ACTION);

        // Sets the filter's category to DEFAULT
        statusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);

        // Instantiates a new DownloadStateReceiver
        mDownloadStateReceiver = new DownloadStateReceiver();

        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mDownloadStateReceiver,
                statusIntentFilter);
        setContentView(R.layout.activity_main);
    }


    public void onDestroy() {

        // If the DownloadStateReceiver still exists, unregister it and set it to null
        if (mDownloadStateReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mDownloadStateReceiver);
            mDownloadStateReceiver = null;
        }

        // Unregisters the FragmentDisplayer instance
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mFragmentDisplayer);

        // Sets the main View to null
        mMainView = null;

        // Must always call the super method at the end.
        super.onDestroy();
    }


    /**
     * This class uses the BroadcastReceiver framework to detect and handle status messages from
     * the service that downloads URLs.
     */
    private class DownloadStateReceiver extends BroadcastReceiver {

        private DownloadStateReceiver() {

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

                        Log.d(CLASS_TAG, "State: STARTED");
                    }
                    break;
                // Logs "connecting to network" state
                case Constants.STATE_ACTION_CONNECTING:
                    if (Constants.LOGD) {

                        Log.d(CLASS_TAG, "State: CONNECTING");
                    }
                    break;
                // Logs "parsing the RSS feed" state
                case Constants.STATE_ACTION_PARSING:
                    if (Constants.LOGD) {

                        Log.d(CLASS_TAG, "State: PARSING");
                    }
                    break;
                // Logs "Writing the parsed data to the content provider" state
                case Constants.STATE_ACTION_WRITING:
                    if (Constants.LOGD) {

                        Log.d(CLASS_TAG, "State: WRITING");
                    }
                    break;
                // Starts displaying data when the RSS download is complete
                case Constants.STATE_ACTION_COMPLETE:
                    // Logs the status
                    if (Constants.LOGD) {

                        Log.d(CLASS_TAG, "State: COMPLETE");
                    }

                    // Finds the fragment that displays thumbnails
                    PhotoThumbnailFragment localThumbnailFragment =
                            (PhotoThumbnailFragment) getSupportFragmentManager().findFragmentByTag(
                                    Constants.THUMBNAIL_FRAGMENT_TAG);

                    // If the thumbnail Fragment is hidden, don't change its display status
                    if ((localThumbnailFragment == null)
                            || (!localThumbnailFragment.isVisible()))
                        return;

                    // Indicates that the thumbnail Fragment is visible
                    localThumbnailFragment.setLoaded(true);
                    break;
                default:
                    break;
            }
        }
    }

}
