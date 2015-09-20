package pollsofhumanity.hardikar.com.pollsofhumanity.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import pollsofhumanity.hardikar.com.pollsofhumanity.BaseActivity;
import pollsofhumanity.hardikar.com.pollsofhumanity.ManageSharedPref;
import pollsofhumanity.hardikar.com.pollsofhumanity.server.GetQuestion;
import pollsofhumanity.hardikar.com.pollsofhumanity.server.listener.GetQuestionListener;
import pollsofhumanity.hardikar.com.pollsofhumanity.server.holder.QuestionHolder;

/**
 *
 */
public class UpdateAlarmReceiver extends BroadcastReceiver {
    private ManageSharedPref manageSharedPref;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("Alarm received this time");
        manageSharedPref = new ManageSharedPref(context);
        new GetQuestion(context, gQListener).execute();
        this.context = context;

        System.out.println(manageSharedPref.getQuestion());
    }

    GetQuestionListener gQListener = new GetQuestionListener() {
        @Override
        public void onGetQuestionComplete(QuestionHolder question) {
            System.out.println("Current question id: " + manageSharedPref.getId());
            System.out.println("New question id: " + question.getId());

            if(manageSharedPref.getId() != question.getId()){

                manageSharedPref.setResultsId(manageSharedPref.getId());
                manageSharedPref.setIsQuestionAnswered(false);
                manageSharedPref.setUpdated(true);
                manageSharedPref.setQuestion(question.getQuestion());
                manageSharedPref.setId(question.getId());

                setResultsAlarm();
            }else{
                System.out.println("Question not updated");
            }


        }
    };

    private void setResultsAlarm(){
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ResultsAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 11);

        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
    }
}
