package co.digitaldavinci.pollsofhumanity;

import android.app.Dialog;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import co.digitaldavinci.pollsofhumanity.R;

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
    private TextView question;
    private Button noButton, yesButton;
    private Dialog loadingDialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        manageSharedPref = new ManageSharedPref(getActivity().getApplicationContext());
        ((BaseActivity) getActivity()).setActionBarButtonToResult();
        loadingDialog = new Dialog(getActivity());
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        View view = inflater.inflate(R.layout.fragment_base, container, false);

        yesButton = (Button) view.findViewById(R.id.butt_Yes);
        yesButton.setOnClickListener(yesButtonListener);
        yesButton.setText(Html.fromHtml("\u2713"));

        noButton = (Button) view.findViewById(R.id.butt_No);
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
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-CondLight.ttf");
        question.setTypeface(font);

        if(manageSharedPref.getUpdate() || manageSharedPref.getCurrentQuestion().isEmpty()){

            manageSharedPref.setResultsId(manageSharedPref.getCurrentQuestionId());
            ((TextView) loadingDialog.findViewById(R.id.action)).setText("Getting question");
            loadingDialog.show();
            new GetQuestion(getActivity(), gQListener).execute();

        }else{

            ((TextView) loadingDialog.findViewById(R.id.action)).setText("Getting question");
            question.setText(manageSharedPref.getCurrentQuestion());

        }

        return view;
    }

    View.OnClickListener yesButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(!manageSharedPref.getCurrentQuestionAnswered()){
                new PostAnswer(manageSharedPref.getCurrentQuestionId(), "yes", pAListener).execute();
                manageSharedPref.setCurrentQuestionAnswer(true);
                noButton.setVisibility(View.INVISIBLE);
            }
        }
    };

    View.OnClickListener noButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!manageSharedPref.getCurrentQuestionAnswered()){
                new PostAnswer(manageSharedPref.getCurrentQuestionId(), "no", pAListener).execute();
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
