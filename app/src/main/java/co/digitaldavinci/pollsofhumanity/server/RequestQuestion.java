package co.digitaldavinci.pollsofhumanity.server;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import co.digitaldavinci.pollsofhumanity.R;

/**
 * Created by ameya on 10/27/16.
 */

public class RequestQuestion extends AsyncTask<Void, Void, String> {

    private Context context;
    private String question;

    public RequestQuestion(Context context, String question){
        this.context = context;
        this.question = question;
    }

    @Override
    protected String doInBackground(Void... params) {

        OutputStreamWriter writer = null;

        try{
            URL url = new URL(context.getString(R.string.request_question_api_endpoint));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();

            JSONObject questionRequest = new JSONObject();
            questionRequest.put("question", question);
            writer = new OutputStreamWriter(connection.getOutputStream());

            writer.write(questionRequest.toString());
            writer.flush();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("REQUEST QUESTION: GOT DETAILS");
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(writer != null){
                try{ writer.close(); }catch(Exception e){}
            }
        }


        return null;
    }
}
