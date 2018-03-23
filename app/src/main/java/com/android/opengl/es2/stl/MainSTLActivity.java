package com.android.opengl.es2.stl;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.android.opengl.es2.R;

import java.util.ArrayList;
import java.util.List;

public class MainSTLActivity extends Activity implements View.OnTouchListener {

    private boolean supportsEs2;
    private GLSurfaceView glView;
    private StlGLRenderer glRenderer;

    private GestureDetector mGestureDetector;

    private List<String> stlNameList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        checkSupported();
        if (supportsEs2) {
            stlNameList.add(0,"Torso_0.10.stl");
            stlNameList.add(1,"HeadPitch_0.10.stl");
//            stlNameList.add(2,"LShoulderPitch_0.10.stl");
            stlNameList.add(2,"LShoulderRoll_0.10.stl");
            glView = new GLSurfaceView(this);
            //-----------------Gesture-----------------------------------------------//
            glView.setOnTouchListener(this);
            glView.setClickable(true);
            glView.setLongClickable(true);
            glView.setFocusable(true);
            mGestureDetector = new GestureDetector(this, new GestureListener());
            //-----------------------------------------------------------------------//
            glRenderer = new StlGLRenderer(this,stlNameList);
            glView.setRenderer(glRenderer);
            setContentView(glView);
        } else {
            setContentView(R.layout.activity_main);
            Toast.makeText(this, "当前设备不支持OpenGL ES 2.0!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return mGestureDetector.onTouchEvent(motionEvent);
    }

    public void rotate(float x,float y) {
        if (glView !=null){
            glRenderer.rotate(x,y);
            glView.invalidate();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (glView != null) {
            glView.onResume();
        }
    }

    private void checkSupported() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        supportsEs2 = configurationInfo.reqGlEsVersion >= 0x2000;

        boolean isEmulator = Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                && (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86"));
        supportsEs2 = supportsEs2 || isEmulator;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (glView != null) {
            glView.onPause();
        }
    }

    public class GestureListener implements GestureDetector.OnGestureListener {

        // 用户轻触触摸屏，由1个MotionEvent ACTION_DOWN触发
        public boolean onDown(MotionEvent e) {
            Log.e("MyGesture", "onDown:(" + e.getX() + "," + e.getY() + ")");
            return false;
        }
        /*
         * 用户轻触触摸屏，尚未松开或拖动，由一个1个MotionEvent ACTION_DOWN触发
         * 注意和onDown()的区别，强调的是没有松开或者拖动的状态
         *
         * 而onDown也是由一个MotionEventACTION_DOWN触发的，但是他没有任何限制，
         * 也就是说当用户点击的时候，首先MotionEventACTION_DOWN，onDown就会执行，
         * 如果在按下的瞬间没有松开或者是拖动的时候onShowPress就会执行，如果是按下的时间超过瞬间
         * （这块我也不太清楚瞬间的时间差是多少，一般情况下都会执行onShowPress），拖动了，就不执行onShowPress。
         */
        public void onShowPress(MotionEvent e) {
            Log.e("MyGesture", "onShowPress");
        }

        // 用户（轻触触摸屏后）松开，由一个1个MotionEvent ACTION_UP触发
        ///轻击一下屏幕，立刻抬起来，才会有这个触发
        //从名子也可以看出,一次单独的轻击抬起操作,当然,如果除了Down以外还有其它操作,那就不再算是Single操作了,所以这个事件 就不再响应
        public boolean onSingleTapUp(MotionEvent e) {
            Log.e("MyGesture", "onSingleTapUp");
            return true;
        }

        // 用户按下触摸屏，并拖动，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE触发
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            Log.e("MyGesture", "onScroll:+(" + e1.getX() + "," + e1.getY() + ")+(" + e2.getX() + "," + e2.getY() + ")");
            Log.e("MyGesture", "onScroll:" + (e2.getX() - e1.getX()) + "   " + distanceX);

            float dealtX =  e2.getX() - e1.getX();
            float dealtY =  e2.getY() - e1.getY();

            rotate(dealtX,dealtY);
            return true;
        }

        // 用户长按触摸屏，由多个MotionEvent ACTION_DOWN触发
        public void onLongPress(MotionEvent e) {
            Log.e("MyGesture", "onLongPress");
        }

        // 用户按下触摸屏、快速移动后松开，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE, 1个ACTION_UP触发
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.e("MyGesture", "onFling");
            return true;
        }
    }
}
