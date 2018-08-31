package com.example.yves_k1.mis;

import android.content.res.AssetManager;
import android.view.MotionEvent;

import java.io.IOException;

import static android.opengl.GLES10.GL_LIGHT0;
import static android.opengl.GLES10.GL_LIGHT1;

/**
 * Created by Yves_K1 on 19/06/2018.
 */

public class MyWallScene extends MISScene {
    touchhistory [] th = new touchhistory[20];
    touchhistory thi = new touchhistory();
    int thwriteindex = 0;
    int threadindex = 0;
    int state = 0;
    MISObject button = new MISObject("button");
    MISObject ball = new MISObject("ball");
    MISObject ground = new MISObject("ground");
    MISObject wall = new MISObject("wall");
    float [] gravity= {0.0f, -9.0f, 0.0f};
    float [] campos = {0.0f, 1.0f, 5.5f};
    float [] ballpos = {0.0f, 0.5f, 2.0f};
    float [] buttonpos= {-0.4f, 0.9f, -1.5f};
    float [] wallpos= {0.0f, 2.5f, -5.0f};
    float [] groundpos= {0.0f, 0.0f, 0.0f};
    MISLight light1 = new MISLight(GL_LIGHT0);
    MISLight light2 = new MISLight(GL_LIGHT1);
    float [] light1pos = {0.0f, 1.0f, -2.0f};
    float [] light2pos = {1.0f, 10.0f, -1.0f};

    private class touchhistory {
        int action;
        int nb;
        float x1=0.0f;
        float x2=0.0f;
        float y1=0.0f;
        float y2=0.0f;
        float t=0.0f;
    }

    MyWallScene(AssetManager assetManager) throws IOException {
        int i;
        for(i=0;i<th.length;i++){
            th[i] = new touchhistory();
        }

        button.loadobj(assetManager, "button.obj", 1.0f/5.0f);
        button.moveto(buttonpos);
        button.loadtexture(assetManager, "button.png" );
        button.weight(0);
        ball.loadobj(assetManager, "spheresmall.obj", 1.0f/50.0f);
        ball.loadtexture(assetManager, "ball.jpg" );
        ball.moveto(ballpos);
        ball.elasticity(0.7f);
        ball.weight(20);
        ground.loadobj(assetManager, "ground.obj", 10.0f);
        ground.loadtexture(assetManager, "tennis.jpg" );
        ground.moveto(groundpos);
        ground.weight(1000000);
        wall.loadobj(assetManager, "wall.obj", 10.0f);
        wall.loadtexture(assetManager, "wall.jpg" );
        wall.weight(1000000);
        wall.moveto(wallpos);
        addobject((MISObject)ball);
        addobject((MISObject)ground);
        addobject((MISObject)wall);
        addobject((MISObject)button);
        light1.moveto(light1pos);
        addlight(light1);
        light2.moveto(light2pos);
        addlight(light2);
        camera.moveto(campos);
    }

    public void statemachine() {
        float [] posspeed= {0.0f, 0.0f, 0.0f};
        float [] zero= {0.0f, 0.0f, 0.0f};
        float [] dir = {0.0f, 0.0f, 0.0f};
        float dx;
        float dy;
        float dz;
        float dt;
        float speed;
        float l;

        camera.positionspeed[0] = (ball.position[0] - camera.position[0])*2.0f;

        if(threadindex != thwriteindex) { // touch event in the queue
            switch (th[threadindex].action) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                case MotionEvent.ACTION_POINTER_UP:
                    pickpointer[0] = th[threadindex].x1;
                    pickpointer[1] = th[threadindex].y1;
                    thi.action=th[threadindex].action;
                    thi.nb=th[threadindex].nb;
                    thi.x1=th[threadindex].x1;
                    thi.x2=th[threadindex].x2;
                    thi.y1=th[threadindex].y1;
                    thi.y2=th[threadindex].y2;
                    thi.t=th[threadindex].t;
                    break;
                case MotionEvent.ACTION_UP:
                    dx = th[threadindex].x1 - thi.x1;
                    dy = th[threadindex].y1 - thi.y1;
                    l = (float)(Math.sqrt(dx*dx+dy*dy));
                    dz = (float)(-2.0f);
                    dt = th[threadindex].t - thi.t;
                    speed = (float)(1000000000.0f/dt/500.0f);
                    posspeed[0]=speed*dx;
                    posspeed[1]=-speed*dy;
                    posspeed[2]=speed*dz*l;
                    ball.posacceleration(gravity);
                    ball.posspeed(posspeed);
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
            }
            threadindex++;
            if(threadindex >= th.length){
                threadindex=0;
            }
        }

        if(button.picked) {
            ball.moveto(ballpos);
            ball.posspeed(zero);
            ball.rotspeed(zero);
            ball.posacceleration(zero);
            camera.moveto(campos);
            camera.posspeed(zero);
        }
    }

    public boolean onTouchEvent(MotionEvent e) {
        int i;
        th[thwriteindex].action = e.getAction() & MotionEvent.ACTION_MASK;
        th[thwriteindex].nb = e.getPointerCount();
        th[thwriteindex].x1 = e.getX();
        th[thwriteindex].y1 = e.getY();
        th[thwriteindex].t= System.nanoTime();
        if(e.getPointerCount() > 1) {
            th[thwriteindex].x2 = e.getX(1);
            th[thwriteindex].y2 = e.getY(1);
        }
        else{
            th[thwriteindex].x2 = 0;
            th[thwriteindex].y2 = 0;
        }
        i = thwriteindex+1;
        if(i >= th.length)
            i= 0;
        if(i == threadindex) // if missed event in main loop we don't care
            thwriteindex=thwriteindex;
        if(i != threadindex) // if missed event in main loop we don't care
            thwriteindex=i;
        return(true);
    }
}
