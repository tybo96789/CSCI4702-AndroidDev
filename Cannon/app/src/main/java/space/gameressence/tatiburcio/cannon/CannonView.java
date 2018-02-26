package space.gameressence.tatiburcio.cannon;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by tybo96789 on 2/23/18.
 */

public class CannonView extends SurfaceView implements SurfaceHolder.Callback{

//    public CannonView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }


         static final String TAG = "CannonView";

         //Constants for game play
         static final int MISS_PENALTY = 2,
                 HIT_REWARD = 3;

         //Constants for the cannon
        static final double CANNON_BASE_RADIUS_PERCENT = 3.0 / 40,
                CANNON_BARREL_WIDTH_PERCENT = 3.0 / 40,
                CANNON_BARREL_LENGTH_PERCENT = 1.0 / 10;

        //Constants for the CannonBall
        static final double CANNONBALL_RADIUS_PERCENT = 3.0 / 80,
                CANNONBALL_SPEED_PERCENT = 3.0 / 2;

        //Constants for the Targets
        static final double TARGET_WIDTH_PERCENT = 1.0 / 4,
                TARGET_LENGTH_PERCENT = 3.0 / 20,
                TARGET_FIRST_X_PERCENT = 3.0 / 5,
                TARGET_SPACING_PERCENT = 1.0 / 60,
                TARGET_PIECES = 9,
                TARGET_MIN_SPEED_PERCENT = 3.0 / 4,
                TARGET_MAX_SPEED_PERCENT = 6.0 / 4;

        //Constrants for the Blocker
        static final double BLOCKER_WIDTH_PERCENT = 1.0 / 4,
                BLOCKER_LENGHT_PERCENT = 1.0 / 4,
                BLOCKER_X_PERCENT = 1.0 / 2,
                BLOCKER_SPEED_PERCENT = 1.0;

        //Text size 1/18 of the screen width
        static final double TEXT_SIZE_PERCENT = 1.0 / 18;



    private CannonThread mCannonThread;
    private Activity mActivity;
    private boolean dialogIsDisplayed = false;

    //Game Objects
    private Cannon mCannon;
    private Blocker mBlocker;
    private ArrayList<Target> targets;


    //dimension variables
    private int screenWidth, screenHeight;

    //Variables for the game loop and tracking statistics
    private boolean gameOver;
    private double timeLeft;
    private int shotsFired;
    private double totalElaspedTime;

    //Constatns and variables for managing sounds

        static final int TARGET_SOUND_ID = 0,
                CANNON_SOUND_ID = 1,
                BLOCKER_SOUND_ID = 2;

    private SoundPool mSoundPool;
    private SparseIntArray soundMap;

    //paint varibles used when drawing each item on the creen
    private Paint textPaint,
        backgroundPaint;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CannonView(Context context, AttributeSet attrs)
    {
        super(context,attrs);
        this.mActivity = (Activity) context;

        this.getHolder().addCallback(this);

        AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
        attrBuilder.setUsage(AudioAttributes.USAGE_GAME);

        //init sounpool to play the app's three sound effects
        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(1);
        builder.setAudioAttributes(attrBuilder.build());
        mSoundPool = builder.build();





        this.soundMap = new SparseIntArray(3);
        this.soundMap.put(TARGET_SOUND_ID, this.mSoundPool.load(context, R.raw.target_hit,1));
        this.soundMap.put(CANNON_SOUND_ID, this.mSoundPool.load(context, R.raw.cannon_fire, 1));
        this.soundMap.put(BLOCKER_SOUND_ID,this.mSoundPool.load(context, R.raw.blocker_hit,1));

        this.textPaint = new Paint();
        this.backgroundPaint = new Paint();
        this.backgroundPaint.setColor(Color.WHITE);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.screenWidth = w;
        this.screenWidth = h;

        //configure text prperties
        this.textPaint.setTextSize((int) TEXT_SIZE_PERCENT * this.screenHeight);
        this.textPaint.setAntiAlias(true);
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void playSound(int soundID)
    {
        this.mSoundPool.play(this.soundMap.get(soundID),1,1,1,0,1f);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void newGame()
    {
        this.mCannon = new Cannon(this, (int) (CANNON_BASE_RADIUS_PERCENT * this.screenHeight),
                (int) (CANNON_BARREL_LENGTH_PERCENT * this.screenWidth),
                (int)(CANNON_BARREL_WIDTH_PERCENT * this.screenHeight));
        Random random = new Random();
        this.targets = new ArrayList<>();

        //init targetx for the first target from the left
        int targetX = (int) (TARGET_FIRST_X_PERCENT * this.screenWidth);

        //calculate Y coordinates of targets
        int targetY = (int) ((0.5 - TARGET_LENGTH_PERCENT/2) * this.screenHeight);

        //add TARGET_PIECES
        for(int n = 0; n < TARGET_PIECES; n++)
        {
            //determine a random velocity between min and max values for target n
//            double velocity = this.screenHeight * (random.nextDouble() * (TARGET_MAX_SPEED_PERCENT - TARGET_MIN_SPEED_PERCENT) + TARGET_MIN_SPEED_PERCENT);
            double velocity = Double.MAX_VALUE;

            int color = (n % 2 == 0) ? this.getResources().getColor(R.color.dark, this.getContext().getTheme()) : this.getResources().getColor(R.color.light,this.getContext().getTheme());

            velocity *= -1;

            //create and add a new target to the target list
            targets.add(new Target(this, color, HIT_REWARD, targetX, targetY, (int) (TARGET_WIDTH_PERCENT * this.screenWidth),(int) (TARGET_LENGTH_PERCENT * this.screenHeight), (int) velocity));


            //increase the x coordinate to the position the next target more to the right
            targetX += (TARGET_WIDTH_PERCENT + TARGET_SPACING_PERCENT) * this.screenWidth;

        }

        //create a new blocker
        this.mBlocker = new Blocker(this,Color.BLACK, MISS_PENALTY, (int) (BLOCKER_X_PERCENT * this.screenWidth), (int) ((0.5-BLOCKER_LENGHT_PERCENT/2)* this.screenHeight), (int) (BLOCKER_WIDTH_PERCENT * this.screenWidth), (int) (BLOCKER_LENGHT_PERCENT * this.screenHeight), (float) (BLOCKER_SPEED_PERCENT * this.screenHeight));

        this.timeLeft = 10;

        this.shotsFired = 0;

        this.totalElaspedTime = 0.0;

        if(this.gameOver)
        {
            this.gameOver = false;
            mCannonThread = new CannonThread(this.getHolder());
            this.mCannonThread.start();
        }

        this.hideSystemBars();

    }
    //TODO 6.13.7
}
