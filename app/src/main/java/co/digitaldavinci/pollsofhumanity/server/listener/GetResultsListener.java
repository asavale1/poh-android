package co.digitaldavinci.pollsofhumanity.server.listener;

import co.digitaldavinci.pollsofhumanity.server.holder.ResultsHolder;

/**
 * Created by ameya on 9/7/15.
 */
public interface GetResultsListener {
    void onGetResultsComplete(ResultsHolder results);
}
