package com.example.anamoni.skopjeshiddenjewels;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by AnaMoni on 15-Jun-17.
 */

public class GeofenceService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    private static final String TAG = "SERVICE";
    public static final long NEVER_EXPIRE = -1;
    private GoogleApiClient mClient = null;
    private List<Geofence> mGeofences = new ArrayList<Geofence>();
    private PendingIntent pIntent = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        final float radius = 0.5f * 1609.0f;
        Geofence.Builder gb = new Geofence.Builder();
// Make a half mile geofence around your home
        Geofence home = gb.setCircularRegion(42.020230, 21.358191, radius)
                .setTransitionTypes(
                        Geofence.GEOFENCE_TRANSITION_ENTER)
                .setExpirationDuration(NEVER_EXPIRE)// 12 hours
                .setLoiteringDelay(300000) // 5 minutes
                .setRequestId("home")
                .setNotificationResponsiveness(5000) // 5 secs
                .build();
        mGeofences.add(home);

// Krofnite od hrom
        Geofence hrom = gb.setCircularRegion(42.0039592, 21.3696179,
                radius)
                .setTransitionTypes(
                        Geofence.GEOFENCE_TRANSITION_ENTER)
                .setExpirationDuration(NEVER_EXPIRE)
                .setRequestId("hrom")
                .build();
        mGeofences.add(hrom);

        //Sv.Gjorgji, Kuchkovo
        Geofence svGjorgji = gb.setCircularRegion(42.0663859, 21.3064183,
                radius)
                .setTransitionTypes(
                        Geofence.GEOFENCE_TRANSITION_ENTER)
                .setExpirationDuration(NEVER_EXPIRE)
                .setRequestId("svGjorgji")
                .build();
        mGeofences.add(svGjorgji);

        //Urban Square Park
        Geofence urban = gb.setCircularRegion(41.995081, 21.3955443,
                radius)
                .setTransitionTypes(
                        Geofence.GEOFENCE_TRANSITION_ENTER)
                .setExpirationDuration(NEVER_EXPIRE)
                .setRequestId("urban")
                .build();
        mGeofences.add(urban);

        //Markovo kruvche
        Geofence marK = gb.setCircularRegion(41.9682582, 21.429299,
                radius)
                .setTransitionTypes(
                        Geofence.GEOFENCE_TRANSITION_ENTER)
                .setExpirationDuration(NEVER_EXPIRE)
                .setRequestId("marK")
                .build();
        mGeofences.add(marK);

        //Kozjak
        Geofence kozjak = gb.setCircularRegion(41.8786668, 21.1905257,
                radius)
                .setTransitionTypes(
                        Geofence.GEOFENCE_TRANSITION_ENTER)
                .setExpirationDuration(NEVER_EXPIRE)
                .setRequestId("kozjak")
                .build();
        mGeofences.add(kozjak);

        //Vrv kale
        Geofence vrvKale = gb.setCircularRegion(41.9567971, 21.3136662,
                radius)
                .setTransitionTypes(
                        Geofence.GEOFENCE_TRANSITION_ENTER)
                .setExpirationDuration(NEVER_EXPIRE)
                .setRequestId("vrvKale")
                .build();
        mGeofences.add(vrvKale);

        //Sladoled vo park
        Geofence park = gb.setCircularRegion(42.0040721, 21.4230783,
                radius)
                .setTransitionTypes(
                        Geofence.GEOFENCE_TRANSITION_ENTER)
                .setExpirationDuration(NEVER_EXPIRE)
                .setRequestId("park")
                .build();
        mGeofences.add(park);

        //fakultet
        Geofence fax = gb.setCircularRegion(42.0049212, 21.40858,
                radius)
                .setTransitionTypes(
                        Geofence.GEOFENCE_TRANSITION_ENTER)
                .setExpirationDuration(NEVER_EXPIRE)
                .setRequestId("fax")
                .build();
        mGeofences.add(fax);


        Intent newIntent = new Intent(this, ReceiveTransitionsIntentService.class);
        pIntent = PendingIntent.getService(GeofenceService.this, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();

        mClient.connect();

        return START_STICKY;
    }





    @Override
    public void onConnected(Bundle arg0) {
// Set up geofences


        Log.v(TAG, "Setting up geofences (onConnected)...");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        PendingResult<Status> pResult
                = LocationServices.GeofencingApi.addGeofences(mClient,
                mGeofences, pIntent);
        pResult.setResultCallback(this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onResult(Status status) {
        Log.v(TAG, "Got a result from addGeofences("
                + status.getStatusCode() + "): "
                + status.getStatus().getStatusMessage());


    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
