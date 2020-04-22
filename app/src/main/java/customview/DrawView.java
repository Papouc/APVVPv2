package customview;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.samples.vision.ocrreader.R;

/**
 * DrawView class holds corner objects and handle them by drawing
 * overlay.
 *
 * @author Chintan Rathod (http://chintanrathod.com)
 */
public class DrawView extends View {

    Point point1, point3;
    Point point2, point4;

    /**
     * point1 and point 3 are of same group and same as point 2 and point4
     */
    int groupId = -1;
    private ArrayList < ColorBall > colorballs = new ArrayList < ColorBall > ();
    // array that holds the balls
    private int balID = 0;
    // variable to know what ball is being dragged
    Paint paint;
    Canvas canvas;

    public static int delkaX = 0;
    public static  int delkaY = 0;
    public static int[] MujLevHorBody = {150,20};
    public static int[] MujPravHorBody = {400, 20};
    public static int[] MujLevDolBody = {150, 320};
    public static int[] MujPravDolBody = {400, 320};
    public static int temp1;
    public static int temp2;

    public DrawView(Context context) {
        super(context);
        paint = new Paint();
        setFocusable(true); // necessary for getting the touch events
        canvas = new Canvas();
        // setting the start point for the balls
        point1 = new Point();
        point1.x = 150;
        point1.y = 20;

        point2 = new Point();
        point2.x = 400;
        point2.y = 20;

        point3 = new Point();
        point3.x = 400;
        point3.y = 320;

        point4 = new Point();
        point4.x = 150;
        point4.y = 320;

        // declare each ball with the ColorBall class
        colorballs.add(new ColorBall(context, R.drawable.roh, point1));
        colorballs.add(new ColorBall(context, R.drawable.roh, point2));
        colorballs.add(new ColorBall(context, R.drawable.roh, point3));
        colorballs.add(new ColorBall(context, R.drawable.roh, point4));



    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        setFocusable(true); // necessary for getting the touch events
        canvas = new Canvas();
        // setting the start point for the balls
        point1 = new Point();
        point1.x = 150; // 150
        point1.y = 20;  // 20

        point2 = new Point();
        point2.x = 400; //400
        point2.y = 20; //20

        point3 = new Point();
        point3.x = 400; //400
        point3.y = 320; //320

        point4 = new Point();
        point4.x = 150; //150
        point4.y = 320; //320

        // declare each ball with the ColorBall class
        colorballs.add(new ColorBall(context, R.drawable.roh, point1));
        colorballs.add(new ColorBall(context, R.drawable.roh, point2));
        colorballs.add(new ColorBall(context, R.drawable.roh, point3));
        colorballs.add(new ColorBall(context, R.drawable.roh, point4));

    }

    // the method that draws the balls
    @Override
    protected void onDraw(Canvas canvas) {
        // canvas.drawColor(0xFFCCCCCC); //if you want another background color

        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.parseColor("#55000000"));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeJoin(Paint.Join.ROUND);
        // mPaint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(5);

        canvas.drawPaint(paint);
        paint.setColor(Color.parseColor("#55FFFFFF"));

        if (groupId == 1) {
            canvas.drawRect(point1.x + colorballs.get(0).getWidthOfBall() / 2,
                    point3.y + colorballs.get(2).getWidthOfBall() / 2, point3.x + colorballs.get(2).getWidthOfBall() / 2, point1.y + colorballs.get(0).getWidthOfBall() / 2, paint);
        } else {
            canvas.drawRect(point2.x + colorballs.get(1).getWidthOfBall() / 2,
                    point4.y + colorballs.get(3).getWidthOfBall() / 2, point4.x + colorballs.get(3).getWidthOfBall() / 2, point2.y + colorballs.get(1).getWidthOfBall() / 2, paint);
        }
        BitmapDrawable mBitmap;
        mBitmap = new BitmapDrawable();

        // draw the balls on the canvas
        for (ColorBall ball: colorballs) {
            canvas.drawBitmap(ball.getBitmap(), ball.getX(), ball.getY(),
                    new Paint());
        }
    }

    // events when touching the screen
    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();

        int X = (int) event.getX();
        int Y = (int) event.getY();




        /*Log.d("bod1", "------------------------------------------");
        Log.d("bod1", "(LevHor) X : " + String.valueOf(point1.x) + " Y : " + String.valueOf(point1.y));
        Log.d("bod1", "(PravHor) X : " + String.valueOf(point2.x) + " Y : " + String.valueOf(point2.y));
        Log.d("bod1", "(LevDol) X : " + String.valueOf(point4.x) + " Y : " + String.valueOf(point4.y));
        Log.d("bod1", "(PravDol) X : " + String.valueOf(point3.x) + " Y : " + String.valueOf(point3.y));
        Log.d("bod1", "delkaX : " + String.valueOf(point2.x - point3.x) + " delkaY : " + String.valueOf(point4.y - point3.y));*/

        int [] HodnotyX = {point1.x, point2.x, point3.x, point4.x};
        int [] HodnotyY = {point1.y, point2.y, point3.y, point4.y};


        for (int i = 0; i < HodnotyX.length; i++) {
            for (int j = i + 1; j < HodnotyX.length; j++) {
                if (HodnotyX[i] > HodnotyX[j]) {
                    temp1 = HodnotyX[i];
                    HodnotyX[i] = HodnotyX[j];
                    HodnotyX[j] = temp1;
                }
            }
        }
        for (int i = 0; i < HodnotyY.length; i++) {
            for (int j = i + 1; j < HodnotyY.length; j++) {
                if (HodnotyY[i] > HodnotyY[j]) {
                    temp2 = HodnotyY[i];
                    HodnotyY[i] = HodnotyY[j];
                    HodnotyY[j] = temp2;
                }
            }
        }

        if (point1.x == point2.x) {
          if (point1.y > point2.y) {
              delkaY = point1.y - point2.y;
          }  else {
              delkaY = point2.y - point1.y;
          }
        }  else if (point1.x == point3.x) {
            if (point1.y > point3.y) {
                delkaY = point1.y - point3.y;
            } else {
                delkaY = point3.y - point1.y;
            }
        } else if (point1.x == point4.x) {
            if (point1.y > point4.y) {
                delkaY = point1.y - point4.y;
            } else {
                delkaY = point4.y - point1.y;
            }
        } else if (point2.x == point3.x) {
            if (point2.y > point3.y) {
                delkaY = point2.y - point3.y;
            } else {
                delkaY = point3.y - point2.y;
            }
        } else if (point2.x == point4.x) {
            if (point2.y > point4.y) {
                delkaY = point2.y - point4.y;
            } else {
                delkaY = point4.y - point2.y;
            }
        } else if (point3.x == point4.x) {
            if (point3.y > point4.y) {
                delkaY = point3.y - point4.y;
            } else {
                delkaY = point4.y - point3.y;
            }
        }
        if (point1.y == point2.y) {
                if (point1.x > point2.x) {
                    delkaX = point1.x - point2.x;
                } else {
                    delkaX = point2.x - point1.x;
                }
            } else if (point1.y == point3.y) {
                if (point1.x > point3.x) {
                    delkaX = point1.x - point3.x;
                } else {
                    delkaX = point3.x -point1.x;
                }
            } else if (point2.y == point3.y) {
                if (point2.x > point3.x) {
                    delkaX = point2.x - point3.x;
                } else {
                    delkaX = point3.x - point2.x;
                }
            } else if (point2.y == point4.y) {
                if (point2.x > point4.x) {
                    delkaX = point2.x - point4.x;
                } else {
                    delkaX = point4.x - point2.x;
                }
            } else if (point3.y == point4.y) {
                if (point3.x > point4.x) {
                    delkaX = point3.x - point4.x;
                } else {
                    delkaX = point4.x - point3.x;
                }
            }

        MujLevHorBody[0] = HodnotyX[0];
        MujLevHorBody[1] = HodnotyY[0];

        MujPravHorBody[0] = MujLevHorBody[0] + delkaX;
        MujPravHorBody[1] = MujLevHorBody[1];
        MujLevDolBody[0] = MujLevHorBody[0];
        MujLevDolBody[1] = MujLevHorBody[1] + delkaY;
        MujPravDolBody[0] = MujLevHorBody[0] + delkaX;
        MujPravDolBody[1] = MujLevHorBody[1] + delkaY;

        Log.d ("bod1", "--------------------------");
        Log.d ("bod1", "LevHorX : " + String.valueOf(MujLevHorBody[0] ) + " LevHorY : " + String.valueOf(MujLevHorBody[1]));
        Log.d ("bod1", "LevDolX : " + String.valueOf(MujLevDolBody[0] ) + " LevDolY : " + String.valueOf(MujLevDolBody[1]));
        Log.d ("bod1", "PravHorX : " + String.valueOf(MujPravHorBody[0] ) + " PravHorY : " + String.valueOf(MujPravHorBody[1]));
        Log.d ("bod1", "PravDolX : " + String.valueOf(MujPravDolBody[0] ) + " PravDilY : " + String.valueOf(MujPravDolBody[1]));
        Log.d("bod1", "Y : " + String.valueOf(delkaY));
        Log.d("bod1","X : " + String.valueOf(delkaX));


        float dpi = this.getResources().getDisplayMetrics().density;
        Log.d("velikost", String.valueOf(dpi));

        switch (eventaction) {

            case MotionEvent.ACTION_DOWN:
                // touch down so check if the finger is on
                // a ball
                balID = -1;
                groupId = -1;
                for (ColorBall ball: colorballs) {
                    // check if inside the bounds of the ball (circle)
                    // get the center for the ball
                    int centerX = ball.getX() + ball.getWidthOfBall();
                    int centerY = ball.getY() + ball.getHeightOfBall();
                    paint.setColor(Color.CYAN);
                    // calculate the radius from the touch to the center of the ball
                    double radCircle = Math.sqrt((double)(((centerX - X) * (centerX - X)) + (centerY - Y) * (centerY - Y)));

                    if (radCircle < ball.getWidthOfBall()) {

                        balID = ball.getID();
                        if (balID == 1 || balID == 3) {
                            groupId = 2;
                            canvas.drawRect(point1.x, point3.y, point3.x, point1.y,
                                    paint);
                        } else {
                            groupId = 1;
                            canvas.drawRect(point2.x, point4.y, point4.x, point2.y,
                                    paint);
                        }
                        invalidate();
                        break;
                    }
                    invalidate();
                }

                break;

            case MotionEvent.ACTION_MOVE:
                // touch drag with the ball
                // move the balls the same as the finger
                if (balID > -1) {
                    colorballs.get(balID).setX(X);
                    colorballs.get(balID).setY(Y);

                    paint.setColor(Color.CYAN);

                    if (groupId == 1) {
                        colorballs.get(1).setX(colorballs.get(0).getX());
                        colorballs.get(1).setY(colorballs.get(2).getY());
                        colorballs.get(3).setX(colorballs.get(2).getX());
                        colorballs.get(3).setY(colorballs.get(0).getY());
                        canvas.drawRect(point1.x, point3.y, point3.x, point1.y,
                                paint);
                    } else {
                        colorballs.get(0).setX(colorballs.get(1).getX());
                        colorballs.get(0).setY(colorballs.get(3).getY());
                        colorballs.get(2).setX(colorballs.get(3).getX());
                        colorballs.get(2).setY(colorballs.get(1).getY());
                        canvas.drawRect(point2.x, point4.y, point4.x, point2.y,
                                paint);
                    }

                    invalidate();
                }

                break;

            case MotionEvent.ACTION_UP:
                // touch drop - just do things here after dropping

                break;
        }
        // redraw the canvas
        invalidate();
        return true;

    }

    public void shade_region_between_points() {
        canvas.drawRect(point1.x, point3.y, point3.x, point1.y, paint);
    }
}
