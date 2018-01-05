package com.radioyps.watertankheater;

import android.provider.Settings;
import android.util.Log;

/**
 * Created by yep on 05/01/18.
 */
public class UsbDebuggingMonitorService
        extends MonitorService
{
    private static final String LOG_TAG = "UsbDebuggingService";
    protected boolean canContinue()
    {
        Log.i(LOG_TAG, "canContinue()>> check adb_enabled");
        return Settings.Secure.getInt(getContentResolver(), "adb_enabled", 0) == 1;
    }
}
