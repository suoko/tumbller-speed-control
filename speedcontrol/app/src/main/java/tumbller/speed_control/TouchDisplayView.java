/*
 * tumbller-speed-control
 * Copyright (c) 2022 Peter Nebe (mail@peter-nebe.dev)
 *
 * This file is part of tumbller-speed-control.
 *
 * tumbller-speed-control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * tumbller-speed-control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with tumbller-speed-control.  If not, see <https://www.gnu.org/licenses/>.
 */

package tumbller.speed_control;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.*;
import android.util.AttributeSet;
import android.view.*;

public class TouchDisplayView extends View {
    private static final int CIRCLE_RADIUS_DP = 20;
    private static final int COLOR_IDLE = 0xffcc0000;
    private static final int COLOR_ACTIVE = 0xffff4444;
    private final int mCircleRadius;
    private final Paint mCirclePaint = new Paint();
    private final GestureDetector mDoubleTapDetector;
    private Point mOrigin = null;
    private Rect mOriginRect = null;
    private Point mControlSpot = null;
    private MotionControlPanel mControlPanel = null;
    private boolean mSpotActive = false;

    public TouchDisplayView(Context context, AttributeSet attrs) {
        super(context, attrs);

        float density = getResources().getDisplayMetrics().density;
        mCircleRadius = (int) (CIRCLE_RADIUS_DP * density);
        mCirclePaint.setColor(COLOR_IDLE);
        mDoubleTapDetector = new GestureDetector(context, new DoubleTapDetector());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mOrigin = new Point(w / 2, h / 2);
        mOriginRect = new Rect(mOrigin.x - mCircleRadius,
                               mOrigin.y - mCircleRadius,
                               mOrigin.x + mCircleRadius,
                               mOrigin.y + mCircleRadius);
        mControlSpot = new Point(mOrigin);
        mControlPanel = new MotionControlPanel(w, h, getContext());

        drawBackground();
    }

    private class DoubleTapDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            String filename = "motion.prog";
            new AlertDialog.Builder(getContext())
                    .setMessage("Run " + filename + "?")
                    .setPositiveButton("Run", (di, i) -> ProgrammableMotion.start(filename, getContext()))
                    .setNegativeButton("Cancel", (di, i) -> {})
                    .create()
                    .show();
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (mDoubleTapDetector.onTouchEvent(event))
                    break;
                if (mOriginRect.contains((int) event.getX(), (int) event.getY())) {
                    mCirclePaint.setColor(COLOR_ACTIVE);
                    mSpotActive = true;
                }
                break;

            case MotionEvent.ACTION_UP:
                mDoubleTapDetector.onTouchEvent(event);
                if (mSpotActive) {
                    mControlSpot.set(mOrigin.x, mOrigin.y);
                    mControlPanel.resetSpot();
                    mCirclePaint.setColor(COLOR_IDLE);
                    mSpotActive = false;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mSpotActive) {
                    mControlSpot.set((int) event.getX(), (int) event.getY());
                    mControlPanel.moveSpot(toUprightCoord(mControlSpot, mOrigin));
                }
                break;
        }

        this.postInvalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mControlSpot.x, mControlSpot.y, mCircleRadius, mCirclePaint);
    }

    Point toUprightCoord(Point spot, Point origin) {
        return new Point(spot.x - origin.x, origin.y - spot.y);
    }

    void drawBackground() {
        int width = getWidth();
        int height = getHeight();
        Bitmap bitmap = Bitmap.createBitmap(getResources().getDisplayMetrics(), width, height, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.WHITE);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setColor(0xffeeeeee);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        final int divisorHi = 2;
        final int divisorLo = 8;
        PointF[] hi = calcPositions(width, height, divisorHi);
        PointF[] lo = calcPositions(width, height, divisorLo);

        drawLineHor(hi[0].y, width, canvas, paint);
        drawLineHor(lo[0].y, width, canvas, paint);
        drawLineHor(mOrigin.y, width, canvas, paint);
        drawLineHor(lo[1].y, width, canvas, paint);
        drawLineHor(hi[1].y, width, canvas, paint);

        drawLineVer(hi[0].x, height, canvas, paint);
        drawLineVer(lo[0].x, height, canvas, paint);
        drawLineVer(mOrigin.x, height, canvas, paint);
        drawLineVer(lo[1].x, height, canvas, paint);
        drawLineVer(hi[1].x, height, canvas, paint);

        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        setBackground(drawable);
    }

    PointF[] calcPositions(int width, int height, int divisor) {
        final float half = 0.5f;
        final float part = half / (float)Math.sqrt(divisor);
        final float factorNeg = half - part;
        final float factorPos = half + part;

        return new PointF[] {
                        new PointF(width * factorNeg, height * factorNeg),
                        new PointF(width * factorPos, height * factorPos)
                    };
    }

    void drawLineHor(float y, float width, Canvas canvas, Paint paint) {
        canvas.drawLine(0f, y, width, y, paint);
    }

    void drawLineVer(float x, float height, Canvas canvas, Paint paint) {
        canvas.drawLine(x, 0f, x, height, paint);
    }
}