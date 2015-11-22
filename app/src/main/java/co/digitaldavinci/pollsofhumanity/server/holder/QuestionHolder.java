package co.digitaldavinci.pollsofhumanity.server.holder;

/**
 *
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
