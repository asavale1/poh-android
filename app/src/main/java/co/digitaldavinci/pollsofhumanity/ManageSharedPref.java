package co.digitaldavinci.pollsofhumanity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 *
 */
public class ManageSharedPref {
    SharedPreferences sharedPref;

    public ManageSharedPref(Context context){
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setUpdate(boolean update){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("update", update);
        editor.commit();
    }

    public boolean getUpdate(){ return sharedPref.getBoolean("update", true); }

    public void setCurrentQuestion(String question){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("question", question);
        editor.commit();
    }

    public String getCurrentQuestion(){
        return sharedPref.getString("question", "Waiting for question");
    }

    public void setCurrentQuestionId(int id){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("question_id", id);
        editor.commit();
    }

    public int getCurrentQuestionId(){ return sharedPref.getInt("question_id", -1); }

    public void setCurrentQuestionAnswer(boolean answer){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("question_answer", answer);
        editor.commit();
    }

    public boolean getCurrentQuestionAnswer(){ return sharedPref.getBoolean("question_answer", false); }

    public void setCurrentQuestionAnswered(boolean answered){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("question_answered", answered);
        editor.commit();
    }

    public boolean getCurrentQuestionAnswered(){
        return sharedPref.getBoolean("question_answered", false);
    }

    public void setQuestion(String question){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("question", question);
        editor.commit();
    }

    public boolean getUpdated(){
        return sharedPref.getBoolean("updated", false);
    }

    public void setUpdated(boolean updated){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("updated", updated);
        editor.commit();
    }


    public void setResultsId(int id){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("results_id", id);
        editor.commit();
    }

    public int getResultsId(){
        return sharedPref.getInt("results_id", -1);
    }

}
