package com.example.diegocuervo.sinradio_pasajero;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Diego Cuervo on 20/10/16.
 */
public class AutoArranque extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context,  MyFirebaseMessagingService.class);
        context.startService(service);
    }

}
