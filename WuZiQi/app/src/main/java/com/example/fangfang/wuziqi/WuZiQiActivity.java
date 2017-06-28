package com.example.fangfang.wuziqi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangfang on 2017/6/27.
 */

public class WuZiQiActivity extends View {
    private Paint mPaint = new Paint();
    private int mWidth;
    private float mHeight;
    private int MAX_LINE = 10;
    private Bitmap whiteQi;   //白棋
    private Bitmap blackQi;//黑棋

    private int mUnder;

    private float ratioPieceOfLineHight = 3 * 1.0f / 4;

    private boolean gameOver;
    private boolean whiteWin;

    private boolean isWhite = true;
    private List<Point> whiteArray = new ArrayList<>();
    private List<Point> blackArray = new ArrayList<>();

    private onGameListener gameListener;

    private int MAX_PIECE_LINE = 5;
    public static int WHITE_WIN = 0;
    public static int BLACK_WIN = 1;
    public static int NOT_WIN = 2;

    private WuZiQiActivity(Context context) {
        this(context, null);
    }

    public interface onGameListener {
        void onGameOver(int i);
    }

    public void setOnGameListener(WuZiQiActivity.onGameListener gameListener) {
        this.gameListener = gameListener;
    }

    public WuZiQiActivity(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(0x44ff0000);
        init();
    }

    private void init() {
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        whiteQi = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2);
        blackQi = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = Math.min(widthSize, heightSize);
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = heightSize;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = mWidth * 1.0f / MAX_LINE;
        mUnder = h - (h - mWidth) / 2;
        int pieceWidth = (int) (mHeight * ratioPieceOfLineHight);
        whiteQi = Bitmap.createScaledBitmap(whiteQi, pieceWidth, pieceWidth, false);
        blackQi = Bitmap.createScaledBitmap(blackQi, pieceWidth, pieceWidth, false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gameOver) {
            return false;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            Point p = getVaLidPoint(x, y);
            if (whiteArray.contains(p) || blackArray.contains(p)) {
                return false;
            }
            if (isWhite) {
                whiteArray.add(p);
            } else {
                blackArray.add(p);
            }
            invalidate();
            isWhite = !isWhite;
        }
        return true;
    }

    private Point getVaLidPoint(int x, int y) {
        return new Point((int) (x / mHeight), (int) (y / mHeight));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBroad(canvas);
        drawPieces(canvas);
        checkGameOver();
    }

    private void checkGameOver() {
        boolean whiteWin = checkFiveInLine(whiteArray);
        boolean blackWin = checkFiveInLine(blackArray);

        if (whiteWin || blackWin) {
            gameOver = true;
            if (gameListener != null) {
                gameListener.onGameOver(whiteWin ? WHITE_WIN : BLACK_WIN);
            }
        }
    }

    private boolean checkFiveInLine(List<Point> points) {
        for (Point p : points) {
            int x = p.x;
            int y = p.y;

            boolean win_flag = checkHorizontal(x, y, points) || checkVertical(x, y, points)
                    || checkLeftDiagonl(x, y, points) || checkRightDiagonl(x, y, points);
            if (win_flag) {
                return true;
            }
        }
        return false;
    }

    private boolean checkRightDiagonl(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1; i < MAX_PIECE_LINE; i++) {
            if (points.contains(new Point(x - i, y - i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_PIECE_LINE) return true;
        for (int i = 1; i < MAX_PIECE_LINE; i++) {
            if (points.contains(new Point(x + i, y + i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_PIECE_LINE) return true;
        return false;

    }

    private boolean checkLeftDiagonl(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1; i < MAX_PIECE_LINE; i++) {
            if (points.contains(new Point(x + i, y - i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_PIECE_LINE) return true;
        for (int i = 1; i < MAX_PIECE_LINE; i++) {
            if (points.contains(new Point(x - i, y + i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_PIECE_LINE) return true;
        return false;
    }

    private boolean checkVertical(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1; i < MAX_PIECE_LINE; i++) {
            if (points.contains(new Point(x, y - i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_PIECE_LINE) return true;
        for (int i = 1; i < MAX_PIECE_LINE; i++) {
            if (points.contains(new Point(x, y + i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_PIECE_LINE) return true;
        return false;
    }

    private boolean checkHorizontal(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1; i < MAX_PIECE_LINE; i++) {
            if (points.contains(new Point(x - i, y))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_PIECE_LINE)
            return true;
        for (int i = 1; i < MAX_PIECE_LINE; i++) {
            if (points.contains(new Point(x + i, y))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_PIECE_LINE) return true;
        return false;
    }


    private void drawPieces(Canvas canvas) {
        for (int i = 0; i < whiteArray.size(); i++) {
            Point whitePoint = whiteArray.get(i);
            canvas.drawBitmap(whiteQi, (whitePoint.x + (1 - ratioPieceOfLineHight) / 2) * mHeight,
                    (whitePoint.y + (1 - ratioPieceOfLineHight) / 2) * mHeight, null);
        }
        for (int i = 0; i < blackArray.size(); i++) {
            Point blackPoint = blackArray.get(i);
            canvas.drawBitmap(blackQi, (blackPoint.x + (1 - ratioPieceOfLineHight) / 2) * mHeight,
                    (blackPoint.y + (1 - ratioPieceOfLineHight) / 2) * mHeight, null);
        }

    }

    private void drawBroad(Canvas canvas) {
        int w = mWidth;
        float h = mHeight;
        for (int i = 0; i < MAX_LINE; i++) {
            int startX = (int) (h / 2);
            int endX = (int) (w - h / 2);
            int y = (int) ((i + 0.5) * h);
            canvas.drawLine(startX, y, endX, y, mPaint);
            canvas.drawLine(y, startX, y, endX, mPaint);
        }
    }

    public int getUnder() {
        return mUnder;
    }

    protected void restartGame(){
        whiteArray.clear();
        blackArray.clear();
        gameOver = false;
        isWhite = false;
        invalidate();
    }


}
