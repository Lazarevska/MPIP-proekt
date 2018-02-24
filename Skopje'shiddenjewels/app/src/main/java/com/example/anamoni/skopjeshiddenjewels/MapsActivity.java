package com.example.anamoni.skopjeshiddenjewels;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzasb;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingApi;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    private static final String TAG = "MAP";
    private GoogleMap mMap;
    private GoogleMap map;

    int check = 0;

    //nov kod



    DBHelper helper = new DBHelper(MapsActivity.this);
    String c = helper.getDatabaseName();
    //boolean check = helper.checkDataBase();
    ArrayList<String> Locations;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        Intent intent = new Intent(this, GeofenceService.class);
        startService(intent);
        //String[] a = { };
        //helper.delete(a);
        //nov kod vo oncreated


        if(c != null){

            //Locations.clear();
            Locations = helper.getAllLocations();
        }
        else {
            Locations.add("Empty");
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        map = googleMap;
        // Add a marker in Skopje and move the camera
        LatLng skopje = new LatLng(41.9973, 21.4280);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(skopje, 15));
        Log.v(TAG, "Camera moved");






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.spinner, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row, R.id.weekofday, Locations);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();





    }

    @Override
    protected void onPause() {
        super.onPause();
        //LocationServices.GeofencingApi.removeGeofences(mClient,pIntent);

    }




    //nov kod





    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if(++check > 1) {
            String lo = (String) adapterView.getSelectedItem();
            ArrayList<String> loc = helper.getLocation(lo);

            String lat = loc.get(0);
            String lng = loc.get(1);

            double lat1 = Double.parseDouble(lat);
            double lng1 = Double.parseDouble(lng);
            LatLng tochka = new LatLng(lat1, lng1);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(tochka, 17));
            map.addMarker(new MarkerOptions().position(tochka).title(lo));
            Log.v(TAG, "HOME");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
