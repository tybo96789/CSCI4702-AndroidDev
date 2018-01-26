package gameressence.space.tatiburcio.gioquiz;

/**
 * Part of the GeoQuiz Program
 * Created by tyleratiburcio on 1/19/18.
 *
 * This class contains the action that pertain to the question aspect to the GeoQuiz Program
 */

public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;

    public Question(int textResId, boolean answerTrue)
    {
        this.mTextResId = textResId;
        this.mAnswerTrue = answerTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }
}
