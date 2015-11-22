package co.digitaldavinci.pollsofhumanity.server.holder;

/**
 *
 */
public class ResultsHolder {
    private int yesCount;
    private int noCount;
    private int total;
    private String question;

    public ResultsHolder(){}

    public void setYesCount(int yesCount){ this.yesCount = yesCount; }
    public int getYesCount(){ return this.yesCount; }

    public void setNoCount(int noCount){ this.noCount = noCount; }
    public int getNoCount(){ return this.noCount; }

    public void setTotal(int total){ this.total = total; }
    public int getTotal(){ return this.total; }

    public void setQuestion(String question){ this.question = question; }
    public String getQuestion(){ return this.question; }

}
