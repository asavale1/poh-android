package pollsofhumanity.hardikar.com.pollsofhumanity.server;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import pollsofhumanity.hardikar.com.pollsofhumanity.server.holder.ResultsHolder;
import pollsofhumanity.hardikar.com.pollsofhumanity.server.listener.GetResultsListener;

/**
 *
 */
public class GetResults extends AsyncTask<Void, Void, ResultsHolder> {
    private String getResultsUrl;
    private int questionId;
    private GetResultsListener listener;


    public GetResults(int questionId, GetResultsListener listener){
        this.getResultsUrl = "https://polls-of-humanity.herokuapp.com/api/get_results?question_id=";
        this.questionId = questionId;
        this.listener = listener;
    }
    @Override
    protected ResultsHolder doInBackground(Void... params) {
        StringBuilder data = new StringBuilder();
        try {
            URL url = new URL(getResultsUrl + questionId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            InputStream in = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
            System.out.println(data.toString());
            connection.disconnect();
            in.close();
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        ResultsHolder results = new ResultsHolder();
        try {
            JSONObject resultDetails = new JSONObject(data.toString());
            results.setYesCount(resultDetails.getInt("yes_count"));
            results.setNoCount(resultDetails.getInt("no_count"));
            results.setTotal(resultDetails.getInt("total"));
            results.setQuestion(resultDetails.getString("question"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return results;
    }

    @Override
    protected void onPostExecute(ResultsHolder results){
        super.onPostExecute(results);
        listener.onGetResultsComplete(results);
    }
}
