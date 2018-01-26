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

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "gameressence.space.tatiburcio.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "gameressence.space.tatiburcio.geoquiz.answer_shown";
    private boolean mAnswerIsTrue, mAnswerShown = false;
    private TextView mAnswerTExtView, mBuildVersionView;
    private Button mShowAnswer;

    private static final String CHEATING_INDEX = "cheater";

    public static Intent newIntent(Context packageContext, boolean answerIsTrue)
    {
        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE,answerIsTrue);
        return i;
    }

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

        if (savedInstanceState != null) {
            this.mAnswerShown = savedInstanceState.getBoolean(CHEATING_INDEX, false);
            this.setAnswerShownResult(this.mAnswerShown);
        }

        this.mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false);

        this.mAnswerTExtView = (TextView) findViewById(R.id.answer_text_view);
        this.mBuildVersionView = (TextView) findViewById(R.id.build_version);
        this.mBuildVersionView.setText("Build Version " + Build.VERSION.SDK_INT);
        this.mShowAnswer = (Button) findViewById(R.id.show_answer_button);
        this.mShowAnswer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mAnswerShown = true;
               // mAnswerIsTrue ? mAnswerTExtView.setText(R.string.true_button) : mAnswerTExtView.setText(R.string.false_button);
                if(mAnswerIsTrue)
                    mAnswerTExtView.setText(R.string.true_button);
                else
                    mAnswerTExtView.setText(R.string.false_button);
                setAnswerShownResult(true);
            }
        });

        //Begin Chapter 6
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
            //THIS ANIMATION CAUSES RAGE....

            //mAnswerTExtView.setVisibility(View.VISIBLE);
            //mShowAnswer.setVisibility(View.INVISIBLE);
        }
        //End Chapter 6

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "HELLO WORLD!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }


        }
        );
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(CHEATING_INDEX,this.mAnswerShown);
    }


    private void setAnswerShownResult(boolean isAnswerShown)
    {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK,data);
    }

}
