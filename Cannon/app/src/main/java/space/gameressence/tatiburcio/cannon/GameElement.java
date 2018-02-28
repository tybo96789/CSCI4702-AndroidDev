package space.gameressence.tatiburcio.cannon;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by tybo96789 on 2/23/18.
 */

public class GameElement {
    CannonView view;
    Paint mPaint = new Paint();
    Rect shape;
    private float velocityY;
    private int soundID;

    public GameElement(CannonView view,int color, int soundID, int x, int y, int width, int length, float velocityY) {
        this.view = view;
        this.velocityY = velocityY;
        this.soundID = soundID;
        this.mPaint.setColor(color);
        this.shape = new Rect(x,y,x + width, y + length);

    }

    public void update(double interval)
    {
        //update vertical pos
        this.shape.offset(0, (int) (this.velocityY * interval));

        //if this gameElement collides with the wall, reverse the direction
        if(this.shape.top < 0 && velocityY < 0 || this.shape.bottom > this.view.getScreenHeight() && this.velocityY > 0)
            this.velocityY *= -1;
    }

    public void draw(Canvas canvas)
    {
        canvas.drawRect(this.shape,this.mPaint);
    }

    public void playSound()
    {
        view.playSound(soundID);
    }
}
