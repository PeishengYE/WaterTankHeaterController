package com.radioyps.watertankheater;

/**
 * Created by yep on 05/01/18.
 */


        import android.app.Service;
        import android.content.Intent;
        import android.net.Uri;
        import android.os.Handler;
        import android.os.IBinder;
        import android.util.Log;

        import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
        import static com.radioyps.watertankheater.Constants.CmdGetSwtichStatus;
        import static com.radioyps.watertankheater.Constants.CmdGetTemperature;
        import static com.radioyps.watertankheater.Constants.PROGRESS_BAR_OFF;
        import static com.radioyps.watertankheater.Constants.PROGRESS_BAR_ON;

public abstract class MonitorService
        extends Service
{
    private Handler mHandler;
    private static final String LOG_TAG = "MonitorService";
    private BroadcastNotifier mBroadcaster = new BroadcastNotifier(this);
    private static boolean isProgressBarEnabled = false;
    private void cleanupAndShutdown()
    {
        Handler localHandler = this.mHandler;
        if (localHandler != null)
        {
            localHandler.removeCallbacksAndMessages(null);
            this.mHandler = null;
        }
        stopSelf();
    }

    protected abstract boolean canContinue();

    public IBinder onBind(Intent paramIntent)
    {
        return null;
    }

    public void onCreate()
    {
        super.onCreate();
        Handler localHandler = new Handler();
        this.mHandler = localHandler;
        new Runnable()
        {
            public void run()
            {

                Log.i(LOG_TAG, "runnable()>> running");
                if (MonitorService.this.mHandler == null) {
                    return;
                }
//                if (MonitorService.this.canContinue())
//                {
//
//                    Log.i(LOG_TAG, "runnable()>> run the task: ");
//                    querySwitchStatus();
//                    queryTemperature();
////                    Intent localIntent = new Intent(MonitorService.this.getBaseContext(), MainActivity.class);
////                    localIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
////                    localIntent.putExtra("goto", true);
////                    MonitorService.this.startActivity(localIntent);
////                    MonitorService.this.cleanupAndShutdown();
//                    return;
//                }
                if(isProgressBarEnabled){
                    mBroadcaster.broadcastIntentWithProgressBarStatus(PROGRESS_BAR_OFF);
                    isProgressBarEnabled = false;
                }else {
                    mBroadcaster.broadcastIntentWithProgressBarStatus(PROGRESS_BAR_ON);
                    isProgressBarEnabled = true;
                }

                querySwitchStatus();
                queryTemperature();
                MonitorService.this.mHandler.postDelayed(this, 10*1000L);

            }
        }.run();
        /*
        localHandler.postDelayed(new Runnable()
        {
            public void run()
            {
                MonitorService.this.cleanupAndShutdown();
            }
        }, 60000L);
        */
    }

    public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
    {
        super.onStartCommand(paramIntent, paramInt1, paramInt2);
        return START_STICKY;
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
}

