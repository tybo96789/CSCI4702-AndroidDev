package gameressence.space.tatiburcio.gioquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "gameressence.space.tatiburcio.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "gameressence.space.tatiburcio.geoquiz.answer_shown";
    private boolean mAnswerIsTrue;
    private TextView mAnswerTExtView;
    private Button mShowAnswer;

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
        this.mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false);

        this.mAnswerTExtView = (TextView) findViewById(R.id.answer_text_view);
        this.mShowAnswer = (Button) findViewById(R.id.show_answer_button);
        this.mShowAnswer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               // mAnswerIsTrue ? mAnswerTExtView.setText(R.string.true_button) : mAnswerTExtView.setText(R.string.false_button);
                if(mAnswerIsTrue)
                    mAnswerTExtView.setText(R.string.true_button);
                else
                    mAnswerTExtView.setText(R.string.false_button);
                setAnswerShownResult(true);
            }
        });

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


    private void setAnswerShownResult(boolean isAnswerShown)
    {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK,data);
    }

}
