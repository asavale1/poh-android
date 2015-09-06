package pollsofhumanity.hardikar.com.pollsofhumanity.server;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rahul on 06-09-2015.
 */
public class GetQuestion extends AsyncTask<Void, Void, QuestionHolder> {
    private String getQuestionUrl;

    public GetQuestion(Context context) {
        this.getQuestionUrl = "http://polls-of-humanity.herokuapp.com/api/get_question";
    }

    @Override
    protected QuestionHolder doInBackground(Void... Params) {
        System.out.println("Over here");
        StringBuilder data = new StringBuilder();
        try {
            URL url = new URL(getQuestionUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            System.out.println("Over here now");

            InputStream in = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            System.out.println("Over here there");

            String line;
            while ((line = reader.readLine()) != null) {
                data.append(line);
                System.out.println("Over line: " + line);

            }

            connection.disconnect();
            in.close();
            reader.close();
            System.out.println("Over data: " + data.toString());

        } catch (Exception e) {
            System.out.println("IN EXCEPTION");
            e.printStackTrace();
        }
        System.out.println("ENDING");
        System.out.println("total " + data.toString());

        return null;
    }
}

