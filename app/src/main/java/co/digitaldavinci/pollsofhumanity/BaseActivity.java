package co.digitaldavinci.pollsofhumanity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import java.util.Calendar;
import java.util.TimeZone;

import co.digitaldavinci.pollsofhumanity.R;

import co.digitaldavinci.pollsofhumanity.receiver.UpdateAlarmReceiver;

public class BaseActivity extends AppCompatActivity {
    private FragmentManager fm;
    private Button menuButton;
    //public Dialog resultsDialog, loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        fm = getSupportFragmentManager();
        actionBarInit();

        Fragment baseFragment = new BaseFragment();
        FragmentTransaction ft = fm.beginTransaction().replace(R.id.content_frame, baseFragment);
        ft.commit();

        //resultsDialog = new Dialog(this);
        //resultsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //loadingDialog = new Dialog(this);
        //loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        /*if(manageSharedPref.getId() == -1){
            loadingDialog.setContentView(R.layout.dialog_get_question);
            loadingDialog.show();
            new GetQuestion(this, gQListener).execute();
        }else{
            questionText.setText(manageSharedPref.getQuestion());
            if(!manageSharedPref.getIsQuestionAnswered()){
                enableSubmit();
            }
        }

        setupUpdateCheck();
        setUpdateAlarm();
    */
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

    /*@Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        System.out.println("IN NEW INTENT");

    }

    @Override
    protected void onResume(){
        super.onResume();

        resultsDialog.setContentView(R.layout.dialog_results);

        int questionId = this.getIntent().getIntExtra("question_id", -1);
        if(questionId != -1){

            new GetResults(questionId, gRListener).execute();
        }

        System.out.println("Show result in resume: " + questionId);
    }

    private void setupUpdateCheck(){
        final Handler updateHandler = new Handler();
        Runnable updateRunnable = new Runnable() {

            @Override
            public void run() {

                updateHandler.postDelayed(this, 30000);
                System.out.println("Checking for updates");
                if(manageSharedPref.getUpdated()){
                    questionText.setText(manageSharedPref.getQuestion());
                    enableSubmit();
                    manageSharedPref.setUpdated(false);
                }
            }
        };

        updateHandler.postDelayed(updateRunnable, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    @Override
    public void onBackPressed(){
        finish();
    }*/

    private void setUpdateAlarm(){
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(BaseActivity.this, UpdateAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(BaseActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 00);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
        //System.currentTimeMillis(), AlarmManager.INTERVAL_DAY, pi);//
    }

}
