package com.example.anamoni.skopjeshiddenjewels;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class PopUpActivity extends AppCompatActivity {


    private RadioGroup mMode;
    Intent intent = null;
    LatLng loc;
    String name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);

        Intent i = getIntent();
        loc = i.getParcelableExtra("locations");
        name = i.getStringExtra("name");

        popUpWindow();
    }



    private void popUpWindow() {
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.pop_up, null);
        popupView.measure(View.MeasureSpec.makeMeasureSpec(450, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(650, View.MeasureSpec.EXACTLY));
        final PopupWindow popupWindow = new PopupWindow(popupView, popupView.getMeasuredWidth(), popupView.getMeasuredHeight(), true);
        findViewById(R.id.popup_activity).post(new Runnable() {
            public void run() {
                popupWindow.showAtLocation(findViewById(R.id.popup_activity), Gravity.CENTER, 0, 0);
            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.setIgnoreCheekPress();
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        mMode = (RadioGroup) popupView.findViewById(R.id.radioScreenMode);
        mMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (mMode.getCheckedRadioButtonId()) {
                    case R.id.radio0:
                        Toast.makeText(getApplicationContext(),"This answer is incorrect. Try again!",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.radio1:
                        Bundle args1 = new Bundle();
                        Bundle args2 = new Bundle();
                        args1.putParcelable("locations", loc);
                        args2.putString("name", name);
                        intent = new Intent(PopUpActivity.this, LocationDetailsActivity.class);
                        intent.putExtras(args1);
                        intent.putExtras(args2);
                        PopUpActivity.this.startActivity(intent);
                        break;
                    case R.id.radio2:
                        Toast.makeText(getApplicationContext(), "This answer is incorrect. Try again!", Toast.LENGTH_LONG).show();
                    default:
                        break;
                }
            }
        });

    }
}
