package co.digitaldavinci.pollsofhumanity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import co.digitaldavinci.pollsofhumanity.receiver.UpdateAlarmReceiver;

import static android.R.attr.angle;

public class BaseActivity extends AppCompatActivity {
    private FragmentManager fm;
    private Button menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        checkGooglePlayServices();

        fm = getSupportFragmentManager();
        actionBarInit();

        Fragment baseFragment = new BaseFragment();
        FragmentTransaction ft = fm.beginTransaction().replace(R.id.content_frame, baseFragment);
        ft.commit();

        setUpdateAlarm();

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.app_cache_file), Context.MODE_PRIVATE);
        String token = sharedPref.getString(getString(R.string.firebase_key), "");

    }

    private void actionBarInit(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setElevation(0);
        View customView = getLayoutInflater().inflate(R.layout.action_bar, null);
        actionBar.setCustomView(customView);

        menuButton = (Button) customView.findViewById(R.id.show_results);

        Toolbar parent =(Toolbar) customView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-CondLight.ttf");
        ((TextView) customView.findViewById(R.id.title)).setTypeface(font);

    }

    public void setActionBarButtonToChart(){
        menuButton.setBackgroundResource(R.drawable.icon_chart);
        rotateChartIcon();

        menuButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Fragment resultFragment = new ResultFragment();
                FragmentTransaction ft = fm.beginTransaction().replace(R.id.content_frame, resultFragment);
                ft.commit();

            }
        });
    }

    private void checkGooglePlayServices(){
        /*GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();

        if(apiAvailability.isGooglePlayServicesAvailable(this) != ConnectionResult.SUCCESS){
            apiAvailability.makeGooglePlayServicesAvailable(this);
        }*/

    }

    private void rotateChartIcon(){
        AnimationSet animSet = new AnimationSet(true);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.setFillAfter(true);
        animSet.setFillEnabled(true);

        RotateAnimation animRotate = new RotateAnimation(0.0f, getRotateAngle(),
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        animRotate.setDuration(1500);
        animRotate.setFillAfter(true);
        animSet.addAnimation(animRotate);

        menuButton.startAnimation(animSet);
    }

    private float getRotateAngle(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        long curTime = cal.getTimeInMillis();

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 00);
        long alarmTime = calendar.getTimeInMillis();

        float hours;
        if(curTime > alarmTime){
            double millis = ((alarmTime + (24 * 60 * 60 * 1000)) - curTime);
            int minutes = (int) ((millis / (1000.0*60)) % 60);
            hours = ((int) ((millis / (1000.0*60*60)) % 24) ) + minutes / 60.0f;

            System.out.println("Hour Now" + hours);
            System.out.println("Minutes Now" + minutes + "\t" + minutes / 60.0);
            System.out.println("Rotate Angle: " + ((24.0f - hours)/24.0f) * 360.0f);

        }else{
            double millis = (alarmTime  - curTime);
            int minutes = (int) ((millis / (1000.0*60)) % 60);
            hours = ((int)((millis / (1000.0*60*60)) % 24)) + minutes / 60.0f;


            System.out.println("Hour Now" + hours);
            System.out.println("Minutes Now" + minutes + "\t" + minutes / 60.0);
            System.out.println("Rotate Angle: " + ((24.0f - hours)/24.0f) * 360.0f);


        }

        return ((24.0f - hours)/24.0f) * 360.0f;
    }

    public void setActionBarButtonToHome(){

        RotateAnimation animRotate = new RotateAnimation(0.0f, 0.0f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        menuButton.startAnimation(animRotate);

        menuButton.setBackgroundResource(R.drawable.icon_back_arrow);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment baseFragment = new BaseFragment();
                FragmentTransaction ft =  fm.beginTransaction().replace(R.id.content_frame, baseFragment);
                ft.commit();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        checkGooglePlayServices();
    }

    private void setUpdateAlarm(){
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(BaseActivity.this, UpdateAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(BaseActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 00);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
    }

}
