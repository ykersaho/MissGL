package com.example.yves_k1.mis;

import android.content.res.AssetManager;
import android.opengl.GLU;
import android.util.Log;
import android.view.MotionEvent;

import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayDeque;
import java.util.Queue;

import static android.content.ContentValues.TAG;
import static android.opengl.GLES10.GL_LIGHT0;
import static android.opengl.GLES10.GL_LIGHT1;
import static java.lang.Math.PI;
import static java.lang.Math.asin;
import static java.lang.Math.sqrt;

/**
 * Created by Yves_K1 on 19/06/2018.
 */

public class MyScene extends MISScene {
    float [] arrowrot= {0.0f, 90.0f, 0.0f};
    touchhistory [] th = new touchhistory[20];
    touchhistory thi = new touchhistory();
    int thwriteindex = 0;
    int threadindex = 0;
    int state = 0;
    float [] currentpos= {0.0f, -9.0f, 0.0f};
    float [] currentrot= {0.0f, -9.0f, 0.0f};
    float [] gravity= {0.0f, -9.0f, 0.0f};
    float [] zero= {0.0f, 0.0f, 0.0f};
    float [] campos = {0.0f, 2.0f, 4.0f};
    float [] camrot = {0.0f, 0.0f, 0.0f};
    float [] camrotspeed = {-10.0f, 0.0f, 0.0f};
    float [] camposspeed = {0.0f, 0.0f, -1.0f};
    float [] pos1 = {0.0f, 1.0f, 1.0f};
    float [] rot1 = {10.0f, 20.0f, 0.0f};
    float [] spos1 = {0.0f, -0.0f, 0.0f};
    float [] srot1 = {100.0f, 20.0f, 00.0f};
    float [] pos2 = {1.0f, 1.0f, 0.0f};
    float [] rot2 = {0.0f, 0.0f, 0.0f};
    float [] spos2 = {0.0f, 0.0f, -17.0f};
    float [] srot2 = {400.0f, 00.0f, 500.0f};
    float [] pos3 = {1.0f, 0.5f, -8.0f};
    float [] posground = {0.0f, 0.0f, -5.0f};
    float [] apos = {0.0f, 0.0f, -2.0f};
    float [] arot = {20.0f, 0.0f, 0.0f};
    MISObject arrow = new MISObject("arrow");
    MISObject sphere1 = new MISObject("sphere1");
    MISObject sphere2 = new MISObject("sphere2");
    MISObject sphere3 = new MISObject("sphere3");
    MISObject sphere4 = new MISObject("sphere4");
    MISObject sphere5 = new MISObject("sphere5");
    MISObject sphere6 = new MISObject("sphere6");
    MISObject button1 = new MISObject("button1");
    MISObject button2 = new MISObject("button2");
    MISObject ground = new MISObject("ground");
    float [] groundb = {0.0f, -1000000.0f, 0.0f};
    float [] posbutton1= {-0.4f, 0.9f, -2.0f};
    float [] rotbutton1= {0.0f, 0.0f, 0.0f};
    float [] posbutton2= {0.4f, 0.9f, -2.0f};
    float [] rotbutton2= {0.0f, 0.0f, 0.0f};
    float [] posleftwall = {-4.0f, -1.5f, -5.0f};
    float [] rotleftwall = {0.0f, 0.0f, -90.0f};
    float [] posrightwall = {4.0f, -1.5f, -5.0f};
    float [] rotrightwall = {0.0f, 0.0f, 90.0f};
    float [] posfrontwall = {0.0f, -1.5f, -15.0f};
    float [] rotfrontwall = {0.0f, 90.0f, 90.0f};
    float [] srotfrontwall = {0.0f, 0.0f, 0.0f};
    MISObject leftwall = new MISObject("leftwall");
    MISObject rightwall = new MISObject("rigthwall");
    MISObject frontwall = new MISObject("frontwall");
    MISLight light1 = new MISLight(GL_LIGHT0);
    MISLight light2 = new MISLight(GL_LIGHT1);
    float [] light1pos = {2.0f, 10.0f, -1.0f};
    float [] light2pos = {-2.0f, 10.0f, -1.0f};
    boolean translation;
    MISObject pickedsphere=sphere1;

    private class touchhistory {
        int action;
        int nb;
        float x1=0.0f;
        float x2=0.0f;
        float y1=0.0f;
        float y2=0.0f;
        float t=0.0f;
    }

    MyScene(AssetManager assetManager) throws IOException {
        int i;
        for(i=0;i<th.length;i++){
            th[i] = new touchhistory();
        }

        arrow.loadobj(assetManager, "face.obj", 1.0f/5.0f);
        arrow.moveto(apos);
        arrow.loadtexture(assetManager, "arrow.png" );
        arrow.rotateto(arot);
        arrow.weight(0);
        button1.loadobj(assetManager, "face.obj", 1.0f/5.0f);
        button1.moveto(posbutton1);
        button1.loadtexture(assetManager, "camera.png" );
        button1.rotspeed(rotbutton1);
        button1.weight(0);
        button2.loadobj(assetManager, "face.obj", 1.0f/5.0f);
        button2.loadtexture(assetManager, "ball.png" );
        button2.moveto(posbutton2);
        button2.rotspeed(rotbutton2);
        button2.weight(0);
        sphere1.loadobj(assetManager, "spheresmall.obj", 1.0f/30.0f);
        sphere1.loadtexture(assetManager, "boule.gif" );
        sphere1.moveto(pos1);
        sphere1.rotateto(rot1);
        sphere1.posspeed(spos1);
        sphere1.rotspeed(srot1);
        sphere1.weight(500);
        sphere2.loadobj(assetManager, "spheresmall.obj" , 1.0f/30.0f);
        sphere2.loadtexture(assetManager, "boule1.jpg" );
        sphere2.moveto(pos2);
        sphere2.rotateto(rot2);
        sphere2.posspeed(spos2);
        sphere2.rotspeed(srot2);
        sphere2.weight(700);
        sphere3.loadobj(assetManager, "spheresmall.obj" , 1.0f/90.0f);
        sphere3.loadtexture(assetManager, "cochonnet.jpg" );
        sphere3.moveto(pos3);
        sphere3.weight(20);
        ground.setbarycenter(groundb);
        ground.loadobj(assetManager, "ground.obj", 20.0f);
        ground.loadtexture(assetManager, "ground1.jpg" );
        ground.weight(1000000);
        ground.moveto(posground);
        leftwall.loadobj(assetManager, "bordure.obj", 20.0f);
        leftwall.loadtexture(assetManager, "wood.jpg" );
        leftwall.moveto(posleftwall);
        leftwall.rotateto(rotleftwall);
        leftwall.weight(1000000);
        rightwall.loadobj(assetManager, "bordure.obj", 20.0f);
        rightwall.loadtexture(assetManager, "wood.jpg" );
        rightwall.moveto(posrightwall);
        rightwall.rotateto(rotrightwall);
        rightwall.weight(1000000);
        frontwall.loadobj(assetManager, "bordure.obj", 20.0f);
        frontwall.loadtexture(assetManager, "wood.jpg" );
        frontwall.moveto(posfrontwall);
        frontwall.rotateto(rotfrontwall);
        frontwall.rotspeed(srotfrontwall);
        frontwall.weight(1000000);
        sphere1.posacceleration(gravity);
        sphere2.posacceleration(gravity);
        sphere3.posacceleration(gravity);
        addobject((MISObject)sphere1);
        addobject((MISObject)sphere2);
        addobject((MISObject)sphere3);
        addobject((MISObject)ground);
        addobject((MISObject)leftwall);
        addobject((MISObject)rightwall);
        addobject((MISObject)frontwall);
        addobject((MISObject)button1);
        addobject((MISObject)button2);
        addobject((MISObject)arrow);
        light1.moveto(light1pos);
        addlight(light1);
        light2.moveto(light2pos);
        addlight(light2);
        camera.moveto(campos);
        camera.rotateto(camrot);
        camera.rotspeed(camrotspeed);
        camera.posspeed(camposspeed);
    }

    public void statemachine() {
        float [] rot= {0.0f, 90.0f, 0.0f};
        float [] pos= {0.0f, 90.0f, 0.0f};
        float [] rotspeed= {0.0f, 0.0f, 0.0f};
        float [] posspeed= {0.0f, 0.0f, 0.0f};
        float [] zero= {0.0f, 0.0f, 0.0f};
        float dx;
        float dy;
        float dxi;
        float dyi;
        float dz;
        float dt;
        float speed;
        float l;
        MISObject o=null;
        if(sphere1.picked)
            o = sphere1;
        if(sphere2.picked)
            o = sphere2;
        if(sphere3.picked)
            o = sphere3;

        if(o != null) {
            if((o != pickedsphere) && (state == 1)) {
                currentpos[0] = o.position[0];
                currentpos[1] = o.position[1];
                currentpos[2] = o.position[2];
                currentrot[0] = o.rotation[0];
                currentrot[1] = o.rotation[1];
                currentrot[2] = o.rotation[2];
            }
            pickedsphere = o;
        }

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
                    if(state==0) {
                        currentpos[0]=camera.position[0];
                        currentpos[1]=camera.position[1];
                        currentpos[2]=camera.position[2];
                        currentrot[0]=camera.rotation[0];
                        currentrot[1]=camera.rotation[1];
                        currentrot[2]=camera.rotation[2];
                    }
                    else {
                        currentpos[0]=pickedsphere.position[0];
                        currentpos[1]=pickedsphere.position[1];
                        currentpos[2]=pickedsphere.position[2];
                        currentrot[0]=pickedsphere.rotation[0];
                        currentrot[1]=pickedsphere.rotation[1];
                        currentrot[2]=pickedsphere.rotation[2];
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    dx = th[threadindex].x1 - thi.x1;
                    dy = th[threadindex].y1 - thi.y1;
                    l = (float)(Math.sqrt(dx*dx+dy*dy));
                    dz = (float)(Math.sin(3.1415926*arrowrot[0]/180));
                    dt = th[threadindex].t - thi.t;
                    speed = (float)(1000000000.0f/dt/500.0f);
                    if(state==1){
                        posspeed[0]=speed*dx;
                        posspeed[1]=-speed*dy;
                        posspeed[2]=speed*dz*l;
                        pickedsphere.posacceleration(gravity);
                        pickedsphere.posspeed(posspeed);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(th[threadindex].nb>1) {
                        dx = th[threadindex].x2 - th[threadindex].x1;
                        dy = th[threadindex].y2 - th[threadindex].y1;
                        dxi = thi.x2 - thi.x1;
                        dyi = thi.y2 - thi.y1;
                        if(state == 1) {
                            arrowrot[0] = (dx-dxi) / 5;
                            arrowrot[1] = 0;
                            arrowrot[2] = 0;
                            arrow.rotateto(arrowrot);
                        }
                        else if(state==0) {
                            pos[0]=currentpos[0];
                            pos[1]=currentpos[1];
                            pos[2]=currentpos[2] - (dx-dxi) / 100;
                            camera.moveto(pos);
                        }
                    }
                    else
                    if(state==0) {
                        dx = th[threadindex].x1 - thi.x1;
                        dy = th[threadindex].y1 - thi.y1;
                        rot[0]=currentrot[0]+dy/10;
                        rot[1]=currentrot[1]+dx/10;
                        rot[2]=currentrot[2];
                        camera.rotateto(rot);
                    }
                    else
                    if(state==1) {
                        dx = th[threadindex].x1 - thi.x1;
                        dy = th[threadindex].y1 - thi.y1;
                        pos[0]= currentpos[0] + dx/500;
                        pos[1]= currentpos[1] - dy/500;
                        pos[2]= currentpos[2];
                        pickedsphere.moveto(pos);
                        pickedsphere.posacceleration(zero);
                        pickedsphere.posspeed(zero);
                        pickedsphere.posspeed(zero);
                    }
                    break;
            }
            threadindex++;
            if(threadindex >= th.length){
                threadindex=0;
            }
        }

        switch(state) {
            case 0 : // camera move
                if(button2.picked) {
                    button2.rotspeed(rot);
                    button1.rotspeed(zero);
                    button1.rotateto(zero);
                    button2.picked = false;
                    pickedsphere.moveto(pos1);
                    pickedsphere.rotateto(rot1);
                    pickedsphere.rotspeed(zero);
                    pickedsphere.posspeed(zero);
                    state=1;
                }
                if(button1.picked) {
                    button1.rotspeed(rot);
                    camera.moveto(campos);
                    camera.rotateto(camrot);
                    camera.rotspeed(zero);
                    camera.posspeed(zero);
                    button1.picked = false;
                }
                break;
            case 1 : // object move
                if(button1.picked) {
                    button1.rotspeed(rot);
                    button2.rotspeed(zero);
                    button2.rotateto(zero);
                    state=0;
                    camera.moveto(campos);
                    camera.rotateto(camrot);
                    camera.rotspeed(zero);
                    camera.posspeed(zero);
                    button1.picked = false;
                }
                if(button2.picked) {
                    button2.rotspeed(rot);
                    pickedsphere.moveto(pos1);
                    pickedsphere.rotateto(rot1);
                    pickedsphere.rotspeed(zero);
                    pickedsphere.posspeed(zero);
                    button2.picked = false;
                }
                break;
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
