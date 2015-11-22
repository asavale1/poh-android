package co.digitaldavinci.pollsofhumanity.server.listener;

import co.digitaldavinci.pollsofhumanity.server.holder.QuestionHolder;

/**
 * Created by ameya on 9/6/15.
 */
public interface GetQuestionListener {
    void onGetQuestionComplete(QuestionHolder question);
}
