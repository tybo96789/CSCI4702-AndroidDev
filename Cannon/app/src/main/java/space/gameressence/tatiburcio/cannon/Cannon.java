package space.gameressence.tatiburcio.cannon;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * Created by tybo96789 on 2/23/18.
 */

public class Cannon {

    private int baseRadius, barrelLength;
    private Point barrelEnd = new Point();
    private double barrelAngle;
    private CannonBall mCannonBall;
    private Paint mPaint = new Paint();
    private CannonView view;

    public Cannon(CannonView view, int baseRadius, int barrelLength, int barrelWidth ) {
        this.baseRadius = baseRadius;
        this.barrelLength = barrelLength;
        this.view = view;
        this.mPaint.setStrokeWidth(barrelWidth);
        this.mPaint.setColor((int) R.color.cannonColor);
        this.align(Math.PI/2);
    }

    public void align(double barrelAngle) {
        this.barrelAngle = barrelAngle;
        this.barrelEnd.x = (int) (this.barrelLength * Math.sin(this.barrelAngle));
        this.barrelEnd.y = (int) (-this.barrelLength * Math.cos(this.barrelAngle)) + view.getScreenHeight()/2;
    }

    public void fireCannonBall()
    {
        int velocityX = (int) (CannonView.CANNONBALL_SPEED_PERCENT * this.view.getScreenHeight() * Math.sin(this.barrelAngle));

        int velocityY = (int) (CannonView.CANNONBALL_SPEED_PERCENT * this.view.getScreenHeight() * -Math.cos(this.barrelAngle));

        int radius = (int) (view.getScreenHeight() * CannonView.CANNONBALL_RADIUS_PERCENT);

        this.mCannonBall = new CannonBall(this.view, (int) R.color.cannonColor, CannonView.CANNON_SOUND_ID, -radius, this.view.getScreenHeight()/2 - radius, radius, velocityX, velocityY);

        this.mCannonBall.playSound();
    }

    public void draw(Canvas canvas)
    {
        canvas.drawLine(0, this.view.getScreenHeight()/2, this.barrelEnd.x, this.barrelEnd.y, this.mPaint);

        canvas.drawCircle(0, (int) this.view.getScreenHeight()/2, (int) baseRadius, this.mPaint);
    }

    public CannonBall getCannonBall() {
        return mCannonBall;
    }

    public void removeCannonBall()
    {
        this.mCannonBall = null;
    }


}
