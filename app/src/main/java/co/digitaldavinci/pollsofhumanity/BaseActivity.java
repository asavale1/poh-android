package co.digitaldavinci.pollsofhumanity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.TimeZone;

import co.digitaldavinci.pollsofhumanity.receiver.UpdateAlarmReceiver;

public class BaseActivity extends AppCompatActivity {
    private FragmentManager fm;
    private Button menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        fm = getSupportFragmentManager();
        actionBarInit();

        Fragment baseFragment = new BaseFragment();
        FragmentTransaction ft = fm.beginTransaction().replace(R.id.content_frame, baseFragment);
        ft.commit();

        setUpdateAlarm();
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

    public void setActionBarButtonToResult(){
        menuButton.setBackgroundResource(R.drawable.graph);
        menuButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Fragment resultFragment = new ResultFragment();
                FragmentTransaction ft = fm.beginTransaction().replace(R.id.content_frame, resultFragment);
                ft.commit();

            }
        });
    }

    public void setActionBarButtonToHome(){
        menuButton.setBackgroundResource(R.drawable.arrow);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment baseFragment = new BaseFragment();
                FragmentTransaction ft =  fm.beginTransaction().replace(R.id.content_frame, baseFragment);
                ft.commit();
            }
        });
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
        //System.currentTimeMillis(), AlarmManager.INTERVAL_DAY, pi);// calendar.getTimeInMillis()
    }

}
