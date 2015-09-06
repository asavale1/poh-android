package pollsofhumanity.hardikar.com.pollsofhumanity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pollsofhumanity.hardikar.com.pollsofhumanity.server.GetQuestion;
import pollsofhumanity.hardikar.com.pollsofhumanity.server.GetQuestionListener;
import pollsofhumanity.hardikar.com.pollsofhumanity.server.PostAnswer;
import pollsofhumanity.hardikar.com.pollsofhumanity.server.QuestionHolder;

public class BaseActivity extends AppCompatActivity {
    private TextView questionText;
    private QuestionHolder questionHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        new GetQuestion(this, gQListener).execute();
        Button yesButton = (Button)findViewById(R.id.butt_Yes);
        yesButton.setOnClickListener(yesListener);

        Button noButton= (Button)findViewById(R.id.butt_No);
        noButton.setOnClickListener(noListener);
        questionText=(TextView) findViewById(R.id.question_Text);
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
            questionText.setText(question.getQuestion());
            questionHolder = question;
        }
    };
    View.OnClickListener yesListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new PostAnswer(questionHolder.getId(), "yes").execute();
        }
    };
     View.OnClickListener noListener= new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            new PostAnswer(questionHolder.getId(), "no").execute();
         }
     };

}
