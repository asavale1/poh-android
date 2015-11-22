package co.digitaldavinci.pollsofhumanity.server;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import co.digitaldavinci.pollsofhumanity.R;
import co.digitaldavinci.pollsofhumanity.server.holder.QuestionHolder;
import co.digitaldavinci.pollsofhumanity.server.listener.GetQuestionListener;

/**
 *
 */
public class GetQuestion extends AsyncTask<Void, Void, QuestionHolder> {
    private String getQuestionUrl;
    private GetQuestionListener listener;

    public GetQuestion(Context context, GetQuestionListener listener) {
        this.getQuestionUrl = context.getString(R.string.get_question_url);
        this.listener = listener;
    }

    @Override
    protected QuestionHolder doInBackground(Void... Params) {
        StringBuilder data = new StringBuilder();
        try {
            URL url = new URL(getQuestionUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            InputStream in = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }

            connection.disconnect();
            in.close();
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        QuestionHolder question = new QuestionHolder();
        try {
            JSONObject questionDetails = new JSONObject(data.toString());
            question.setId(questionDetails.getInt("id"));
            question.setQuestion(questionDetails.getString("question"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return question;
    }

    @Override
    protected void onPostExecute(QuestionHolder question){
        super.onPostExecute(question);
        listener.onGetQuestionComplete(question);
    }
}

