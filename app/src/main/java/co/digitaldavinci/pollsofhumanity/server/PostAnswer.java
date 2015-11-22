package co.digitaldavinci.pollsofhumanity.server;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import co.digitaldavinci.pollsofhumanity.server.listener.PostAnswerListener;

/**
 *
 */
public class PostAnswer extends AsyncTask<Void, Void, String> {
    private int question_id;
    private String answer, postAnswerUrl;
    private PostAnswerListener listener;

    public PostAnswer(int question_id, String answer, PostAnswerListener listener){
        this.postAnswerUrl="https://polls-of-humanity.herokuapp.com/api/post_answer";
        this.question_id=question_id;
        this.answer=answer;
        this.listener = listener;

    }
    @Override
    protected String doInBackground(Void... params) {
        String result = null;
        try {
            URL url = new URL(postAnswerUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

            String urlParameters = "question_id="+question_id;
            urlParameters += "&answer="+answer;

            writer.write(urlParameters);
            writer.flush();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream stream = connection.getInputStream();
                BufferedReader r = new BufferedReader(new InputStreamReader(stream));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                result = total.toString();
                stream.close();
            }
            writer.close();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(result);

        return result;
    }

    @Override
    protected void onPostExecute(String result){
        listener.onPostAnswerComplete();
    }
}
