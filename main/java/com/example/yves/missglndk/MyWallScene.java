package com.example.yves.missglndk;

import android.content.res.AssetManager;
import android.view.MotionEvent;

import java.io.IOException;

import static android.opengl.GLES10.GL_LIGHT0;
import static android.opengl.GLES10.GL_LIGHT1;

/**
 * Created by Yves_K1 on 19/06/2018.
 */

public class MyWallScene extends MISScene {
    MISObject activeobject = null;
    touchhistory thi = new touchhistory();
    float [] gravity= {0.0f, -9.0f, 0.0f};
    float [] campos = {0.0f, 0.0f, 3.0f};
    float [] ballpos = {0.0f, 0.5f, 2.0f};

    MyWallScene(AssetManager assetManager) throws IOException {
        addobject(new MISObject("ball1", assetManager, "spheresmall.obj", 1.0f / 50.0f, "ball.jpg", 0.1f, 4.5f, 1.0f, 20.0f, 0.7f));
        addobject(new MISObject("ball2", assetManager, "spheresmall.obj", 1.0f / 50.0f, "ball.jpg", 0.2f, 4.0f, 1.0f, 20.0f, 0.7f));
        addobject(new MISObject("ball3", assetManager, "spheresmall.obj", 1.0f / 50.0f, "ball.jpg", 0.3f, 3.5f, 1.0f, 20.0f, 0.7f));
        addobject(new MISObject("ball4", assetManager, "spheresmall.obj", 1.0f / 30.0f, "ball.jpg", 0.3f, 3.0f, 1.0f, 20.0f, 0.7f));
        addobject(new MISObject("ball5", assetManager, "spheresmall.obj", 1.0f / 50.0f, "ball.jpg", 0.3f, 2.5f, 1.0f, 20.0f, 0.7f));
        addobject(new MISObject("ball6", assetManager, "spheresmall.obj", 1.0f / 50.0f, "ball.jpg", 0.3f, 2.0f, 1.0f, 20.0f, 0.7f));
        addobject(new MISObject("ball7", assetManager, "spheresmall.obj", 1.0f / 50.0f, "ball.jpg", 0.3f, 1.5f, 1.0f, 20.0f, 0.7f));
        addobject(new MISObject("ball8", assetManager, "spheresmall.obj", 1.0f / 50.0f, "ball.jpg", 0.3f, 1.0f, 1.0f, 20.0f, 0.7f));
        addobject(new MISObject("ball9", assetManager, "spheresmall.obj", 1.0f / 50.0f, "ball.jpg", 0.3f, 0.5f, 1.0f, 20.0f, 0.7f));
        addobject(new MISObject("ground", assetManager, "ground.obj", 10.0f, "tennis.jpg", 0.0f, -2.0f, 0.0f,1000000.0f,0.0f));
        addobject(new MISObject("wall", assetManager, "wall.obj", 10.0f, "wall.jpg", 0.0f, 0.0f, -5.0f,1000000.0f,0.0f));
        addobject(new MISObject("button", assetManager, "button.obj", 1.0f/5.0f, "button.png", -0.4f, 0.9f, -1.5f,0.0f,0.0f));
        addlight(new MISLight(GL_LIGHT0,0.0f, 1.0f, -2.0f));
        addlight(new MISLight(GL_LIGHT1,1.0f, 10.0f, -1.0f));
        getobject("ball1").posacceleration(gravity);
        getobject("ball2").posacceleration(gravity);
        getobject("ball3").posacceleration(gravity);
        getobject("ball4").posacceleration(gravity);
        getobject("ball5").posacceleration(gravity);
        getobject("ball6").posacceleration(gravity);
        getobject("ball7").posacceleration(gravity);
        getobject("ball8").posacceleration(gravity);
        getobject("ball9").posacceleration(gravity);
        camera.moveto(campos);
    }

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
        MISObject object = getpickedobject();
        MISObject button = getobject("button");

        if(object != null) {
            activeobject=object;
        }
        if(activeobject != null) {
            camera.positionspeed[0] = (activeobject.position[0] - camera.position[0]) * 2.0f;
        }

        if(gettouchevent(myth)) { // touch event in the queue
            switch (myth.action) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                case MotionEvent.ACTION_POINTER_UP:
                    pickpointer[0] = myth.x1;
                    pickpointer[1] = myth.y1;
                    thi.action = myth.action;
                    thi.nb = myth.nb;
                    thi.x1 = myth.x1;
                    thi.x2 = myth.x2;
                    thi.y1 = myth.y1;
                    thi.y2 = myth.y2;
                    thi.t = myth.t;
                    break;
                case MotionEvent.ACTION_UP:
                    dx = myth.x1 - thi.x1;
                    dy = myth.y1 - thi.y1;
                    l = (float) (Math.sqrt(dx * dx + dy * dy));
                    dz = (float) (-2.0f);
                    dt = myth.t - thi.t;
                    if (dt == 0) break;
                    speed = (float) (1000000000.0f / dt / 500.0f);
                    posspeed[0] = speed * dx;
                    posspeed[1] = -speed * dy;
                    posspeed[2] = speed * dz * l;
                    if (activeobject != null) {
                            //activeobject.posacceleration(gravity);
                            activeobject.posspeed(posspeed);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    pickpointer[0] = myth.x1;
                    pickpointer[1] = myth.y1;
                    break;
            }
        }

        if(button.picked) {
            if(activeobject != null) {
                activeobject.moveto(ballpos);
                activeobject.posspeed(zero);
                activeobject.rotspeed(0);
                //activeobject.posacceleration(zero);
            }
            camera.moveto(campos);
            camera.posspeed(zero);
        }
        return(true);
    }
}
