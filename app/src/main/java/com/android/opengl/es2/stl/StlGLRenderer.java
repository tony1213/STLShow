package com.android.opengl.es2.stl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;

import com.android.opengl.es2.stl.bean.Model;
import com.android.opengl.es2.stl.utils.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class StlGLRenderer implements GLSurfaceView.Renderer {

    private Model model;
    private Point mCenterPoint;
    private Point eye = new Point(0, 0, -3);
    private Point up = new Point(0, 1, 0);
    private Point center = new Point(0, 0, 0);
    private float mScalef = 1;
    private float mDegree = 0;
    private float mXaxis = 0;
    private float mYaxis = 0;
    private float mZaxis = 0;

    //---------------------light start-----------------------------//
    float[] ambient = {0.9f, 0.9f, 0.9f, 1.0f,};
    float[] diffuse = {0.5f, 0.5f, 0.5f, 1.0f,};
    float[] specular = {1.0f, 1.0f, 1.0f, 1.0f,};
    float[] lightPosition = {0.5f, 0.5f, 0.5f, 0.0f,};
    //---------------------light end-------------------------------//
    //--------------------material start---------------------------//
    float[] materialAmb = {0.4f, 0.4f, 1.0f, 1.0f};
    float[] materialDiff = {0.0f, 0.0f, 1.0f, 1.0f};//漫反射设置蓝色
    float[] materialSpec = {1.0f, 0.5f, 0.0f, 1.0f};
    //---------------------material end----------------------------//

    private List<Model> modelList = new ArrayList<>();

    public StlGLRenderer(Context context, List<String> list) {
        STLReader reader = new STLReader();
        try {
            for (int i = 0; i < list.size(); i++) {
                model = new STLReader().parserBinStlInAssets(context, list.get(i));
                modelList.add(model);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glEnable(GL10.GL_DEPTH_TEST); // 启用深度缓存
        gl.glClearDepthf(1.0f); // 设置深度缓存值
        gl.glDepthFunc(GL10.GL_LEQUAL); // 设置深度缓存比较函数
        gl.glShadeModel(GL10.GL_SMOOTH);// 设置阴影模式GL_SMOOTH
        float r = model.getR();
        //r是半径，不是直径，因此用0.5/r可以算出放缩比例
        mScalef = 0.5f / r;
//        mCenterPoint = model.getCentrePoint();
        openLight(gl);
//        enableMaterial(gl);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // 设置OpenGL场景的大小,(0,0)表示窗口内部视口的左下角，(width, height)指定了视口的大小
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION); // 设置投影矩阵
        gl.glLoadIdentity(); // 设置矩阵为单位矩阵，相当于重置矩阵
        GLU.gluPerspective(gl, 45.0f, ((float) width) / height, 1f, 100f);// 设置透视范围
        //以下两句声明，以后所有的变换都是针对模型(即我们绘制的图形)
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        /*// 清除屏幕和深度缓存
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();// 重置当前的模型观察矩阵
        //眼睛对着原点看
        GLU.gluLookAt(gl, eye.x, eye.y, eye.z, center.x, center.y, center.z, up.x, up.y, up.z);
        //为了能有立体感觉，通过改变mDegree值，让模型不断旋转
        gl.glRotatef(mDegree, mXaxis, mYaxis, mZaxis);
        //将模型放缩到View刚好装下
        gl.glScalef(mScalef, mScalef, mScalef);
        //把模型移动到原点
        gl.glTranslatef(-mCenterPoint.x, -mCenterPoint.y, -mCenterPoint.z);
        //===================begin==============================//
        //允许给每个顶点设置法向量
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        // 允许设置顶点
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        // 允许设置颜色
        //设置法向量数据源
        gl.glNormalPointer(GL10.GL_FLOAT, 0, model.getVnormBuffer());
        // 设置三角形顶点数据源
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, model.getVertBuffer());
        // 绘制三角形
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, model.getFacetCount() * 3);
        // 取消顶点设置
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        //取消法向量设置
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        //=====================end============================//*/


        // 清除屏幕和深度缓存
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();// 重置当前的模型观察矩阵
        //眼睛对着原点看
        GLU.gluLookAt(gl, eye.x, eye.y, eye.z, center.x, center.y, center.z, up.x, up.y, up.z);
        gl.glScalef(mScalef, mScalef, mScalef);
        //为了能有立体感觉，通过改变mDegree值，让模型不断旋转
        gl.glRotatef(mDegree, mXaxis, mYaxis, mZaxis);
        //允许给每个顶点设置法向量
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        // 允许设置顶点
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        for (int i = 0; i < modelList.size(); i++) {
            Point point = modelList.get(i).getCentrePoint();
            if (i == 0) {
                Log.e("Point","point0===>("+point.x+","+point.y+","+point.z+")");
                gl.glTranslatef(point.x, point.y, point.z);
            } else if (i == 1) {
                Log.e("Point","point1===>("+point.x+","+point.y+","+point.z+")");
                gl.glTranslatef(point.x, point.y, point.z+0.6f);
            } else if (i == 2) {
                Log.e("Point","point2===>("+point.x+","+point.y+","+point.z+")");
                gl.glTranslatef(point.x-0.2f, point.y+0.9f, point.z-0.2f);
            }
            //设置法向量数据源
            gl.glNormalPointer(GL10.GL_FLOAT, 0, modelList.get(i).getVnormBuffer());
            // 设置三角形顶点数据源
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, modelList.get(i).getVertBuffer());
            // 绘制三角形
            gl.glDrawArrays(GL10.GL_TRIANGLES, 0, modelList.get(i).getFacetCount() * 3);
        }
        //===================begin==============================//
        // 取消顶点设置
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        //取消法向量设置
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        //=====================end============================//
    }

    public void rotate(float delatX, float delatY) {
        if (delatX >= delatY) {
            mDegree = delatX;
            mXaxis = 0;
            mYaxis = 1;
        } else {
            mDegree = delatY;
            mXaxis = 1;
            mYaxis = 0;
        }
    }

    public void openLight(GL10 gl) {
        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_LIGHT0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, Util.floatToBuffer(ambient));
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, Util.floatToBuffer(diffuse));
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, Util.floatToBuffer(specular));
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, Util.floatToBuffer(lightPosition));
    }

    public void enableMaterial(GL10 gl) {
        //材料对环境光的反射情况
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, Util.floatToBuffer(materialAmb));
        //散射光的反射情况
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, Util.floatToBuffer(materialDiff));
        //镜面光的反射情况
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, Util.floatToBuffer(materialSpec));
    }
}