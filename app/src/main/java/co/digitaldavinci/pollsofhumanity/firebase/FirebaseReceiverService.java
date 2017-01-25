package co.digitaldavinci.pollsofhumanity.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import co.digitaldavinci.pollsofhumanity.BaseActivity;
import co.digitaldavinci.pollsofhumanity.ManageSharedPref;
import co.digitaldavinci.pollsofhumanity.R;
import co.digitaldavinci.pollsofhumanity.server.GetQuestion;
import co.digitaldavinci.pollsofhumanity.server.holder.QuestionHolder;
import co.digitaldavinci.pollsofhumanity.server.listener.GetQuestionListener;

/**
 * Created by ameya on 1/17/17.
 */

public class FirebaseReceiverService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseReceiverService";
    private ManageSharedPref manageSharedPref;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder nb;

    private Context context;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        Log.i(TAG, "Message Received");
        context = getBaseContext();
        manageSharedPref = new ManageSharedPref(context);

        mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        nb = new NotificationCompat.Builder(context);
        nb.setContentTitle("New question published");
        nb.setContentText("The results of the old question are ready");
        nb.setAutoCancel(true);
        nb.setSmallIcon(R.drawable.icon_chart);
        nb.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logox100));

        Intent resultIntent = new Intent(context, BaseActivity.class);

        PendingIntent pi = PendingIntent.getActivity(context, 0, resultIntent, 0);
        nb.setContentIntent(pi);

        manageSharedPref.setUpdate(true);
        mNotificationManager.notify(
                0,
                nb.build()
        );

    }

}
