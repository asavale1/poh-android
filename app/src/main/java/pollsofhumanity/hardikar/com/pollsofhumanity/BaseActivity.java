package pollsofhumanity.hardikar.com.pollsofhumanity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import pollsofhumanity.hardikar.com.pollsofhumanity.server.GetQuestion;
import pollsofhumanity.hardikar.com.pollsofhumanity.server.GetQuestionListener;
import pollsofhumanity.hardikar.com.pollsofhumanity.server.QuestionHolder;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        new GetQuestion(this, gQListener).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    GetQuestionListener gQListener = new GetQuestionListener() {
        @Override
        public void onGetQUestionComplete(QuestionHolder question) {
            System.out.println("Got question");
            System.out.println(question.getQuestion());
        }
    };
}
