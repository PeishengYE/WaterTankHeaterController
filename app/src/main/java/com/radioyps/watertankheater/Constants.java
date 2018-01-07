package com.radioyps.watertankheater;

/**
 * Created by yep on 18/12/17.
 */


        import java.util.Locale;

/**
 *
 * Constants used by multiple classes in this package
 */
public final class Constants {

    // Set to true to turn on verbose logging
    public static final boolean LOGV = false;

    // Set to true to turn on debug logging
    public static final boolean LOGD = true;

    // Custom actions

    public static final String ACTION_VIEW_IMAGE =
            "com.radioyps.watertankheater.ACTION_VIEW_IMAGE";

    public static final String ACTION_ZOOM_IMAGE =
            "com.radioyps.watertankheater.ACTION_ZOOM_IMAGE";

    // Defines a custom Intent action
    public static final String BROADCAST_ACTION = "com.radioyps.watertankheater.BROADCAST";

    // Fragment tags
    public static final String PHOTO_FRAGMENT_TAG =
            "com.radioyps.watertankheater.PHOTO_FRAGMENT_TAG";

    public static final String THUMBNAIL_FRAGMENT_TAG =
            "com.radioyps.watertankheater.THUMBNAIL_FRAGMENT_TAG";

    // Defines the key for the status "extra" in an Intent
    public static final String EXTENDED_SWITCH_STATUS = "com.radioyps.watertankheater.SWITCH_STATUS";
    public static final String EXTENDED_RELAY_TEMPERATURE = "com.radioyps.watertankheater.RELAY_TEMPERATURE";
    public static final String EXTENDED_WATER_TEMPERATURE = "com.radioyps.watertankheater.WATER_TEMPERATURE";
    public static final String EXTENDED_NETWORK_ERROR = "com.radioyps.watertankheater.NETWORK_ERROR";

    public static final String EXTENDED_QUERY_PROGRESS_BAR_STATUS = "com.radioyps.watertankheater.PROGRESS_BAR_STATUS";




    public static final int HAVE_NETWORK_ERROR = 0x44;
    public static final int NO_NETWORK_ERROR = 0x88;

    public static final int POWER_BUTTON_STATUS_UNKNOWN = 0x14;
    public static final int POWER_BUTTON_STATUS_ON = 0x22;
    public static final int POWER_BUTTON_STATUS_OFF = 0x33;

    public static final int PROGRESS_BAR_ON = 0x133;
    public static final int PROGRESS_BAR_OFF = 0x144;

    public static final int YEP_UNKNOWN_ERROR = 0x444;



    // Defines the key for the log "extra" in an Intent
    public static final String EXTENDED_STATUS_LOG = "com.radioyps.watertankheater.LOG";

    // Defines the key for storing fullscreen state
    public static final String EXTENDED_FULLSCREEN =
            "com.radioyps.watertankheater.EXTENDED_FULLSCREEN";

    // Defines the key for storing fullscreen state
    public static final String Heater_IP_ADDRESS =
            "192.168.12.202";

    public static final int Heater_IP_PORT =
            5018;


    public static String CmdGetTemperature = "get_temp";
    public static String CmdGetSwtichStatus =        "get_switch";
    public static String CmdSetSwitchON  =      "switch_on";
    public static String CmdSetSwitchOFF =        "switch_off";

    public static String NETWORK_ERROR =        "Network Timeout";

    public static String A13_REPLY_SWITCH_OFF = "switch is off";
    public static String A13_REPLY_SWITCH_ON = "switch is on";


    public static String A13_REPLY_WATER_TEMPERATURE = "Water Tank Temp:";
//    public static String A13_REPLY_SWITCH_ON = "switch is on";



    public static final int STATE_SWITCH_ON = 0x22;
    public static final int STATE_SWITCH_OFF = 0x33;
    // Status values to broadcast to the Activity

    // The download is starting
    public static final int STATE_ACTION_STARTED = 0;

    // The background thread is connecting to the RSS feed
    public static final int STATE_ACTION_CONNECTING = 1;

    // The background thread is parsing the RSS feed
    public static final int STATE_ACTION_PARSING = 2;

    // The background thread is writing data to the content provider
    public static final int STATE_ACTION_WRITING = 3;

    // The background thread is done
    public static final int STATE_ACTION_COMPLETE = 4;

    // The background thread is doing logging
    public static final int STATE_LOG = -1;

    public static final CharSequence BLANK = " ";
}
