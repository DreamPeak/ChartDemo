package com.smartvot.chartdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ===================================
 * 飞行曲线编辑自定义折线图
 * ===================================
 */

public class MyAssistCurveView extends View  {
    private String TAG="MyAssistCurveView";
    private Context context;
    private Path path;

    private Path mPath;
    private Path mAssistPath;
    private List<Point> mPointList;
    private float lineSmoothness = 0.2f;
    private PathMeasure mPathMeasure;
    private float drawScale = 1f;
    private float defYAxis = 700f;
    private float defXAxis = 10f;


    private float XLength;
    private float YLength;
    private float XScale=55;
    private float YScale=32;
    private float XPoint=60;
    private float YPoint=330;
    private PointF start,end,control;
    private List<Integer> steps=new ArrayList<>();
    private float XtextScale;//X方向文字显示间距
    private float XtimeScale;//X方向单位时间(5分钟)
    private float YstepsScale;//步数的单位
    private final static String X1_KEY="x1pos";
    private final static String Y1_KEY="y1pos";
    private List<Map<String,Float>> mListPoint=new ArrayList<>();
    private List<Map<String,Float>> mRedListPoint=new ArrayList<>();
    private double XStandardMark;//X轴每格的标度
    private double YStandardMark;//Y轴每格的标度
    Paint mPaint=new Paint();
    Paint mPaint2=new Paint();
    Paint mLinePaint=new Paint();
    Paint textPaint=new Paint();
    DecimalFormat decimalFormat=new DecimalFormat("0.0");//保留一位小数
    Animation animation;
    public MyAssistCurveView(Context context) {
        super(context);
        initView(context);

    }
    public MyAssistCurveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyAssistCurveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    private void initView(Context context) {
        this.context=context;
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true);//去锯齿
        mLinePaint.setColor(Color.parseColor("#46A5FA"));
        mLinePaint.setStrokeWidth(2);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);//去锯齿
        mPaint.setColor(Color.parseColor("#E6E6E6"));
        mPaint.setStrokeWidth(dp2px(2));

        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setAntiAlias(true);//去锯齿
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(sp2px(12));

        mPaint2.setStyle(Paint.Style.STROKE);
        mPaint2.setAntiAlias(true);//去锯齿
        mPaint2.setColor(Color.parseColor("#666666"));
        mPaint2.setStrokeWidth(2.5f);
        path=new Path();
        start=new PointF(0,0);
        end=new PointF(0,0);
        control=new PointF(0,0);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        XLength=MeasureSpec.getSize(widthMeasureSpec);
        YLength=MeasureSpec.getSize(heightMeasureSpec);
        XtextScale=(XLength-dp2px(20f))/9;
        XtimeScale=(XLength-dp2px(20f))/(12f);
        YstepsScale=(YLength-dp2px(62f))/2f/10000f;//暂定为10000步最大
        Log.e("TEST     XtimeScale=",px2dip(XtimeScale)+"");
        Log.e("TEST     YstepsScale=",px2dip(YLength)+"");
        Log.e("TEST     YstepsScale=",px2dip(YstepsScale)+"");
        Log.e("TEST     YstepsScale=",px2dip(YLength-dp2px(62f))+"");
        Log.e("TEST     YstepsScale=",px2dip((YLength-dp2px(62f))/2f)+"");
        Log.e("TEST     YstepsScale=",px2dip((YLength-dp2px(62f))/2f/10000f)+"");
        Log.e("sdfsdfe",widthMeasureSpec+"  ,  "+heightMeasureSpec);
        Log.e("sdfsdfeVVVVV",px2dip(MeasureSpec.getSize(widthMeasureSpec))+"  ,  "+px2dip(MeasureSpec.getSize(heightMeasureSpec)));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
//         XLength= context.getResources().getDisplayMetrics().widthPixels;
//         YLength= context.getResources().getDisplayMetrics().heightPixels;
        XPoint=left;
        YPoint=top;
        Log.e("sdfsdfeTTT",px2dip(left)+"  ,  "+px2dip(top)+"  ,  "+px2dip(right)+"  ,  "+px2dip(bottom));
    }



    @Override
    protected void onDraw(Canvas canvas) {

        xxxxxx(canvas);
        Log.e("TEST     steps.size 1",steps.size()+"");
        super.onDraw(canvas);
        canvas.drawLine(0,  YLength - dp2px(62), XLength, YLength - dp2px(62), mPaint);
        canvas.drawText("00:00",XtextScale*0+dp2px(20),YLength - dp2px(45),textPaint);
        canvas.drawText("06:00",XtextScale*2+dp2px(20),YLength - dp2px(45),textPaint);
        canvas.drawText("12:00",XtextScale*4+dp2px(20),YLength - dp2px(45),textPaint);
        canvas.drawText("18:00",XtextScale*6+dp2px(20),YLength - dp2px(45),textPaint);
        canvas.drawText("24:00",XtextScale*8+dp2px(20),YLength - dp2px(45),textPaint);

//        int startx=100;
//        int endx=400;
//        int starty=300;
//        int endy=500;
//        path.moveTo(startx,starty);
//        path.quadTo((startx+endx)/2,(starty+endy)/2+100,endx,endy);
//        canvas.drawPath(path,mLinePaint);




//        if (steps.size()!=0) {
//            int MaxLineNums=steps.size()-1;
//            for (int i = 0; i < MaxLineNums; i++) {
//                start.x=XtimeScale * i + dp2px(20);
//                start.y=YLength - dp2px(62) - steps.get(i) * YstepsScale;
//                end.x=XtimeScale * (i+1) + dp2px(20);
//                end.y=YLength - dp2px(62) - steps.get(i + 1) * YstepsScale;
//                //control.x=(start.x+end.x)/2;
//                control.x=(end.x-start.x)*0.8f+start.x;
//                if (start.y>end.y){
//                    control.y=(start.y+end.y+dp2px(20))/2;
//                }else {
//                    control.y=(start.y+end.y-dp2px(20))/2;
//                }
//
//                path.moveTo(start.x,start.y);
//                path.quadTo(control.x,control.y,end.x,end.y);
//         Log.e("TEST     path","startX= "+px2dip(start.x)+"     startY="+px2dip(start.y)+"    stopX= "+px2dip(end.x)+"    stopY="+px2dip(end.y)+"   controlx="+px2dip(control.x)+"   controly"+px2dip(control.y));
//
//           canvas.drawPath(path,mLinePaint);
//            }
//       }

    }

    private void xxxxxx(Canvas canvas) {
        if (mPointList == null)
            return;
        //measurePath();
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        //绘制辅助线
        canvas.drawPath(mAssistPath,paint);

        paint.setColor(Color.GREEN);
        Path dst = new Path();
        dst.rLineTo(0, 0);
        float distance = mPathMeasure.getLength() * drawScale;
        if (mPathMeasure.getSegment(0, distance, dst, true)) {
            //绘制线
            canvas.drawPath(dst, paint);
            float[] pos = new float[2];
            mPathMeasure.getPosTan(distance, pos, null);
            //绘制阴影
            drawShadowArea(canvas, dst, pos);
            //绘制点
            drawPoint(canvas,pos);
        }
        /*greenPaint.setPathEffect(getPathEffect(mPathMeasure.getLength()));
        canvas.drawPath(mPath, greenPaint);*/
        //mPath.reset();adb shell screenrecord --bit-rate 2000000 /sdcard/test.mp4

    }






















    public float dp2px(float dpValue){

        final float scale=context.getResources().getDisplayMetrics().density;

        return (dpValue*scale+0.5f);

    }
    public  float px2dip(float pxValue){

        final float scale=context.getResources().getDisplayMetrics().density;

        return pxValue/scale+0.5f;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_MOVE){
            Log.e("sdfsdfeTTT5",event.getX()+"      "+event.getY());
        }
        return true;
    }
    public  int px2sp( float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
    public  int sp2px( float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
    public  void setSteps(List steps){
        this.steps=steps;
        postInvalidate();
    }



    private void measurePath() {
        mPath = new Path();
        mAssistPath = new Path();
        float prePreviousPointX = Float.NaN;
        float prePreviousPointY = Float.NaN;
        float previousPointX = Float.NaN;
        float previousPointY = Float.NaN;
        float currentPointX = Float.NaN;
        float currentPointY = Float.NaN;
        float nextPointX;
        float nextPointY;

        final int lineSize = mPointList.size();
        for (int valueIndex = 0; valueIndex < lineSize; ++valueIndex) {
            if (Float.isNaN(currentPointX)) {
                Point point = mPointList.get(valueIndex);
                currentPointX = XtimeScale * valueIndex + dp2px(20);
                currentPointY = YLength - dp2px(62) - steps.get(valueIndex) * YstepsScale;






            }
            if (Float.isNaN(previousPointX)) {
                //是否是第一个点
                if (valueIndex > 0) {
                    Point point = mPointList.get(valueIndex - 1);
                    previousPointX = XtimeScale * (valueIndex - 1) + dp2px(20);
                    previousPointY = YLength - dp2px(62) - steps.get(valueIndex - 1) * YstepsScale;
                } else {
                    //是的话就用当前点表示上一个点
                    previousPointX = currentPointX;
                    previousPointY = currentPointY;
                }
            }

            if (Float.isNaN(prePreviousPointX)) {
                //是否是前两个点
                if (valueIndex > 1) {
                    Point point = mPointList.get(valueIndex - 2);
                    prePreviousPointX = XtimeScale * (valueIndex - 2) + dp2px(20);
                    prePreviousPointY = YLength - dp2px(62) - steps.get(valueIndex - 2) * YstepsScale;
                } else {
                    //是的话就用当前点表示上上个点
                    prePreviousPointX = previousPointX;
                    prePreviousPointY = previousPointY;
                }
            }

            // 判断是不是最后一个点了
            if (valueIndex < lineSize - 1) {
                Point point = mPointList.get(valueIndex + 1);
                nextPointX = XtimeScale * (valueIndex +1) + dp2px(20);
                nextPointY =YLength - dp2px(62) - steps.get(valueIndex +1) * YstepsScale;
            } else {
                //是的话就用当前点表示下一个点
                nextPointX = currentPointX;
                nextPointY = currentPointY;
            }

            if (valueIndex == 0) {
                // 将Path移动到开始点
                mPath.moveTo(currentPointX, currentPointY);
                mAssistPath.moveTo(currentPointX, currentPointY);
            } else {
                // 求出控制点坐标
                final float firstDiffX = (currentPointX - prePreviousPointX);
                final float firstDiffY = (currentPointY - prePreviousPointY);
                final float secondDiffX = (nextPointX - previousPointX);
                final float secondDiffY = (nextPointY - previousPointY);
                final float firstControlPointX = previousPointX + (lineSmoothness * firstDiffX);
                final float firstControlPointY = previousPointY + (lineSmoothness * firstDiffY);
                final float secondControlPointX = currentPointX - (lineSmoothness * secondDiffX);
                final float secondControlPointY = currentPointY - (lineSmoothness * secondDiffY);
                //画出曲线
                mPath.cubicTo(firstControlPointX, firstControlPointY, secondControlPointX, secondControlPointY,
                        currentPointX, currentPointY);
                //将控制点保存到辅助路径上
                mAssistPath.lineTo(firstControlPointX, firstControlPointY);
                mAssistPath.lineTo(secondControlPointX, secondControlPointY);
                mAssistPath.lineTo(currentPointX, currentPointY);
            }

            // 更新值,
            prePreviousPointX = previousPointX;
            prePreviousPointY = previousPointY;
            previousPointX = currentPointX;
            previousPointY = currentPointY;
            currentPointX = nextPointX;
            currentPointY = nextPointY;
        }
        mPathMeasure = new PathMeasure(mPath, false);
    }



    public void setPointList(List<Point> pointList) {
        mPointList = pointList;
        measurePath();
    }

    public void setLineSmoothness(float lineSmoothness) {
        if (lineSmoothness != this.lineSmoothness) {
            this.lineSmoothness = lineSmoothness;
            measurePath();
            postInvalidate();
        }
    }







    /**
     * 绘制阴影
     * @param canvas
     * @param path
     * @param pos
     */
    private void drawShadowArea(Canvas canvas, Path path, float[] pos) {
        path.lineTo(pos[0], defYAxis);
        path.lineTo(defXAxis, defYAxis);
        path.close();
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0x88CCCCCC);
        canvas.drawPath(path, paint);
    }

    /**
     * 绘制点
     * @param canvas
     * @param pos
     */
    private void drawPoint(Canvas canvas, final float[] pos){
        Paint redPaint = new Paint();
        redPaint.setColor(Color.RED);
        redPaint.setStrokeWidth(3);
        redPaint.setStyle(Paint.Style.FILL);
        for (Point point : mPointList) {
            if (point.x > pos[0]) {
                break;
            }
            canvas.drawCircle(point.x, point.y, 10, redPaint);
        }
    }



}
