package com.hoanmy.football.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

/** * Created by ADMIN on 10/14/15.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    public static final String ACTION_NETWORK_STATE_CHANGED = "Action.Network.Changed";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            context.sendBroadcast(new Intent(ACTION_NETWORK_STATE_CHANGED));
        }
    }
}
