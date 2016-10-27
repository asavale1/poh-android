package co.digitaldavinci.pollsofhumanity;

import android.app.Dialog;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Calendar;
import java.util.TimeZone;

import co.digitaldavinci.pollsofhumanity.server.GetQuestion;
import co.digitaldavinci.pollsofhumanity.server.PostAnswer;
import co.digitaldavinci.pollsofhumanity.server.holder.QuestionHolder;
import co.digitaldavinci.pollsofhumanity.server.listener.GetQuestionListener;
import co.digitaldavinci.pollsofhumanity.server.listener.PostAnswerListener;

/**
 * Created by ameya on 10/25/15.
 */
public class BaseFragment extends Fragment {
    private ManageSharedPref manageSharedPref;
    private TextView question, timeTill;
    private Button noButton, yesButton;
    private EditText questionRequest;
    private Dialog loadingDialog;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            calculateTimeTill();
        }
    };


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        manageSharedPref = new ManageSharedPref(getActivity().getApplicationContext());
        ((BaseActivity) getActivity()).setActionBarButtonToChart();

        loadingDialog = new Dialog(getActivity());
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        View view = inflater.inflate(R.layout.fragment_base, container, false);

        yesButton = (Button) view.findViewById(R.id.button_yes);
        yesButton.setOnClickListener(yesButtonListener);
        yesButton.setText(Html.fromHtml("\u2713"));

        noButton = (Button) view.findViewById(R.id.button_no);
        noButton.setOnClickListener(noButtonListener);
        noButton.setText(Html.fromHtml("\u2715"));

        if(manageSharedPref.getCurrentQuestionAnswered()){
            if(manageSharedPref.getCurrentQuestionAnswer()){
                noButton.setVisibility(View.INVISIBLE);
            }else{
                yesButton.setVisibility(View.INVISIBLE);
            }
        }

        question = (TextView) view.findViewById(R.id.question_Text);
        final Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-CondLight.ttf");
        question.setTypeface(font);

        questionRequest = (EditText) view.findViewById(R.id.question_request);
        questionRequest.setHintTextColor(getResources().getColor(R.color.green_2));
        questionRequest.setTypeface(font);
        questionRequest.setCursorVisible(false);

        questionRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionRequest.setCursorVisible(true);
            }
        });

        final ImageButton submitQuestion = (ImageButton) view.findViewById(R.id.submit_question);

        questionRequest.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                String question = questionRequest.getText().toString().trim();
                if(!question.isEmpty()){
                    submitQuestion.setVisibility(View.VISIBLE);
                }else{
                    submitQuestion.setVisibility(View.GONE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        timeTill = (TextView) view.findViewById(R.id.time_till);
        timeTill.setTypeface(font);

        if(manageSharedPref.getUpdate() || manageSharedPref.getCurrentQuestion().isEmpty()){

            manageSharedPref.setResultsId(manageSharedPref.getCurrentQuestionId());
            ((TextView) loadingDialog.findViewById(R.id.action)).setText("Getting question");
            loadingDialog.show();
            new GetQuestion(getActivity(), gQListener).execute();

        }else{

            ((TextView) loadingDialog.findViewById(R.id.action)).setText("Getting question");
            question.setText(manageSharedPref.getCurrentQuestion());

        }

        final Dialog helpDialog = new Dialog(getActivity());
        helpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        helpDialog.setContentView(R.layout.dialog_help);
        Button help = (Button) view.findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView helpText = (TextView) helpDialog.findViewById(R.id.help);
                helpText.setText("Each day you will get a question.\n\n" +
                        "Answer it anonymously with yes or no.\n\n" +
                        "Please be honest with your responses\n\n" +
                        "In 24 hours you will receive a new question\n\n" +
                        "Tap on the bar graph icon on the top left corner, to view the poll results of the previous question.");
                helpText.setTypeface(font);
                helpDialog.show();
            }
        });

        runnable.run();

        return view;
    }


    private void calculateTimeTill(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        long curTime = cal.getTimeInMillis();

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 00);
        long alarmTime = calendar.getTimeInMillis();

        if(curTime > alarmTime){
            double millis = ((alarmTime + (24 * 60 * 60 * 1000)) - curTime);
            int seconds = (int) (millis / 1000.0) % 60 ;
            int minutes = (int) ((millis / (1000.0*60)) % 60);
            int hours   = (int) ((millis / (1000.0*60*60)) % 24);

            //System.out.println("Hour " + hours);
            //System.out.println("Minutes " + minutes);
            //System.out.println("Seconds " + seconds);

            timeTill.setText("Next question in " + hours + " hrs " + minutes + " min");
        }else{
            double millis = (alarmTime  - curTime);
            int seconds = (int) (millis / 1000.0) % 60 ;
            int minutes = (int) ((millis / (1000.0*60)) % 60);
            int hours   = (int) ((millis / (1000.0*60*60)) % 24);
            timeTill.setText("Next question in " + hours + " hrs " + minutes + " min");
        }
        handler.postDelayed(runnable, 1000 * 60);
    }

    View.OnClickListener yesButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(!manageSharedPref.getCurrentQuestionAnswered()){
                new PostAnswer(getActivity(), manageSharedPref.getCurrentQuestionId(), "yes", pAListener).execute();
                manageSharedPref.setCurrentQuestionAnswer(true);
                noButton.setVisibility(View.INVISIBLE);
            }
        }
    };

    View.OnClickListener noButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!manageSharedPref.getCurrentQuestionAnswered()){
                new PostAnswer(getActivity(), manageSharedPref.getCurrentQuestionId(), "no", pAListener).execute();
                manageSharedPref.setCurrentQuestionAnswer(false);
                yesButton.setVisibility(View.INVISIBLE);
            }
        }
    };

    PostAnswerListener pAListener = new PostAnswerListener() {
        @Override
        public void onPostAnswerComplete() {
            manageSharedPref.setCurrentQuestionAnswered(true);
        }
    };

    GetQuestionListener gQListener = new GetQuestionListener() {
        @Override
        public void onGetQuestionComplete(QuestionHolder mQH) {
            question.setText(mQH.getQuestion());
            manageSharedPref.setUpdate(false);
            manageSharedPref.setCurrentQuestion(mQH.getQuestion());
            manageSharedPref.setCurrentQuestionId(mQH.getId());
            manageSharedPref.setCurrentQuestionAnswered(false);

            yesButton.setVisibility(View.VISIBLE);
            noButton.setVisibility(View.VISIBLE);

            loadingDialog.cancel();
        }
    };


}
