package space.gameressence.tatiburcio.cannon;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

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
        static final double BLOCKER_WIDTH_PERCENT = 1.0 / 16,
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
        this.screenHeight = h;

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
        System.out.println(this.screenHeight);
        System.out.println(this.screenWidth);
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
            double velocity = this.screenHeight * (random.nextDouble() * (TARGET_MAX_SPEED_PERCENT - TARGET_MIN_SPEED_PERCENT) + TARGET_MIN_SPEED_PERCENT);
//            double velocity = Double.MAX_VALUE;
//             double velocity = 0.1;

//            int color = (n % 2 == 0) ? this.getResources().getColor(R.color.dark, this.getContext().getTheme()) : this.getResources().getColor(R.color.light,this.getContext().getTheme());
            //TODO color fix...
//            int color = 0x1976D2;

            velocity *= -1;

            //create and add a new target to the target list
            targets.add(new Target(this, Color.BLACK, HIT_REWARD, targetX, targetY, (int) (TARGET_LENGTH_PERCENT * this.screenHeight),(int) (TARGET_WIDTH_PERCENT * this.screenWidth), (int) velocity));


            //increase the x coordinate to the position the next target more to the right
            targetX += (TARGET_WIDTH_PERCENT + TARGET_SPACING_PERCENT) * this.screenWidth;


            System.out.println("Target " + n + " spacing " + targetX);
        }

        //create a new blocker
//        this.mBlocker = new Blocker(this,Color.BLACK, MISS_PENALTY, (int) (BLOCKER_X_PERCENT * this.screenWidth), (int) ((0.5-BLOCKER_LENGHT_PERCENT/2)* this.screenHeight), (int) (BLOCKER_WIDTH_PERCENT * this.screenWidth), (int) (BLOCKER_LENGHT_PERCENT * this.screenHeight), (float) (BLOCKER_SPEED_PERCENT * this.screenHeight));

        this.mBlocker = new Blocker(this,Color.BLACK, MISS_PENALTY, (int) (BLOCKER_X_PERCENT * this.screenWidth), (int) ((0.5-(BLOCKER_LENGHT_PERCENT/2)* this.screenHeight)), (int) (BLOCKER_WIDTH_PERCENT * this.screenWidth), (int) (BLOCKER_LENGHT_PERCENT * this.screenHeight), (float) (BLOCKER_SPEED_PERCENT * this.screenHeight));


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

    public void updatePositions(double elaspedTimeMS)
    {
        double interval = elaspedTimeMS / 1000.0;

        //update cannon ball's pos if it is on the screen
        if(mCannon.getCannonBall() != null)
            mCannon.getCannonBall().update(interval);

        mBlocker.update(interval); // update the blocker pos

        for(GameElement target: targets)
        {
            target.update(interval);
        }

        timeLeft -= interval; // subtract from the time left

        //if the time reached zero
        if(timeLeft <= 0)
        {
            timeLeft =0.0;
            gameOver = true;
            mCannonThread.setRunning(false);
            showGameOverDialog(R.string.lose);
        }

        //if all pieces have been hit
        if(targets.isEmpty())
        {
            mCannonThread.setRunning(false);
            showGameOverDialog(R.string.win);
            gameOver = true;
        }

    }

    //aligns the barrels and fires a cannon ball if the cannonball is not already on the screen
    public void alignAndFireCannonBall(MotionEvent event)
    {
        //get the loc of the touch in this view
        Point touchPoint = new Point((int) event.getX(), (int) event.getY());

        //compute the touch distance from center of the screen on the y-axis
        double centerMinusY = (screenHeight/2 - touchPoint.y);

        double angle = 0;

        //calculate the angle of the barrel makes with the horizontal

        angle = Math.atan2(touchPoint.x,centerMinusY);

        //point the barrel at the point where the screen was touched
        mCannon.align(angle);

        //fire cannonball if there is not alrady a cannonball on screen
        if(mCannon.getCannonBall() == null || !mCannon.getCannonBall().isOnScreen())
        {
            mCannon.fireCannonBall();
            ++shotsFired;
        }
    }


    @SuppressLint("ValidFragment")
    //display an alterdialog when the game ends
    private void showGameOverDialog(final int messageID)
    {
        final DialogFragment gameResult = new DialogFragment()
        {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getResources().getString(messageID));
                //displays the number of shots fired and total time elasped
                builder.setMessage(getResources().getString(R.string.results_format,shotsFired,totalElaspedTime));
                builder.setPositiveButton(R.string.reset_game, new DialogInterface.OnClickListener()
                {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogIsDisplayed = false;
                        newGame();
                    }
                    });
                return builder.create();
            }
        };

        //in GUI thread, use fragmentmanager to display the dialog fragment

        mActivity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        showSystemBars();
                        dialogIsDisplayed = true;
                        gameResult.setCancelable(false);
                        gameResult.show(mActivity.getFragmentManager(), "results");
                    }
                }
        );
    }

    //draws the game to the given canvas
    public void drawGameElements(Canvas canvas)
    {
        //clear the background
        canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(),backgroundPaint);

        //display time remaining
        canvas.drawText(getResources().getString(R.string.time_remaining_format,timeLeft),100,150,textPaint);
        System.out.println(this.timeLeft);
        mCannon.draw(canvas);

        //draw the gameelements
        if(mCannon.getCannonBall() != null && mCannon.getCannonBall().isOnScreen())
            mCannon.getCannonBall().draw(canvas);

        mBlocker.draw(canvas);

        //System.out.println("Number of targets is:" + this.targets.size());
        //draw all of the targets
        for(GameElement target : targets)
            target.draw(canvas);
    }

    //check if the ball collides with the blocker or any of the targets and handles the collisions
    public void testForCollision()
    {
        //removes any targets that teh cannonball collides with
        if(mCannon.getCannonBall() != null && mCannon.getCannonBall().isOnScreen())
        {
            for(int n = 0; n < targets.size(); n++)
            {
                if(mCannon.getCannonBall().collidesWith(targets.get(n)))
                {
                    targets.get(n).playSound();
                    //add hit rewards time to remaining time
                    timeLeft += targets.get(n).getHitReward();

                    mCannon.removeCannonBall(); // removes cannonball from game
                    targets.remove(n);  //removes the target that is hit
                    --n;//ensure that we dont skip testing new target n
                    break;
                }
            }
        }
        else
            mCannon.removeCannonBall(); //remove the cannonball if it should not be on screen

        //checks if the ball collides with the blocker
        if(mCannon.getCannonBall() != null && mCannon.getCannonBall().collidesWith(mBlocker))
        {
            mBlocker.playSound();

            //reverse ball direction
            mCannon.getCannonBall().reverseVelocityX();

            //dedust blocker miss pentalty from the time remaining
            timeLeft -= mBlocker.getMissPenalty();
        }
    }

    //stops the game called by cannongame fragment on pause method
    public void stopGame()
    {
        if(mCannonThread != null)
            mCannonThread.setRunning(false);
    }

    //release resources: called by cannon game's on destory method
    public void releaseResoruces()
    {
        mSoundPool.release();
        mSoundPool = null;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(!dialogIsDisplayed)
        {
            newGame();
            mCannonThread = new CannonThread(holder);
            mCannonThread.setRunning(true);
            mCannonThread.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        mCannonThread.setRunning(false);

        while(retry)
        {
            try{
                mCannonThread.join();
                retry = false;
            }
            catch(InterruptedException e)
            {
                Log.e(TAG,"Thread Sad",e);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //get the int represetning the type of action which caused this event
        int action = event.getAction();

        //the user touched the screen or dragged along the screen
        if(action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE)
        {
            alignAndFireCannonBall(event);
        }
        return true;
    }

    private class CannonThread extends Thread
    {
        //for manipulating canvas
        private SurfaceHolder mSurfaceHolder;
        private boolean threadIsRunning = true; //running by default

        public CannonThread(SurfaceHolder surfaceHolder) {
            mSurfaceHolder = surfaceHolder;
            this.setName("CannonThread");
        }

        public void setRunning(boolean running)
        {
            this.threadIsRunning = running;
        }

        @Override
        public void run() {
            Canvas canvas = null; //Used for drawing

            long previousFrameTime = System.currentTimeMillis();

            while(threadIsRunning)
            {
                try
                {
                    //get canvas for exclusive drawing from this thread
                    canvas = mSurfaceHolder.lockCanvas(null);

                    //lock the surfaceholder for drawing
                    synchronized (mSurfaceHolder)
                    {
                        long currentTime = System.currentTimeMillis();

                        double elaspedTimeMS = currentTime - previousFrameTime;

                        totalElaspedTime += elaspedTimeMS / 1000.0;

                        updatePositions(elaspedTimeMS);// update game state
                        testForCollision(); //test for gameement collisions
                        drawGameElements(canvas);
                        previousFrameTime = currentTime;


                    }
                }
                finally {
                    //display  canvas contents on the cannon view and enable other thread to use the canvas
                    if(canvas != null)
                        mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }


    //hides system bars and app bar
    private void hideSystemBars()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
            View.SYSTEM_UI_FLAG_FULLSCREEN|
            View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    //shows system bars and app bar
    private void showSystemBars()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
