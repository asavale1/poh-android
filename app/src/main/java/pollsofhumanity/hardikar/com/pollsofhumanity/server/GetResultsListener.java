package pollsofhumanity.hardikar.com.pollsofhumanity.server;

import pollsofhumanity.hardikar.com.pollsofhumanity.server.holder.ResultsHolder;

/**
 * Created by ameya on 9/7/15.
 */
public interface GetResultsListener {
    void onGetResultsComplete(ResultsHolder results);
}
