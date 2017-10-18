package com.bigstar.mvp.view;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


import com.bigstar.mvp.R;

/**
 * 视频播放的动态无限切换view
 */

public class PlayButtonView extends View {

    // 接口回调对象
    public PlayPauseListener listener;
    // 是否播放
    private boolean isPlaying;
    // View宽度
    private int mWidth;
    // View高度
    private int mHeight;
    // 动画时间
    private int mAnimDuration = 200;
    // 创建画笔
    private Paint mPaint;
    //暂停时左侧竖条Path
    private Path mLeftPath;
    //暂停时右侧竖条Path
    private Path mRightPath;
    // 动画Progress
    private float mProgress;
    // 整体背景
    private int mBgColor = Color.WHITE;
    // 点击背景
    private int mBtnColor = Color.BLACK;
    // 圆的半径
    private int mRadius;

    private int mDirection = Direction.POSITIVE.value;
    // 圆内矩形宽度
    private float mRectWidth;
    // 圆内矩形高度
    private float mRectHeight;
    // 设置padding
    private float mPadding;
    // 矩形左侧上侧坐标
    private int mRectLT;
    // 两个暂停竖条中间的空隙,默认为两侧竖条的宽度
    private float mGapWidth;
    // 枚举类型
    public enum Direction {
        // 顺时针
        POSITIVE(1),
        // 逆时针
        NEGATIVE(2);
        int value;

        Direction(int value) {
            this.value = value;
        }
    }
    /**
     * 绘制时控制文本绘制的范围
     */
    private Rect mRect;

    public PlayButtonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mLeftPath = new Path();
        mRightPath = new Path();
        mRect = new Rect();
        // 获取并绑定这个View
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PlayPauseView);
        // 设置背景颜色
        mBgColor = typedArray.getColor(R.styleable.PlayPauseView_bg_color, Color.WHITE);
        // 设置内部图标
        mBtnColor = typedArray.getColor(R.styleable.PlayPauseView_btn_color, Color.BLACK);
        // 设置局部大小
        mGapWidth = typedArray.getFloat(R.styleable.PlayPauseView_gap_width, 0);
        // 设置旋转那种类型
        mDirection = typedArray.getInt(R.styleable.PlayPauseView_anim_direction, Direction.POSITIVE.value);
        // 设置Padding大小
        mPadding = typedArray.getFloat(R.styleable.PlayPauseView_space_padding, 0);
        // 设置动画时间
        mAnimDuration = typedArray.getInt(R.styleable.PlayPauseView_anim_duration,mAnimDuration);
        typedArray.recycle();
    }


    // 对View 进行测量 算需要多少面积
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取到当前的宽高
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        switch (widthMode){
            // 一般是设置了明确的值或者是match_parent
            case MeasureSpec.EXACTLY:
                // do someThing...求最小值
                mWidth = mHeight = Math.min(mWidth, mHeight);
                setMeasuredDimension(mWidth,mHeight);
                break;
            // 表示子布局限制在一个最大值内,一般都是warp_content
            case MeasureSpec.AT_MOST:
                // do someThing...默认设置一个大小 默认50dp
                float density = getResources().getDisplayMetrics().density;
                mWidth = mHeight = (int) (50 * density);
                setMeasuredDimension(mWidth,mHeight);
                break;
            // 表示子布局多大就使用多大,一般用不到这个
            case MeasureSpec.UNSPECIFIED:
                // do someThing...
                break;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = mHeight = w;
        initValue();
    }

    private void initValue() {
        mRadius = mWidth / 2;

        mPadding = getSpacePadding() == 0 ? mRadius / 3f : getSpacePadding();
        if (getSpacePadding() > mRadius / Math.sqrt(2) || mPadding < 0) {
            // 默认值
            mPadding = mRadius / 3f;
        }
        // 矩形宽高的一半
        float space = (float) (mRadius / Math.sqrt(2) - mPadding);
        mRectLT = (int) (mRadius - space);
        int rectRB = (int) (mRadius + space);
        mRect.top = mRectLT;
        mRect.bottom = rectRB;
        mRect.left = mRectLT;
        mRect.right = rectRB;
        // 改为float类型，否则动画有抖动。并增加一像素防止三角形之间有缝隙
        mRectWidth = 2 * space + 1;
        mRectHeight = 2 * space + 1;
        mGapWidth = getGapWidth() != 0 ? getGapWidth() : mRectWidth / 3;
        mProgress = isPlaying ? 0 : 1;
        mAnimDuration = getAnimDuration() < 0 ? 200 : getAnimDuration();
    }

    // 对图片进行绘制,走你
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mLeftPath.rewind();
        mRightPath.rewind();
        // 设置颜色
        mPaint.setColor(mBgColor);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mPaint);
        // 暂停时左右两边矩形距离
        float distance = mGapWidth * (1 - mProgress);
        // 一个矩形的宽度
        float barWidth = mRectWidth / 2 - distance / 2;
        // 左边矩形左上角
        float leftLeftTop = barWidth * mProgress;
        // 右边矩形左上角
        float rightLeftTop = barWidth + distance;
        // 右边矩形右上角
        float rightRightTop = 2 * barWidth + distance;
        // 右边矩形右下角
        float rightRightBottom = rightRightTop - barWidth * mProgress;

        mPaint.setColor(mBtnColor);
        mPaint.setStyle(Paint.Style.FILL);

        if (mDirection == Direction.NEGATIVE.value) {
            mLeftPath.moveTo(mRectLT, mRectLT);
            mLeftPath.lineTo(leftLeftTop + mRectLT, mRectHeight + mRectLT);
            mLeftPath.lineTo(barWidth + mRectLT, mRectHeight + mRectLT);
            mLeftPath.lineTo(barWidth + mRectLT, mRectLT);
            mLeftPath.close();

            mRightPath.moveTo(rightLeftTop + mRectLT, mRectLT);
            mRightPath.lineTo(rightLeftTop + mRectLT, mRectHeight + mRectLT);
            mRightPath.lineTo(rightRightBottom + mRectLT, mRectHeight + mRectLT);
            mRightPath.lineTo(rightRightTop + mRectLT, mRectLT);
            mRightPath.close();
        } else {
            mLeftPath.moveTo(leftLeftTop + mRectLT, mRectLT);
            mLeftPath.lineTo(mRectLT, mRectHeight + mRectLT);
            mLeftPath.lineTo(barWidth + mRectLT, mRectHeight + mRectLT);
            mLeftPath.lineTo(barWidth + mRectLT, mRectLT);
            mLeftPath.close();

            mRightPath.moveTo(rightLeftTop + mRectLT, mRectLT);
            mRightPath.lineTo(rightLeftTop + mRectLT, mRectHeight + mRectLT);
            mRightPath.lineTo(rightLeftTop + mRectLT + barWidth, mRectHeight + mRectLT);
            mRightPath.lineTo(rightRightBottom + mRectLT, mRectLT);
            mRightPath.close();
        }

        canvas.save();

        canvas.translate(mRectHeight / 8f * mProgress, 0);

        float progress = isPlaying ? (1 - mProgress) : mProgress;
        int corner = mDirection == Direction.NEGATIVE.value ? -90 : 90;
        float rotation = isPlaying ? corner * (1 + progress) : corner * progress;
        canvas.rotate(rotation, mWidth / 2f, mHeight / 2f);

        canvas.drawPath(mLeftPath, mPaint);
        canvas.drawPath(mRightPath, mPaint);

        canvas.restore();
    }

    public void setPlayPauseListener(final PlayPauseListener listener) {
        this.listener = listener;
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying()){
                    pause();
                    // 回调该值的状态
                    if (listener != null){
                        listener.Pause();
                    }
                }else{
                    play();
                    // 回调该值的状态
                    if (listener != null){
                        listener.Play();
                    }
                }
            }
        });
    }


    // 播放
    public void play(){
        // 如果当前不为空的,取消动画,改变当前播放状态
        if (getPlayPauseAnim() != null){
            getPlayPauseAnim().cancel();
        }
        setPlaying(true);
        getPlayPauseAnim().start();
    }

    // 暂停
    public void pause(){
        // 如果当前不为空的,取消动画,改变当前播放状态
        if (getPlayPauseAnim() != null){
            getPlayPauseAnim().cancel();
        }
        setPlaying(false);
        getPlayPauseAnim().start();
    }

    // 设置动画效果
    public ValueAnimator getPlayPauseAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(isPlaying ? 1 : 0, isPlaying ? 0 : 1);
        valueAnimator.setDuration(mAnimDuration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mProgress = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        return valueAnimator;
    }


    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public float getSpacePadding() {
        return mPadding;
    }

    public int getAnimDuration() {
        return mAnimDuration;
    }

    public float getGapWidth() {
        return mGapWidth;
    }

    public interface PlayPauseListener{
        // 开始接口
        void Play();
        // 暂停接口
        void Pause();
    }

}
