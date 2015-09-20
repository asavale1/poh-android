package pollsofhumanity.hardikar.com.pollsofhumanity.server;

import pollsofhumanity.hardikar.com.pollsofhumanity.server.holder.QuestionHolder;

/**
 * Created by ameya on 9/6/15.
 */
public interface GetQuestionListener {
    void onGetQuestionComplete(QuestionHolder question);
}
