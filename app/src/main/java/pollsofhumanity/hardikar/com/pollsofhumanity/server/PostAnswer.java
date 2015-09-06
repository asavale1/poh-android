package pollsofhumanity.hardikar.com.pollsofhumanity.server;

import android.content.Context;
import android.os.AsyncTask;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by rahul on 06-09-2015.
 */
public class PostAnswer extends AsyncTask<Void, Void, String> {
    private int question_id;
    private String answer, postAnswerUrl;

    public PostAnswer(Context context, int question_id, String answer){
        this.postAnswerUrl="https://polls-of-humanity.herokuapp.com/api/post_answer";
        this.question_id=question_id;
        this.answer=answer;

    }
    @Override
    protected String doInBackground(Void... params) {
        String result = null;
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(postAnswerUrl);
        try{
            nameValues.add(new BasicNameValuePair("question_id", Integer.toString(question_id)));
            nameValues.add(new BasicNameValuePair("answer", answer));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValues));
            HttpResponse response = client.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
            result = EntityUtils.toString(resEntity);

        }
        catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(result);

        return result;
    }
}
