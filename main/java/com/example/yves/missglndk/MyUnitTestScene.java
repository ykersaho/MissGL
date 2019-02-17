package com.example.yves.missglndk;

import android.content.res.AssetManager;
import android.view.MotionEvent;

import java.io.IOException;

import static android.opengl.GLES10.GL_LIGHT0;
import static android.opengl.GLES10.GL_LIGHT1;

/**
 * Created by Yves_K1 on 19/06/2018.
 */

public class MyUnitTestScene extends MISScene {
    float [] gravity= {0.0f, -9.0f, 0.0f};
    float [] speed= {0.0f, 0.0f, -5.0f};
    float [] speed1= {6.0f, 0.0f, 0.0f};
    float [] campos = {0.0f, 0.0f, 5.0f};
    float [] camrot = {0.0f, 1.0f, 0.0f};
    float [] axis= {0.0f, 100.0f, 0.0f};
    float [] rcenter= {0.0f, 0.0f, 0.0f};

    MyUnitTestScene(AssetManager assetManager) throws IOException {
        addobject(new MISObject("ground", assetManager, "ground.obj", 100.0f, "tennis.jpg", 0.0f, -1.0f, -5.0f,1000000.0f,0.0f));
       addobject(new MISObject("ball1", assetManager, "spheresmall.obj", 1.0f / 10.0f, "ball.jpg", -6f, 2.0f, -5.0f, 2000.0f, 0.2f));
       //  addobject(new MISObject("BowlingPins0", assetManager, "BowlingPins.obj", 1.0f / 5.0f, "bowling_pin_TEX.jpg", 0.0f, 5.0f, -5.0f, 1000f, 0.4f));
        //addobject(new MISObject("BowlingPins1", assetManager, "BowlingPins.obj", 1.0f / 10.0f, "bowling_pin_TEX.jpg", 3.0f, 1.0f, -5.0f, 1000f, 0.4f));
        //     addobject(new MISObject("BowlingPins2", assetManager, "BowlingPins.obj", 1.0f / 10.0f, "bowling_pin_TEX.jpg", 1.5f, 1.0f, -4.0f, 1000f, 0.4f));
                addobject(new MISObject("cube", assetManager, "cube.obj", 10.0f, "tennis.jpg", 5.0f, 5.0f, -5.0f,1000000.0f,0.5f));
                //addobject(new MISObject("cube1", assetManager, "cube.obj", 1.0f, "tennis.jpg", 0.0f, 2.0f, -5.0f,10.0f,0.5f));
        //addobject(new MISObject("ball2", assetManager, "spheresmall.obj", 1.0f / 10.0f, "ball.jpg", 2.0f, 2.0f, -5.0f, 2000.0f, 0.6f));
        //addobject(new MISObject("ball3", assetManager, "ball.obj", 1.0f / 10.0f, "ball.jpg", 2.0f, 1.0f, -5.0f, 20.0f, 0.6f));
        addlight(new MISLight(GL_LIGHT0,0.0f, 1.0f, -2.0f));
        addlight(new MISLight(GL_LIGHT1,1.0f, 10.0f, -1.0f));
        getobject("ball1").posacceleration(gravity);
        //getobject("ball2").posacceleration(gravity);
        //getobject("BowlingPins0").posacceleration(gravity);
        //        getobject("BowlingPins1").posacceleration(gravity);
        //                getobject("BowlingPins2").posacceleration(gravity);

        //getobject("BowlingPins0").rotationangle(-20.0f);
        //getobject("BowlingPins0").rotationaxis(axis);
        // getobject("BowlingPins0").posspeed(speed1);
        //      getobject("cube").rotationangle(30.0f);
        //           getobject("cube").rotationaxis(axis);
        //getobject("cube").posacceleration(gravity);
        //getobject("cube").posspeed(speed1);
        // getobject("cube1").posacceleration(gravity);
        //        getobject("cube").rotcenter(rcenter);
        //        getobject("cube").rotspeed(30.0f);
        //        getobject("cube1").rotationaxis(axis);
        //        getobject("cube1").rotspeed(30.0f);
        getobject("ball1").posspeed(speed1);
        //getobject("ball2").posspeed(speed);
        camera.moveto(campos);
        //camera.rotationaxis(camrot);
        //camera.rotationangle(90);
        //           camera.rotspeed(30);
    }

    public boolean statemachine() {
        float [] campos = {0.0f, 0.0f, 5.0f};
        //       camera.positionspeed[0] = (getobject("BowlingPins0").position[0] - camera.position[0]) * 2.0f;
        //camera.positionspeed[0] = (getobject("ball1").position[0] - camera.position[0]) * 2.0f;
        campos[0] = getobject("ball1").position[0];
        camera.moveto(campos);
        return(true);
    }
}
