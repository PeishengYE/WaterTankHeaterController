package com.radioyps.watertankheater;

/**
 * Created by yep on 05/01/18.
 */


        import android.app.Service;
        import android.content.Intent;
        import android.graphics.Rect;
        import android.net.Uri;
        import android.os.Handler;
        import android.os.IBinder;
        import android.util.Log;
        import android.view.Surface;

        import java.lang.reflect.Method;
//        import android.hardware.display.IDisplayManager;
//        import android.view.IWindowManager;
//        import android.view.IWindowManager.Stub;


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
    private static final String mName = "stdout";

    Rect displayRect;

    public void cleanupAndShutdown()
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
        Log.i(LOG_TAG, "onCreate()>> ");


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

try {

    final Method localMethod6 = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", new Class[] { String.class });

//    IWindowManager wm = IWindowManager.Stub.asInterface((IBinder)localMethod6.invoke(null, new Object[] { "window" }));
    Class<?> surfaceControlClass = Class.forName("android.view.SurfaceControl");

    /*
    *   public static IBinder createDisplay(String name, boolean secure) {
        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        }
        return nativeCreateDisplay(name, secure);
    }*/

    Method mCreateDisplay = surfaceControlClass.getDeclaredMethod("createDisplay", String.class, Boolean.TYPE);

    IBinder mIBinder = (IBinder) mCreateDisplay.invoke(null,mName,Boolean.valueOf(false));
    /*
    IBinder mIBinder = (IBinder) surfaceControlClass.getDeclaredMethod("createDisplay",
            new Class[]{String.class, Boolean.TYPE})
            .invoke(null, new Object[]{mName, Boolean.valueOf(false)});
            */




/*
    public static void setDisplaySurface(IBinder displayToken, Surface surface) {
        if (displayToken == null) {
            throw new IllegalArgumentException("displayToken must not be null");
        }
        if (surface != null) {
            synchronized (surface.mLock) {
                nativeSetDisplaySurface(displayToken, surface.mNativeObject);
            }
        } else {
            nativeSetDisplaySurface(displayToken, 0);
        }
    }
    */

    Method mSetDisplaySurface = surfaceControlClass.getDeclaredMethod("setDisplaySurface",
            new Class[]{IBinder.class, Surface.class});

/*
    public static void setDisplayProjection(IBinder displayToken,
    int orientation, Rect layerStackRect, Rect displayRect) {
        if (displayToken == null) {
            throw new IllegalArgumentException("displayToken must not be null");
        }
        if (layerStackRect == null) {
            throw new IllegalArgumentException("layerStackRect must not be null");
        }
        if (displayRect == null) {
            throw new IllegalArgumentException("displayRect must not be null");
        }
        nativeSetDisplayProjection(displayToken, orientation,
                layerStackRect.left, layerStackRect.top, layerStackRect.right, layerStackRect.bottom,
                displayRect.left, displayRect.top, displayRect.right, displayRect.bottom);
    }
*/

    final Method mSetDisplayProjection = surfaceControlClass.getDeclaredMethod("setDisplayProjection",
            new Class[]{IBinder.class, Integer.TYPE, Rect.class, Rect.class});

    /******************************/
   /*
    public static void setDisplayLayerStack(IBinder displayToken, int layerStack) {
        if (displayToken == null) {
            throw new IllegalArgumentException("displayToken must not be null");
        }
        nativeSetDisplayLayerStack(displayToken, layerStack);
    }
    */
    Method mSetDisplayLayerStack = surfaceControlClass.getDeclaredMethod("setDisplayLayerStack",
            new Class[]{IBinder.class, Integer.TYPE});



    /*
    public static void openTransaction() {
        nativeOpenTransaction();
    }*/
    final Method mOpenTransaction = surfaceControlClass.getDeclaredMethod("openTransaction",
            new Class[0]);

    /** end a transaction
     public static void closeTransaction() {
     nativeCloseTransaction(false);
     }
     */
    final Method mCloseTransaction = surfaceControlClass.getDeclaredMethod("closeTransaction",
            new Class[0]);


    Rect displayRect = new Rect(0, 0, 800, 480);

    Rect localRect = new Rect(0, 0, 800, 480);

    mOpenTransaction.invoke(null, new Object[0]);

    //mSetDisplaySurface.invoke(null, new Object[] { paramString, paramSurface });

    mSetDisplayProjection.invoke(null, new Object[] { mIBinder, Integer.valueOf(0), localRect, displayRect });

    mSetDisplayLayerStack.invoke(null, new Object[] { mIBinder, Integer.valueOf(0) });

    mCloseTransaction.invoke(null, new Object[0]);

}catch (Exception e){

                    e.printStackTrace();
}
                if(isProgressBarEnabled){
                    mBroadcaster.broadcastIntentWithProgressBarStatus(PROGRESS_BAR_OFF);
                    isProgressBarEnabled = false;
                }else {
                    mBroadcaster.broadcastIntentWithProgressBarStatus(PROGRESS_BAR_ON);
                    isProgressBarEnabled = true;
                }

                //querySwitchStatus();
                //queryTemperature();
                MonitorService.this.mHandler.postDelayed(this, 10*1000L);

            }
        }.run();

        localHandler.postDelayed(new Runnable()
        {
            public void run()
            {
                MonitorService.this.cleanupAndShutdown();
            }
        }, 60000L);


        Log.i(LOG_TAG, "onCreate()<< ");
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

