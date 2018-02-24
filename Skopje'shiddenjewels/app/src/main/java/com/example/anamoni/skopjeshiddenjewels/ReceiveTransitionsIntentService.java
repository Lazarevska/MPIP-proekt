package com.example.anamoni.skopjeshiddenjewels;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by sasko on 01.06.2017.
 */

public class ReceiveTransitionsIntentService extends IntentService{
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *  Used to name the worker thread, important only for debugging.
     */
    public ReceiveTransitionsIntentService() {
        super("ReceiveTransitionsIntentService");
    }

    //public ReceiveTransitionsIntentService(String name) { super(name); }

    NotificationManager notificationMgr;

    public void onCreate() {
        super.onCreate();
        notificationMgr = (NotificationManager)getSystemService(
                NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_REDELIVER_INTENT;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent gfEvent = GeofencingEvent.fromIntent(intent);
// First check for errors
        if (gfEvent.hasError()) {
// Get the error code with a static method
            int errorCode = gfEvent.getErrorCode();
// Log the error
            Log.e(TAG, "Location Services error: " +
                    Integer.toString(errorCode));
/*
* If there's no error, get the transition type and the
IDs
* of the geofence or geofences that triggered the
transition
*/
        } else {
// Get the type of transition (entry or exit)
            int transitionType =
                    gfEvent.getGeofenceTransition();
            String tranTypeStr = "UNKNOWN(" + transitionType
                    + ")";
            switch(transitionType) {
                case Geofence.GEOFENCE_TRANSITION_ENTER:
                    tranTypeStr = "ENTER";
                    break;
                case Geofence.GEOFENCE_TRANSITION_EXIT:
                    tranTypeStr = "EXIT";
                    break;
                case Geofence.GEOFENCE_TRANSITION_DWELL:
                    tranTypeStr = "DWELL";
                    break;
            }
            Log.v(TAG, "transitionType reported: " + tranTypeStr);
            Location triggerLoc = gfEvent.getTriggeringLocation();
            Log.v(TAG, "triggering location is " + triggerLoc);
            List<Geofence> triggerList = gfEvent.getTriggeringGeofences();
            String[] triggerIds = new String[triggerList.size()];
            for (int i = 0; i < triggerIds.length; i++) {
// Grab the Id of each geofence
                triggerIds[i] = triggerList.get(i).getRequestId();
                String msg = "Continue to collect it!";
                String title = "You found a new hidden jewel!";
                double lat =  triggerLoc.getLatitude();
                double lng = triggerLoc.getLongitude();
                String name = triggerIds[0];
                displayNotificationMessage(title, msg, name, lat, lng);
            }
        }


    }
    private void displayNotificationMessage(String title, String
            message, String name, double lat, double lng)
    {


        LatLng location = new LatLng(lat, lng);
        Bundle args = new Bundle();
        Bundle args1 = new Bundle();
        args.putParcelable("locations", location );
        args1.putString("name", name);


        Intent intent = new Intent(this, PopUpActivity.class);
        intent.putExtras(args);
        intent.putExtras(args1);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);


        int notif_id = (int) (System.currentTimeMillis() &
                0xFFL);
        Notification notification = new
                NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pIntent)
                .setSmallIcon(R.drawable.diamond153970_960_720)
                .setOngoing(false)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationMgr.notify(notif_id, notification);
    }
}
