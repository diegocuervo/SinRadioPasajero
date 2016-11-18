package com.example.diegocuervo.sinradio_pasajero;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by Diego Cuervo on 20/10/16.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private Map<String, String> cuerpo;

    private RemoteMessage remoteMensage;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {



        cuerpo = remoteMessage.getData();

        Log.w("probando noti", "entro a onmessagereceived"+cuerpo);

        String patente= cuerpo.get("body");
        String titulo= cuerpo.get("title");
        sendNotification(patente,titulo);

    }
    private void sendNotification(String patente, String titulo) {
        int notificationID = 1;

        long[] pattern = new long[]{1000,2000,2000};

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        CharSequence ticker =titulo;
        CharSequence contentTitle =titulo;
        CharSequence contentText =patente;
        Notification noti = new android.support.v7.app.NotificationCompat.Builder(this)

                .setTicker(ticker)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.taxi_pasajero_mod)
                .setLargeIcon((((BitmapDrawable)getResources()
                      .getDrawable(R.drawable.taxi_pasajero_mod)).getBitmap()))  // este es el de la notificacion a la derecha
                .setSound(defaultSound)
                .setLights(Color.BLUE, 1, 0)
                .setVibrate(pattern)
                .build();
        nm.notify(notificationID, noti);
    }


}
