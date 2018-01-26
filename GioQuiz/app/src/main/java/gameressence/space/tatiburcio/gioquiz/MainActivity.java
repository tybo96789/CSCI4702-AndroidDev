package gameressence.space.tatiburcio.gioquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Main class for the QeoQuiz program
 * @author Tyler Atiburcio
 * date: 1/26/18 
 */
public class MainActivity extends AppCompatActivity {
	
	//Static variables used for keys 
	private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String CHEATING_INDEX = "cheater";
	private final static String CHEAT_FLAG_INDEX = "cheatFlagIndex";
	private final static int REQUEST_CODE_CHEAT = 0;
	
	//Variables that are used to reference the items on the layout
    private Button mTrueButton,mFalseButton,mCheatButton;
    private ImageButton mNextButton, mPrevButton;
    private TextView mQuestionTextView;
	
	//GeoQuiz local variables
    private int mCurrentIndex = 0;
    private Question[] mQuestionBank = new Question[]
            {
                    new Question(R.string.question_oceans,true),
                    new Question(R.string.question_mideast,false),
                    new Question(R.string.question_africa,false),
                    new Question(R.string.question_americas,true),
                    new Question(R.string.question_asia,true),
            };
    private int[] CHEAT_FLAG = new int[mQuestionBank.length];
    private boolean mIsCheater;
	
	/**
	 * This method will update the variables and the state of the Geoquiz program to reflect the actions of user from 
	 * 	cheating activity
	 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode != Activity.RESULT_OK) return;
        if(requestCode == REQUEST_CODE_CHEAT)
        {
            if (data == null) return;
            this.mIsCheater = CheatActivity.wasAnswerShown(data);
            this.CHEAT_FLAG[mCurrentIndex] = -1;
        }
    }
	
	/**
	 * Updates UI to show the current question, according to the mCurrentIndex
	 */
    private void updateQuestion() {
        Log.d(TAG, "Updating question text for question #" + mCurrentIndex);
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }
	
	/**
	 * Checks to see if the user has cheated first, if the has not check to see if the user has answered the question correctly
	 */
    private void checkAnswer(boolean userPressedTrue)
    {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
		//If user is a cheater or if the question has already been cheated on judge the user 
        if(this.mIsCheater || this.CHEAT_FLAG[mCurrentIndex] == -1) {
            messageResId = R.string.judgment_toast;
        }
        else 
		{
            if (userPressedTrue == answerIsTrue) 
			{
                messageResId = R.string.correct_toast;
                this.CHEAT_FLAG[mCurrentIndex]++;
            } else 
			{
                messageResId = R.string.incorrect_toast;
            }
        }
		Toast.makeText(MainActivity.this, messageResId, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG,"onCreate(Bundle) called");
		
		//Restore the state of the program if the user has rotated the display 
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            this.mIsCheater = savedInstanceState.getBoolean(CHEATING_INDEX, false);
            this.CHEAT_FLAG = savedInstanceState.getIntArray(CHEAT_FLAG_INDEX);
        }
        /**
         * Begin chapter 5 cheating mode
         */
        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start CheatActivity
                //Begin chapter 5 stuff
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent i = CheatActivity.newIntent(MainActivity.this,answerIsTrue);

                startActivityForResult(i,REQUEST_CODE_CHEAT);
            }
        });
		//End Chapter 5 cheating stuff 
		
		//Adding listener to text view to update the question when touched 
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });
        updateQuestion();
		
		//start usage of text button arrow 
/*
        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                //int question = mQuestionBank[mCurrentIndex].getTextResId();
                //mQuestionTextView.setText(question);
                updateQuestion();
            }
        });

        mPrevButton = (Button) findViewById(R.id.previous_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentIndex == 0)
                    mCurrentIndex = mQuestionBank.length;
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                //int question = mQuestionBank[mCurrentIndex].getTextResId();
                //mQuestionTextView.setText(question);
                updateQuestion();
            }
        });
*/
		//End usage of the text button arrow 
		
		//Start usage of arrow image button
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });

        mPrevButton = (ImageButton) findViewById(R.id.previous_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentIndex == 0)
                    mCurrentIndex = mQuestionBank.length;
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                //int question = mQuestionBank[mCurrentIndex].getTextResId();
                //mQuestionTextView.setText(question);
                mIsCheater = false;
                updateQuestion();
            }
        });
		//End usage of image arrow button
		
		//Start True false buttons
        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkAnswer(true);
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });
		//End true false button
    }
	
    @Override
    public void onStart()
    {
        super.onStart();
        Log.d(TAG,"onStart() called");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
		
		//Saves the important variables of the geoquiz before the UI is updated after rotating
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(CHEATING_INDEX,mIsCheater);
        savedInstanceState.putIntArray(CHEAT_FLAG_INDEX,this.CHEAT_FLAG);
    }
}
