package com.radioyps.watertankheater;

/**
 * Created by yep on 05/01/18.
 */


        import android.app.Service;
        import android.content.Intent;
        import android.os.Handler;
        import android.os.IBinder;
        import android.util.Log;

        import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public abstract class MonitorService
        extends Service
{
    private Handler mHandler;
    private static final String LOG_TAG = "MonitorService";
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
                if (MonitorService.this.canContinue())
                {
                    Intent localIntent = new Intent(MonitorService.this.getBaseContext(), MainActivity.class);
                    localIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    localIntent.putExtra("goto", true);
                    MonitorService.this.startActivity(localIntent);
                    MonitorService.this.cleanupAndShutdown();
                    return;
                }
                MonitorService.this.mHandler.postDelayed(this, 1000L);
            }
        }.run();
        localHandler.postDelayed(new Runnable()
        {
            public void run()
            {
                MonitorService.this.cleanupAndShutdown();
            }
        }, 60000L);
    }

    public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
    {
        super.onStartCommand(paramIntent, paramInt1, paramInt2);
        return START_STICKY;
    }
}

