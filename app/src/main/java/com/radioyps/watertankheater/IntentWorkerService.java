package com.radioyps.watertankheater;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

import static com.radioyps.watertankheater.Constants.A13_REPLY_SWITCH_OFF;
import static com.radioyps.watertankheater.Constants.A13_REPLY_SWITCH_ON;
import static com.radioyps.watertankheater.Constants.A13_REPLY_WATER_TEMPERATURE;
import static com.radioyps.watertankheater.Constants.HAVE_NETWORK_ERROR;
import static com.radioyps.watertankheater.Constants.Heater_IP_ADDRESS;
import static com.radioyps.watertankheater.Constants.Heater_IP_PORT;
import static com.radioyps.watertankheater.Constants.NETWORK_ERROR;
import static com.radioyps.watertankheater.Constants.NO_NETWORK_ERROR;
import static com.radioyps.watertankheater.Constants.PROGRESS_BAR_OFF;
import static com.radioyps.watertankheater.Constants.PROGRESS_BAR_ON;
import static com.radioyps.watertankheater.Constants.STATE_SWITCH_OFF;
import static com.radioyps.watertankheater.Constants.STATE_SWITCH_ON;

/**
 * Created by yep on 18/12/17.
 */

public class IntentWorkerService extends IntentService {

    public static final String LOG_TAG = "IntentWorkerService";

    // Defines and instantiates an object for handling status updates.
    //private BroadcastNotifier mBroadcaster = new BroadcastNotifier(getApplicationContext());
    private BroadcastNotifier mBroadcaster = new BroadcastNotifier(this);
    private String response;
    private final static int SOCKET_TIMEOUT= 10*000; /*10 seconds */



    /**
     * An IntentService must always have a constructor that calls the super constructor. The
     * string supplied to the super constructor is used to give a name to the IntentService's
     * background thread.
     */
    public IntentWorkerService() {

        super("IntentWorkerService");
        Log.i(LOG_TAG, "IntentWorkerService()>> ");
    }

    /**
     * In an IntentService, onHandleIntent is run on a background thread.  As it
     * runs, it broadcasts its current status using the LocalBroadcastManager.
     * @param workIntent The Intent that starts the IntentService. This Intent contains the
     * URL of the web site from which the RSS parser gets data.
     */
    @Override
    protected void onHandleIntent(Intent workIntent) {

        Log.i(LOG_TAG, "onHandleIntent()>> ");
        // Gets a URL to read from the incoming Intent's "data" value
        String cmd = workIntent.getDataString();

        Log.i(LOG_TAG, "IntentWorkerService()>> getting intent with:  " + cmd);

        sendCmd(cmd);
//        mBroadcaster.broadcastIntentWithProgressBarStatus(PROGRESS_BAR_OFF);

    }

    private void sendCmd(String  cmdString){

        Socket socket = null;


        try {

            response = "";
            socket = new Socket();

            Log.i(LOG_TAG, "onHandleIntent()>> start connecting");
            socket.connect(new InetSocketAddress(
                    Heater_IP_ADDRESS, Heater_IP_PORT), 2000);
            socket.setSoTimeout(SOCKET_TIMEOUT);
            Log.i(LOG_TAG, "onHandleIntent()>> Got connecting");
            ByteArrayOutputStream byteArrayOutputStream =
                    new ByteArrayOutputStream(1024);

            byte[] buffer = new byte[1024];

            int bytesRead;
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();


            outputStream.write(cmdString.getBytes());
            outputStream.flush();


			/*a
			 * notice: inputStream.read() will block if no data return
			 */
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                Log.i(LOG_TAG, "onHandleIntent()>> read one");
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                response += byteArrayOutputStream.toString("UTF-8");
                Log.i(LOG_TAG, "onHandleIntent()>> read with: << " + response +" >>");

            }
            Log.i(LOG_TAG, "onHandleIntent ()>> after reading");
            outputStream.close();
            inputStream.close();

        } catch (UnknownHostException e) {
            Log.i(LOG_TAG, "onHandleIntent()>> exception on UnknownHostException " );
            e.printStackTrace();
            response = NETWORK_ERROR;
        } catch (SocketTimeoutException e) {

            Log.i(LOG_TAG, "onHandleIntent()>> exception on SocketTimeoutException  " );
            e.printStackTrace();
            response = NETWORK_ERROR;
        } catch (IOException e) {
            Log.i(LOG_TAG, "onHandleIntent()>> exception on UnKnown issue  " );
            e.printStackTrace();

            response = NETWORK_ERROR;
        } finally {
            Log.i(LOG_TAG, "onHandleIntent ()>> trying closing socket");
            if (socket != null) {
                try {
                    Log.i(LOG_TAG, "onHandleIntent ()>> do close socket");
                    socket.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }

        if(!response.startsWith(NETWORK_ERROR)){

            mBroadcaster.broadcastIntentWithError(NO_NETWORK_ERROR);

            if (response.equalsIgnoreCase(A13_REPLY_SWITCH_OFF))
                mBroadcaster.broadcastIntentWithSwitchState(STATE_SWITCH_OFF);

            if (response.equalsIgnoreCase(A13_REPLY_SWITCH_ON))
                mBroadcaster.broadcastIntentWithSwitchState(STATE_SWITCH_ON);

            if (response.startsWith(A13_REPLY_WATER_TEMPERATURE)){
                String tmp = response.substring(17, 22);
                Log.i(LOG_TAG, "response with temperature: "+ tmp);
                int tmperature = Integer.parseInt(tmp);
                mBroadcaster.broadcastIntentWithWaterTemperature(tmperature);
            }

        }else{



        }

    }
    }

