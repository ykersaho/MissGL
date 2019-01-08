package com.example.yves_k1.mis;

import android.content.res.AssetManager;
import android.view.MotionEvent;

import java.io.IOException;

import static android.opengl.GLES10.GL_LIGHT0;
import static android.opengl.GLES10.GL_LIGHT1;

/**
 * Created by Yves_K1 on 19/06/2018.
 */

public class MyCollisionScene extends MISScene {
    touchhistory thi = new touchhistory();
    int state = 0;
    MISObject wall = new MISObject("wall");
    MISObject button = new MISObject("button");
    MISObject cube1 = new MISObject("cube1");
    MISObject cube2 = new MISObject("cube2");
    MISObject ground = new MISObject("ground");
    float [] zero= {0.0f, 0.0f, 0.0f};
    float [] gravity= {0.0f, -9.0f, 0.0f};
    float [] campos = {0.0f, 1.0f, 5.5f};
    float [] cube1pos = {0.0f, 2.5f, 2.0f};
    float [] cube1rotaxis = {0.0f, 0.0f, 1.0f};
    float [] cube1rotcenter = {0.0f, 1.5f,2.0f};
    float cube1rotangle = 0.0f;
    float cube1rotspeed = 0.0f;
    float [] cube2pos = {0.0f, 0.5f, 2.0f};
    float [] buttonpos= {-0.4f, 0.9f, -1.5f};
    float [] groundpos= {0.0f, 0.0f, 0.0f};
    float [] wallpos= {0.0f, 2.5f, -5.0f};
    MISLight light1 = new MISLight(GL_LIGHT0,0.0f, 1.0f, -2.0f);
    MISLight light2 = new MISLight(GL_LIGHT1,1.0f, 10.0f, -1.0f);

    MyCollisionScene(AssetManager assetManager) throws IOException {
        button.loadobj(assetManager, "button.obj", 1.0f/5.0f);
        button.moveto(buttonpos);
        button.loadtexture(assetManager, "button.png" );
        button.weight(0);
        cube1.loadobj(assetManager, "cube.obj", 1.0f/1.0f);
        cube1.loadtexture(assetManager, "ball.jpg" );
        cube1.rotationaxis(cube1rotaxis);
        cube1.rotcenter(cube1rotcenter);
        cube1.rotspeed(cube1rotspeed);
        cube1.rotationangle(cube1rotangle);
        cube1.moveto(cube1pos);
        cube1.posacceleration(gravity);
        cube1.elasticity(0.7f);
        cube1.weight(20);
        cube2.loadobj(assetManager, "cube.obj", 1.0f/1.0f);
        cube2.loadtexture(assetManager, "ball.jpg" );
        cube2.moveto(cube2pos);
        cube2.posacceleration(gravity);
        cube2.elasticity(0.7f);
        cube2.weight(20);
        ground.loadobj(assetManager, "ground.obj", 10.0f);
        ground.loadtexture(assetManager, "tennis.jpg" );
        ground.moveto(groundpos);
        ground.weight(1000000);
        wall.loadobj(assetManager, "wall.obj", 10.0f);
        wall.loadtexture(assetManager, "wall.jpg" );
        wall.weight(1000000);
        wall.moveto(wallpos);
        addobject((MISObject)cube1);
        addobject((MISObject)cube2);
        addobject((MISObject)ground);
        addobject((MISObject)wall);
        addobject((MISObject)button);
        addlight(light1);
        addlight(light2);
        camera.moveto(campos);
    }

    @Override
    public boolean statemachine() {
        touchhistory myth = new touchhistory();
        float [] posspeed= {0.0f, 0.0f, 0.0f};
        float [] zero= {0.0f, 0.0f, 0.0f};
        float [] dir = {0.0f, 0.0f, 0.0f};
        float dx;
        float dy;
        float dz;
        float dt;
        float speed;
        float l;

        if(gettouchevent(myth)) { // touch event in the queue
            switch (myth.action) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                case MotionEvent.ACTION_POINTER_UP:
                    pickpointer[0] = myth.x1;
                    pickpointer[1] = myth.y1;
                    thi.action=myth.action;
                    thi.nb=myth.nb;
                    thi.x1=myth.x1;
                    thi.x2=myth.x2;
                    thi.y1=myth.y1;
                    thi.y2=myth.y2;
                    thi.t=myth.t;
                    break;
                case MotionEvent.ACTION_UP:
                    dx = myth.x1 - thi.x1;
                    dy = myth.y1 - thi.y1;
                    l = (float)(Math.sqrt(dx*dx+dy*dy));
                    dz = (float)(-2.0f);
                    dt = myth.t - thi.t;
                    speed = (float)(1000000000.0f/dt/500.0f);
                    posspeed[0]=speed*dx;
                    posspeed[1]=-speed*dy;
                    posspeed[2]=speed*dz*l;
                    cube1.posacceleration(gravity);
                    cube1.posspeed(posspeed);
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
            }
        }

        if(button.picked) {
            cube1.moveto(cube1pos);
            //cube1.rotateto(cube1rot);
            cube1.posspeed(zero);
            //cube1.rotspeed(zero);
            cube1.posacceleration(gravity);
            cube2.moveto(cube2pos);
            //cube2.rotateto(zero);
            cube2.posspeed(zero);
            //cube2.rotspeed(zero);
            cube2.posacceleration(gravity);
            camera.moveto(campos);
            camera.posspeed(zero);
        }
        return(true);
    }
}
