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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.iid.FirebaseInstanceId;

import java.net.HttpURLConnection;
import java.util.Calendar;
import java.util.TimeZone;

import co.digitaldavinci.pollsofhumanity.server.GetQuestion;
import co.digitaldavinci.pollsofhumanity.server.PostAnswer;
import co.digitaldavinci.pollsofhumanity.server.RequestQuestion;
import co.digitaldavinci.pollsofhumanity.server.holder.QuestionHolder;
import co.digitaldavinci.pollsofhumanity.server.listener.GetQuestionListener;
import co.digitaldavinci.pollsofhumanity.server.listener.PostAnswerListener;
import co.digitaldavinci.pollsofhumanity.server.listener.QuestionApiListener;

/**
 * Created by ameya on 10/25/15.
 */
public class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";

    private ManageSharedPref manageSharedPref;
    private TextView question, timeTill;
    private Button noButton, yesButton;
    private Dialog loadingDialog;
    private Dialog enterQuestionDialog;
    private ImageButton submitQuestionRequest;

    private EditText requestedQuestion;
    private TextView requestQuestion;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            calculateTimeTill();
        }
    };


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        manageSharedPref = new ManageSharedPref(getActivity().getApplicationContext());
        ((BaseActivity) getActivity()).setActionBarButtonToChart();


        MobileAds.initialize(getActivity().getApplicationContext(), getString(R.string.app_id));


        String token = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "Token: " + token);
        Log.i(TAG, "ID: " + FirebaseInstanceId.getInstance().getId());

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

        requestQuestion = (TextView) view.findViewById(R.id.question_request);
        requestQuestion.setTypeface(font);
        requestQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterQuestionDialog.show();
            }
        });
        setupEnterQuestionDialog();


        submitQuestionRequest = (ImageButton) view.findViewById(R.id.submit_question);
        submitQuestionRequest.setOnClickListener(submitQuestionRequestListener);

        timeTill = (TextView) view.findViewById(R.id.time_till);
        timeTill.setTypeface(font);

        if(manageSharedPref.getUpdate() || manageSharedPref.getCurrentQuestion().isEmpty()){

            Log.i(TAG, Integer.toString(manageSharedPref.getCurrentQuestionId()));
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

    private void setupEnterQuestionDialog(){
        enterQuestionDialog = new Dialog(getActivity());
        enterQuestionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        enterQuestionDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        enterQuestionDialog.setContentView(R.layout.dialog_enter_question);

        requestedQuestion = ((EditText) enterQuestionDialog.findViewById(R.id.requested_question));

        if(!requestQuestion.getText().equals(getString(R.string.question_request_hint))){
            requestedQuestion.setText(requestQuestion.getText());
        }

        ((Button) enterQuestionDialog.findViewById(R.id.accept)).setText(Html.fromHtml("\u2713"));
        enterQuestionDialog.findViewById(R.id.accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = requestedQuestion.getText().toString().trim();
                if(!question.isEmpty() && !question.equals(getString(R.string.question_request_hint))){
                    requestQuestion.setText(question);
                    submitQuestionRequest.setVisibility(View.VISIBLE);
                }else{
                    requestQuestion.setText(getString(R.string.question_request_hint));
                    submitQuestionRequest.setVisibility(View.INVISIBLE);
                }

                enterQuestionDialog.dismiss();
            }
        });


        ((Button) enterQuestionDialog.findViewById(R.id.decline)).setText(Html.fromHtml("\u2715"));
        enterQuestionDialog.findViewById(R.id.decline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!requestQuestion.getText().equals(getString(R.string.question_request_hint))){
                    requestedQuestion.setText(requestQuestion.getText());
                }
                enterQuestionDialog.dismiss();
            }
        });

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

    private QuestionApiListener listener = new QuestionApiListener() {
        @Override
        public void onRequestQuestionComplete(int status) {
            if(status == HttpURLConnection.HTTP_OK){
                requestedQuestion.setText("");
                requestQuestion.setText(getString(R.string.question_request_hint));
                submitQuestionRequest.setVisibility(View.INVISIBLE);
                loadingDialog.dismiss();
                Toast.makeText(getActivity(), "Question submitted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), "Submission failed, please try again", Toast.LENGTH_SHORT).show();
            }
        }
    };

    View.OnClickListener submitQuestionRequestListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String question = requestQuestion.getText().toString().trim();
            if(!question.equals(getString(R.string.question_request_hint)) && !question.isEmpty()){
                ((TextView) loadingDialog.findViewById(R.id.action)).setText("Submitting question");
                loadingDialog.show();
                new RequestQuestion(getActivity(), listener, question).execute();

            }else {
                Toast.makeText(getActivity(), "Please enter a question", Toast.LENGTH_SHORT).show();
            }
        }
    };

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


            if(mQH.getQuestion() != null){

                if(manageSharedPref.getCurrentQuestionId() != mQH.getId()){
                    question.setText(mQH.getQuestion());
                    manageSharedPref.setUpdate(false);
                    manageSharedPref.setCurrentQuestion(mQH.getQuestion());
                    manageSharedPref.setCurrentQuestionId(mQH.getId());
                    manageSharedPref.setCurrentQuestionAnswered(false);


                }else{
                    question.setText(manageSharedPref.getCurrentQuestion());
                }

                yesButton.setVisibility(View.VISIBLE);
                noButton.setVisibility(View.VISIBLE);

            }else{
                question.setText("Please wait for the next question");
                yesButton.setVisibility(View.INVISIBLE);
                noButton.setVisibility(View.INVISIBLE);
            }


            loadingDialog.cancel();
        }
    };


}
