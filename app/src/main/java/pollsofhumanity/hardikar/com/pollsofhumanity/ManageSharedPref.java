package pollsofhumanity.hardikar.com.pollsofhumanity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ameya on 9/6/15.
 */
public class ManageSharedPref {
    SharedPreferences sharedPref;

    public ManageSharedPref(Context context){
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean getIsQuestionAnswered(){
        return sharedPref.getBoolean("answered", false);
    }

    public void setIsQuestionAnswered(boolean answered){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("answered", answered);
        editor.commit();
    }

    public String getQuestion(){
        return sharedPref.getString("question", "Waiting for question");
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

    public int getId(){
        return sharedPref.getInt("id", -1);
    }

    public void setId(int id){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("id", id);
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
