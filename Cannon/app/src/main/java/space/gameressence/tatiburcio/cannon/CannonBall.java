package space.gameressence.tatiburcio.cannon;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by tybo96789 on 2/23/18.
 */

public class CannonBall extends GameElement {

    private float velocityX;
    private boolean onScreen;

    public CannonBall(CannonView view, int color, int soundID, int x, int y, int radius, float velocityX, float velocityY)
    {
        super(view,color,soundID,x,y,2*radius,2*radius,velocityY);
        this.velocityX = velocityX;
        this.onScreen = true;
    }

    private int getRadius()
    {
        return (shape.right - shape.left)/2;
    }

    public boolean collidesWith(GameElement element)
    {
        return (Rect.intersects(shape,element.shape) && velocityX > 0);
    }

    public boolean isOnScreen()
    {
        return this.onScreen;
    }

    public void reverseVelocityX()
    {
        this.velocityX *= -1;
    }

    @Override
    public void update(double interval) {
        super.update(interval);

        //update horizontal pos
        this.shape.offset((int) (velocityX * interval),0);

        //if cannonball goes off the screen
        if(this.shape.top < 0 || this.shape.left < 0 || this.shape.bottom > this.view.getScreenHeight() || this.shape.right > this.view.getScreenWidth())
            this.onScreen = false;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(this.shape.left + this.getRadius(), this.shape.top + this.getRadius(), this.getRadius(), this.mPaint);
    }
}
