package com.example.anamoni.skopjeshiddenjewels;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.nfc.Tag;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.internal.WebDialog;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import com.facebook.FacebookSdk;

public class LocationDetailsActivity extends AppCompatActivity {

    private static final String TAG = "LOCATION_DETAILS";
    private SQLiteDatabase db;
    private CursorAdapter dataSource;
    Context context = getBaseContext();


    //facebook

    private Button mFacebookBtn;
    private GoogleMap map;
    Intent intent;
    ShareDialog shareDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_location_details);


        Intent i = getIntent();
        final LatLng location = i.getParcelableExtra("locations");
        final double lat = location.latitude;
        final double lng = location.longitude;
        final String name = i.getStringExtra("name");

        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DBHelper helper = new DBHelper(LocationDetailsActivity.this);
                helper.insertLocation(name, lat, lng);
                intent = new Intent(LocationDetailsActivity.this, MapsActivity.class);
                LocationDetailsActivity.this.startActivity(intent);
                //map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));

            }
        });

        shareDialog = new ShareDialog(this);
        mFacebookBtn = (Button) findViewById(R.id.fb_button);
        mFacebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(shareDialog.canShow(ShareLinkContent.class)){

                Log.v(TAG, "Button clicked");

                final ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://developers.facebook.com"))
                        .setContentTitle("I just discovered a new jewel!")
                        .setContentDescription("I discovered a new location with Skopje's hidden jewels app. Try it out!")
                        .build();
                //mFacebookBtn.setShareContent(content);
                shareDialog.show(content);
            }


            }
        });

    }





}
