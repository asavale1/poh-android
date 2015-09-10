package pollsofhumanity.hardikar.com.pollsofhumanity.server;

/**
 * Created by ameya on 9/7/15.
 */
public class ResultsHolder {
    private int yesCount;
    private int noCount;
    private int total;

    public ResultsHolder(){}

    public void setYesCount(int yesCount){ this.yesCount = yesCount; }
    public int getYesCount(){ return this.yesCount; }

    public void setNoCount(int noCount){ this.noCount = noCount; }
    public int getNoCount(){ return this.noCount; }

    public void setTotal(int total){ this.total = total; }
    public int getTotal(){ return this.total; }

}
