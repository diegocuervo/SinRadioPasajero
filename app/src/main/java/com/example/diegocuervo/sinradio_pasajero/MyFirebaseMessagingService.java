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

/**
 * Created by Diego Cuervo on 20/10/16.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private String cuerpo;
    private String from;
    private RemoteMessage remoteMensage;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        remoteMensage = remoteMessage;
        cuerpo = remoteMessage.getFrom();
        from = remoteMessage.getNotification().getBody();
        sendNotification(remoteMessage.getNotification().getBody());
        Log.w("FIRE BASEEE", remoteMessage.getNotification().getBody());
    }
    private void sendNotification(String messageBody) {
        int notificationID = 1;
        Intent i = new Intent(this, NotificationView.class);
        i.putExtra("notificationID", notificationID);
        long[] pattern = new long[]{1000,2000,2000};

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
       // PendingIntent deleteIntent = PendingIntent.getActivity(this, 0, i, 0);
       // PendingIntent shareIntent = PendingIntent.getActivity(this, 0, i, 0);
      //  PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);
        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        CharSequence ticker ="El Taxi Ha Llegado!";
        CharSequence contentTitle = "El Taxi Ha Llegado!";
        CharSequence contentText = "Su chofer lo esta esperado en la direccion acordada.";
        Notification noti = new android.support.v7.app.NotificationCompat.Builder(this)
            //    .setContentIntent(pendingIntent)
                .setTicker(ticker)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.taxi_pasajero_mod)
                .setLargeIcon((((BitmapDrawable)getResources()
                      .getDrawable(R.drawable.taxi_pasajero_mod)).getBitmap()))  // este es el de la notificacion a la derecha
               //  .addAction(R.drawable.taxi_pasajero_modificado, ticker, pendingIntent)


                .setSound(defaultSound)
                .setLights(Color.BLUE, 1, 0)
                .setVibrate(pattern)
                .build();
        nm.notify(notificationID, noti);
    }


}
