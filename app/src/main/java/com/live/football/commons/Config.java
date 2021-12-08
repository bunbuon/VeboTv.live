package com.hoanmy.football.commons;



public class Config {

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global_film9x";
    public static final String TOPIC_TEST = "test";
    public static final String TOPIC_DEMO = "demo";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final String ACTION_USER_PRESENT = "android.intent.action.ACTION_USER_PRESENT";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";
}
