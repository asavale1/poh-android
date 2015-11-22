package co.digitaldavinci.pollsofhumanity.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import co.digitaldavinci.pollsofhumanity.BaseActivity;
import co.digitaldavinci.pollsofhumanity.ManageSharedPref;
import co.digitaldavinci.pollsofhumanity.R;
import co.digitaldavinci.pollsofhumanity.server.GetQuestion;
import co.digitaldavinci.pollsofhumanity.server.listener.GetQuestionListener;
import co.digitaldavinci.pollsofhumanity.server.holder.QuestionHolder;

/**
 *
 */
public class UpdateAlarmReceiver extends BroadcastReceiver {
    private ManageSharedPref manageSharedPref;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder nb;

    @Override
    public void onReceive(Context context, Intent intent) {
        manageSharedPref = new ManageSharedPref(context);
        new GetQuestion(context, gQListener).execute();

        mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        nb = new NotificationCompat.Builder(context);
        nb.setContentTitle("New Question");
        nb.setAutoCancel(true);
        nb.setSmallIcon(R.drawable.notification_icon);

        Intent resultIntent = new Intent(context, BaseActivity.class);

        PendingIntent pi = PendingIntent.getActivity(context, 0, resultIntent, 0);
        nb.setContentIntent(pi);
    }

    GetQuestionListener gQListener = new GetQuestionListener() {
        @Override
        public void onGetQuestionComplete(QuestionHolder question) {
            System.out.println("Current question id: " + manageSharedPref.getCurrentQuestionId());
            System.out.println("New question id: " + question.getId());

            if(manageSharedPref.getCurrentQuestionId() != question.getId()){
                manageSharedPref.setUpdate(true);
                mNotificationManager.notify(
                        0,
                        nb.build()
                );
            }
        }
    };
}
