package com.radioyps.watertankheater;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.Surface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Hashtable;

/**
 * Created by yep on 06/02/18.
 */

public class Main {

    public static final String LOG_TAG = "watertankheater.Main";
    public static void main(String[] paramArrayOfString)
            throws Exception
    {
      final Object paramTable = new Hashtable();
      int numParam = paramArrayOfString.length;
      int i = 0;


      while (i < numParam){
       String tmp[] = paramArrayOfString[i].split("=", 2);
       String value ="";
       if(tmp.length == 2){
           value = tmp[1];
       }
          ((Hashtable)paramTable).put(tmp[0], value);
        i += 1;
        Log.i("YEP inside: ", tmp[0] + " = " + tmp[1]);
      }
        checkSurceControlPermission();
        dimDisplay(true);
        makeScreenShot();
    }


    private static File getPicStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }
    private static void saveBitmap(Bitmap bmp)
    {

        FileOutputStream out = null;
        try {
            File file = new File(getPicStorageDir("TEST"), "screenshot.png");

            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void makeScreenShot()
    {
        Log.d(LOG_TAG, "Main.makeScreenShot()>>");
        try {
            Class<?> surfaceControlClass = Class.forName("android.view.SurfaceControl");
            Method mGetScreenshot = surfaceControlClass.getDeclaredMethod("screenshot", new Class[] { Integer.TYPE, Integer.TYPE });
            Bitmap bmp = (Bitmap)mGetScreenshot.invoke(null, new Object[] { Integer.valueOf(1024), Integer.valueOf(600) });
            saveBitmap(bmp);
            Log.d(LOG_TAG, "Main.makeScreenShot()<<");
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public static void checkSurceControlPermission()
    {
        String mName = "helloSurface";
        Log.d(LOG_TAG, "Main.checkSurceControlPermission()>>");
        try {
            final Method localMethod6 = Class.forName("android.os.ServiceManager").
                    getDeclaredMethod("getService", new Class[]{String.class});

            Class<?> surfaceControlClass = Class.forName("android.view.SurfaceControl");

            Method mCreateDisplay = surfaceControlClass.getDeclaredMethod("createDisplay", String.class, Boolean.TYPE);

            IBinder mIBinder = (IBinder) mCreateDisplay.invoke(null,mName,Boolean.valueOf(false));
            Method mSetDisplaySurface = surfaceControlClass.getDeclaredMethod("setDisplaySurface",
                    new Class[]{IBinder.class, Surface.class});

            final Method mSetDisplayProjection = surfaceControlClass.getDeclaredMethod("setDisplayProjection",
                    new Class[]{IBinder.class, Integer.TYPE, Rect.class, Rect.class});

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
            Log.d(LOG_TAG, "Main.checkSurceControlPermission()<<");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public  static void dimDisplay(boolean paramBoolean)
    {


            try
            {
                Log.d(LOG_TAG, "Main.dimDisplay()>>");
                Runtime localRuntime = Runtime.getRuntime();
                if (paramBoolean)
                {

                    Object localObject = localRuntime.exec(new String[] { "/system/bin/settings", "put", "system", "screen_brightness_mode", "0" });
                    if (!paramBoolean) {
                        return ;
                    }
                    ((Process)localObject).waitFor();
                    localObject = Runtime.getRuntime().exec(new String[] { "/system/bin/settings", "put", "system", "screen_brightness", "100" });
                    return ;
                }
            }
            catch (Exception localException)
            {
                Log.d(LOG_TAG, "error on dim display");
                localException.printStackTrace();
                return ;
            }

    }

}
