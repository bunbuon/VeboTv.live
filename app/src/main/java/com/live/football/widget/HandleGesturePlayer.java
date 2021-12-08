package com.hoanmy.football.widget;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.hoanmy.football.utils.ScreenUtils;


public class HandleGesturePlayer implements GestureDetector.OnGestureListener {
    public interface GestureListener {
        void onChangeVolumn(int volumn);

        void onChangeBrightness(int brightness);

        void onStartGestureSeek();

        void onSeek(long startTimeSeek, int second);

        void onSingleTapUp();
    }

    private enum SwipeAction {
        CHANGE_BRIGHTNESS, CHANGE_VOLUMN, SEEK, NONE;
        long value;

        public long getValue() {
            return value;
        }

        public void setValue(long value) {
            this.value = value;
        }
    }

    private Context context;

    private GestureListener listener;

    //Sử dụng để bắt gesture trên màn hình
    private GestureDetector mGestureDetector;

    //Sử dụng để thay đổi âm lượng
    private AudioManager audioManager;
    private int maxVolumn;

    //Sử dụng để thay đổi độ sáng màn hình
    private WindowManager.LayoutParams layoutParams;

    private SwipeAction mSwipeAction;

    private final int SWIPE_TO_TOP = 0;
    private final int SWIPE_TO_BOTTOM = 1;
    private final int SWIPE_TO_LEFT = 2;
    private final int SWIPE_TO_RIGHT = 3;

    private final int MIN_PIXEL_TRIGGER = 30;

    final int NUMBER_PIXEL_PER_SECOND_SEEK = 20;

    private float p1X = -1;
    private float p1Y = -1;

    private float brightness;
    private int volumn;
    private long startTimeSeek = 0;

    private int MAX_PROGRESS_VOLUMN = 15;

    public HandleGesturePlayer(Context context, GestureListener listener) {
        this.context = context;
        this.listener = listener;

        mGestureDetector = new GestureDetector(context, this);

        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        layoutParams = ((Activity) context).getWindow().getAttributes();
        maxVolumn = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    public boolean setTouchEvent(MotionEvent motionEvent) {
        return mGestureDetector.onTouchEvent(motionEvent);
    }

    public void restartGesture() {
        mSwipeAction = SwipeAction.NONE;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        listener.onSingleTapUp();
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        if (p1X != motionEvent.getX() || p1Y != motionEvent.getY()) {
            p1X = motionEvent.getX();
            p1Y = motionEvent.getY();

            volumn = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

            if (layoutParams.screenBrightness < 0) {
                brightness = (float) android.provider.Settings.System.getInt(context.getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, 0) / 255;
            } else {
                brightness = layoutParams.screenBrightness;
            }

            mSwipeAction = SwipeAction.NONE;
            startTimeSeek = 0;
        }

        float x1 = motionEvent.getX();
        float y1 = motionEvent.getY();
        float x2 = motionEvent1.getX();
        float y2 = motionEvent1.getY();

        if (mSwipeAction == SwipeAction.NONE) {
            int swipeDirection;

            if (Math.abs(x2 - x1) < Math.abs(y2 - y1)) {
                if (x2 > x1) {
                    swipeDirection = SWIPE_TO_TOP;
                } else {
                    swipeDirection = SWIPE_TO_BOTTOM;
                }
            } else {
                if (y2 > y1) {
                    swipeDirection = SWIPE_TO_RIGHT;
                } else {
                    swipeDirection = SWIPE_TO_LEFT;
                }
            }

            if (swipeDirection == SWIPE_TO_TOP || swipeDirection == SWIPE_TO_BOTTOM) {
                if (x1 > ScreenUtils.getWidthScreen(context) / 2) {
                    //thay doi am luong
                    mSwipeAction = SwipeAction.CHANGE_VOLUMN;
                    changeVolumn(y1, y2);
                } else {
                    //thay doi do sang
                    mSwipeAction = SwipeAction.CHANGE_BRIGHTNESS;
                    changeBrightness(y1, y2);
                }
            } else {
                // seek
                mSwipeAction = SwipeAction.SEEK;
                listener.onStartGestureSeek();
                seek(x1, x2);
            }
        } else {
            if (mSwipeAction == SwipeAction.CHANGE_BRIGHTNESS) changeBrightness(y1, y2);
            else if (mSwipeAction == SwipeAction.CHANGE_VOLUMN) changeVolumn(y1, y2);
            else if (mSwipeAction == SwipeAction.SEEK) seek(x1, x2);
        }

        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    private void changeVolumn(float y1, float y2) {
        if (maxVolumn < MAX_PROGRESS_VOLUMN) MAX_PROGRESS_VOLUMN = maxVolumn;
        int baseVolumn = maxVolumn / MAX_PROGRESS_VOLUMN;

        int currentVolumn = volumn;

        if (y2 < y1) {
            currentVolumn += ((y1 - y2) / MIN_PIXEL_TRIGGER) * baseVolumn;
        } else {
            currentVolumn -= ((y2 - y1) / MIN_PIXEL_TRIGGER) * baseVolumn;
        }

        currentVolumn = currentVolumn >= 0 ? currentVolumn : 0;
        currentVolumn = currentVolumn > maxVolumn ? maxVolumn : currentVolumn;

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolumn, AudioManager.FLAG_SHOW_UI);

        listener.onChangeVolumn((currentVolumn / baseVolumn));
    }

    private void changeBrightness(float y1, float y2) {
        int currentBrightness = (int) (brightness * 100);

        currentBrightness += (y1 - y2) / 6;

        currentBrightness = currentBrightness >= 0 ? currentBrightness : 0;
        currentBrightness = currentBrightness > 100 ? 100 : currentBrightness;

        layoutParams.screenBrightness = (float) currentBrightness / 100;

        ((Activity) context).getWindow().setAttributes(layoutParams);

        listener.onChangeBrightness(currentBrightness);
    }

    private void seek(float x1, float x2) {
        int second = (int) ((x2 - x1) / NUMBER_PIXEL_PER_SECOND_SEEK) * 1000;
        mSwipeAction.setValue(second);

        listener.onSeek(startTimeSeek, second);
    }
}
