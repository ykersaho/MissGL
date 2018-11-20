package com.example.yves_k1.mis;

import android.opengl.GLES30;
import android.opengl.Matrix;
import android.os.AsyncTask;
import android.os.Process;
import android.view.MotionEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

import static android.opengl.GLES10.glLoadIdentity;
import static android.os.Process.THREAD_PRIORITY_BACKGROUND;
import static android.os.Process.setThreadPriority;
import static java.lang.Thread.*;
import static java.lang.Thread.sleep;

/**
 * Created by Yves_K1 on 19/06/2018.
 */

public class MISScene {
    int state = 0;
    float viewratio = 1.0f;
    ArrayList<MISObject> objects;
    ArrayList<MISLight> lights;
    MISCamera camera;
    MISShader shader;
    float pickpointer[] = {0.0f, 0.0f};
    float[] mProjectionMatrix = new float[16];
    touchhistory[] m_th = new touchhistory[20];
    int thwriteindex = 0;
    int threadindex = 0;
    MISCollision collision = new MISCollision(this);
    MISPicking picking = new MISPicking(this);
    HashMap<String, MISObject> hmap = new HashMap<String, MISObject>();

    public class touchhistory {
        int action;
        int nb;
        float x1=0.0f;
        float x2=0.0f;
        float y1=0.0f;
        float y2=0.0f;
        float t=0.0f;
    }

    MISScene() {
        objects = new ArrayList<MISObject>();
        lights = new ArrayList<MISLight>();
        camera = new MISCamera();
        shader = new MISShader();
        int i;
        for(i=0;i<m_th.length;i++){
            m_th[i] = new MISScene.touchhistory();
        }
    }

    public void viewratio(float r) {
        viewratio = r;
    }

    public void addobject(MISObject obj){
        objects.add(obj);
        hmap.put(obj.name, obj);
    }

    public MISObject getobject(String s){
        return(hmap.get(s));
    }

    public MISObject getpickedobject(){
        for (int i = 0; i < objects.size(); i++) {
            if(     (objects.get(i).m > 0)&&
                    (objects.get(i).m < 1000000)&&
                    (objects.get(i).picked == true)) {
                return (objects.get(i));
            }
        }
        return(null);
    }

    public void addlight(MISLight light){
        lights.add(light);
    }

    public void draw() {
        int i;
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
        Matrix.setIdentityM(mProjectionMatrix, 0);
        Matrix.frustumM(mProjectionMatrix, 0, -viewratio, viewratio, -1.0f, 1.0f, 1.0f, 20.0f);
        for(i=0;i<lights.size();i++) {
            lights.get(i).move();
        }
        for(i=0;i<lights.size();i++) {
            lights.get(i).move();
        }
        camera.move();
        for (i = 0; i < objects.size(); i++) {
            objects.get(i).draw(shader, camera.mViewMatrix, mProjectionMatrix);
        }
        collision.run();
        picking.run();
    }

    public boolean statemachine() {
        return(true);
    }

    public boolean gettouchevent(touchhistory th) {
        if (threadindex != thwriteindex) { // touch event in the queue
            th.action = m_th[threadindex].action;
            th.nb = m_th[threadindex].nb;
            th.x1 = m_th[threadindex].x1;
            th.y1 = m_th[threadindex].y1;
            th.t = m_th[threadindex].t;
            th.x2 = m_th[thwriteindex].x2;
            th.y2 = m_th[thwriteindex].y2;
            threadindex++;
            if (threadindex >= m_th.length) {
                threadindex = 0;
            }
            return(true);
        }
        else
            return(false);
    }

    public boolean onTouchEvent(MotionEvent e) {
        int i;
        m_th[thwriteindex].action = e.getAction() & MotionEvent.ACTION_MASK;
        m_th[thwriteindex].nb = e.getPointerCount();
        m_th[thwriteindex].x1 = e.getX();
        m_th[thwriteindex].y1 = e.getY();
        m_th[thwriteindex].t= System.nanoTime();
        if(e.getPointerCount() > 1) {
            m_th[thwriteindex].x2 = e.getX(1);
            m_th[thwriteindex].y2 = e.getY(1);
        }
        else{
            m_th[thwriteindex].x2 = 0;
            m_th[thwriteindex].y2 = 0;
        }
        i = thwriteindex+1;
        if(i >= m_th.length)
            i= 0;
        if(i == threadindex) // if missed event in main loop we don't care
            thwriteindex=thwriteindex;
        if(i != threadindex) // if missed event in main loop we don't care
            thwriteindex=i;
        return(true);
    }
    }
