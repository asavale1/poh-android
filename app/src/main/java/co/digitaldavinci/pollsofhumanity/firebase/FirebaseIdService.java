package co.digitaldavinci.pollsofhumanity.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;


/**
 * Created by ameya on 1/18/17.
 */

public class FirebaseIdService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIdService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("users");
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
    }



}
