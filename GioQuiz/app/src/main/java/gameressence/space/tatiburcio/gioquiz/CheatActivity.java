package gameressence.space.tatiburcio.gioquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;



/**
 * Cheating Activity of the GeoQuiz program. Contains methods to let the main activity know what was the state of the
 * 	Activity and change the program flow accordingly
 * @author Tyler Atiburcio
 * Date: 1/26/18
 */

public class CheatActivity extends AppCompatActivity {
	
	
	//Static Variables
	//Keys
    private static final String EXTRA_ANSWER_IS_TRUE = "gameressence.space.tatiburcio.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "gameressence.space.tatiburcio.geoquiz.answer_shown";
	private static final String CHEATING_INDEX = "cheater";
	
	//Cheating Activity variables
    private boolean mAnswerIsTrue, mAnswerShown = false;
    private TextView mAnswerTExtView, mBuildVersionView;
    private Button mShowAnswer;

    
	/**
	 * @return Intent; to be used to link together Main activity and cheating activity
	 */
    public static Intent newIntent(Context packageContext, boolean answerIsTrue)
    {
        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE,answerIsTrue);
        return i;
    }
	
	/**
	 * @return boolean; whether or not the user has revealed the answer to the question 
	 */
    public static boolean wasAnswerShown(Intent result)
    {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN,false);
    }
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

		
		//This will restore the state of the cheating activity if the user decides to rotate the display
        if (savedInstanceState != null) {
            this.mAnswerShown = savedInstanceState.getBoolean(CHEATING_INDEX, false);
            this.setAnswerShownResult(this.mAnswerShown);
        }
		//Determine if the question's answers is true or false
        this.mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false);
		
        this.mAnswerTExtView = (TextView) findViewById(R.id.answer_text_view);
        this.mBuildVersionView = (TextView) findViewById(R.id.build_version);
        //Shows build number
        this.mBuildVersionView.setText("Build Version " + Build.VERSION.SDK_INT);
        this.mShowAnswer = (Button) findViewById(R.id.show_answer_button);
        this.mShowAnswer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mAnswerShown = true;
                if(mAnswerIsTrue)
                    mAnswerTExtView.setText(R.string.true_button);
                else
                    mAnswerTExtView.setText(R.string.false_button);
                setAnswerShownResult(true);
            }
        });

        //Begin Chapter 6
		//API compatibility error  
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            int cx = mShowAnswer.getWidth() / 2;
            int cy = mShowAnswer.getHeight() / 2;
            float radius = mShowAnswer.getWidth();
            Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswer, cx, cy, radius, 0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mAnswerTExtView.setVisibility(View.VISIBLE);
                    mShowAnswer.setVisibility(View.INVISIBLE);
                }
            });
            anim.start();
        } else {
            //THIS ANIMATION CAUSES RAGE.... Does not well on emulation...

            //mAnswerTExtView.setVisibility(View.VISIBLE);
            //mShowAnswer.setVisibility(View.INVISIBLE);
        }
        //End Chapter 6
    }

	
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
		//Saves the state of the isAnswerShown boolean so when instance is restored the boolean state is correctly restored
        savedInstanceState.putBoolean(CHEATING_INDEX,this.mAnswerShown);
    }

	/**
	 * Lets the main activity know that the user has viewed the answer and is now considered as a cheater
	 */
    private void setAnswerShownResult(boolean isAnswerShown)
    {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK,data);
    }

}
