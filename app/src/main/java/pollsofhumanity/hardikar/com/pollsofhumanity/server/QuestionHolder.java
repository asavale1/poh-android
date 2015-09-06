package pollsofhumanity.hardikar.com.pollsofhumanity.server;

/**
 * Created by rahul on 06-09-2015.
 */
public class QuestionHolder {
    private int id;
    private String question;
    public void setId(int id)
    {
        this.id=id;
    }
    public int getId()
    {
        return this.id;
    }

    public void setQuestion(String question)
    {
        this.question=question;
    }
    public String getQuestion()
    {
        return question;
    }
}
