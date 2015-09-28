package pollsofhumanity.hardikar.com.pollsofhumanity;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Calendar;

import pollsofhumanity.hardikar.com.pollsofhumanity.receiver.ResultsAlarmReceiver;
import pollsofhumanity.hardikar.com.pollsofhumanity.receiver.UpdateAlarmReceiver;
import pollsofhumanity.hardikar.com.pollsofhumanity.server.GetQuestion;
import pollsofhumanity.hardikar.com.pollsofhumanity.server.listener.GetQuestionListener;
import pollsofhumanity.hardikar.com.pollsofhumanity.server.GetResults;
import pollsofhumanity.hardikar.com.pollsofhumanity.server.listener.GetResultsListener;
import pollsofhumanity.hardikar.com.pollsofhumanity.server.PostAnswer;
import pollsofhumanity.hardikar.com.pollsofhumanity.server.listener.PostAnswerListener;
import pollsofhumanity.hardikar.com.pollsofhumanity.server.holder.QuestionHolder;
import pollsofhumanity.hardikar.com.pollsofhumanity.server.holder.ResultsHolder;

public class BaseActivity extends AppCompatActivity {
    private TextView questionText;
    private Button yesButton, noButton;
    private ManageSharedPref manageSharedPref;
    public Dialog resultsDialog, loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        manageSharedPref = new ManageSharedPref(getApplicationContext());

        resultsDialog = new Dialog(this);
        resultsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog = new Dialog(this);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        yesButton = (Button)findViewById(R.id.butt_Yes);
        yesButton.setOnClickListener(yesListener);

        noButton= (Button)findViewById(R.id.butt_No);
        noButton.setOnClickListener(noListener);

        questionText=(TextView) findViewById(R.id.question_Text);

        disableSubmit();

        //calculateTime();

        if(manageSharedPref.getId() == -1){
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

    }

    View.OnClickListener yesListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loadingDialog.setContentView(R.layout.dialog_submit_answer);
            loadingDialog.show();
            new PostAnswer(manageSharedPref.getId(), "yes", pAListener).execute();
        }
    };
    View.OnClickListener noListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loadingDialog.setContentView(R.layout.dialog_submit_answer);
            loadingDialog.show();
            new PostAnswer(manageSharedPref.getId(), "no", pAListener).execute();
        }
    };

    @Override
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

    private void enableSubmit(){

        yesButton.setVisibility(View.VISIBLE);
        noButton.setVisibility(View.VISIBLE);
    }

    private void disableSubmit(){
        yesButton.setVisibility(View.INVISIBLE);
        noButton.setVisibility(View.INVISIBLE);
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

    GetQuestionListener gQListener = new GetQuestionListener() {
        @Override
        public void onGetQuestionComplete(QuestionHolder question) {
            loadingDialog.dismiss();
            questionText.setText(question.getQuestion());

            manageSharedPref.setIsQuestionAnswered(false);
            manageSharedPref.setUpdated(false);
            manageSharedPref.setQuestion(question.getQuestion());
            manageSharedPref.setId(question.getId());
            enableSubmit();

        }
    };

    PostAnswerListener pAListener = new PostAnswerListener() {
        @Override
        public void onPostAnswerComplete() {
            loadingDialog.dismiss();
            manageSharedPref.setIsQuestionAnswered(true);
            manageSharedPref.setUpdated(false);
            System.out.println("Answered: " + manageSharedPref.getIsQuestionAnswered());
            disableSubmit();
        }
    };

    GetResultsListener gRListener = new GetResultsListener() {
        @Override
        public void onGetResultsComplete(ResultsHolder results) {

            ((PieChart) resultsDialog.findViewById(R.id.pie_chart)).setNoCount(results.getNoCount());
            ((PieChart) resultsDialog.findViewById(R.id.pie_chart)).setYesCount(results.getYesCount());

            DecimalFormat df = new DecimalFormat("#.##");

            double yesRatio = ((double) results.getYesCount()) / ((double) (results.getTotal()));
            ((TextView) resultsDialog.findViewById(R.id.yes_count)).setText("Yes " + df.format(yesRatio * 100) + "%");
            ((TextView) resultsDialog.findViewById(R.id.no_count)).setText("No " + df.format((1 - yesRatio) * 100) + "%");

            ((TextView) resultsDialog.findViewById(R.id.question)).setText(results.getQuestion());
            resultsDialog.show();

        }
    };
    
    private void setUpdateAlarm(){
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(BaseActivity.this, UpdateAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(BaseActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);

        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
        //am.set(AlarmManager.RTC_WAKEUP, calculateTime(), pi);

    }

    private long calculateTime(){
        long offset;
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        System.out.println("HOUR: " + hour);
        if(hour < 12){
            offset = (long)((12 - hour)*60*60*1000);
            System.out.println("Before " + offset);
        }else{
            offset = (long)(((24 - hour)+12) * 60 * 60 * 1000);
            System.out.println(offset);
        }

        return (System.currentTimeMillis() + offset);
    }

    @Override
    public void onBackPressed(){
        finish();
    }

}
