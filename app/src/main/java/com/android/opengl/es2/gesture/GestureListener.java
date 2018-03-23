package com.android.opengl.es2.gesture;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;


public class GestureListener implements GestureDetector.OnGestureListener {

    // 用户轻触触摸屏，由1个MotionEvent ACTION_DOWN触发
    public boolean onDown(MotionEvent e) {
        Log.e("MyGesture", "onDown:("+e.getX()+","+e.getY()+")"+"===>"+e.getPointerCount());
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
        Log.e("MyGesture","onScroll:+("+e1.getX()+","+e1.getY()+")+("+e2.getX()+","+e2.getY()+")");
//        Log.e("MyGesture", "onScroll:" + (e2.getX() - e1.getX()) + "   " + distanceX);
        float dealtX =  e2.getX() - e1.getX();
        float dealtY =  e2.getY() - e1.getY();
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
