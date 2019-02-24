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
    float [] speed= {10f, 0.0f, 0.0f};
    float [] speed1= {0.0f, 0.0f, -25.0f};
    float [] speed2= {10.0f, 0.0f, 0.0f};
    float [] campos = {0.0f, 0.0f, 10.0f};
    float [] rot = {1.0f, 0.0f, 1.0f};

    MyUnitTestScene(AssetManager assetManager) throws IOException {
        test5(assetManager);
    }

    public void test1(AssetManager assetManager) throws IOException {
        addobject(new MISObject("ground", assetManager, "ground.obj", 100.0f, "tennis.jpg", 0.0f, -1.0f, -5.0f,1000000.0f,0.0f));
        addobject(new MISObject("ball1", assetManager, "spheresmall.obj", 1.0f / 5.0f, "ball.jpg", 0.0f, 2.0f, 10.0f, 2000.0f, 0.3f));
        getobject("ball1").posacceleration(gravity);
        getobject("ball1").posspeed(speed1);
        camera.moveto(campos);
    }

    public void test2(AssetManager assetManager) throws IOException {
        addobject(new MISObject("ground", assetManager, "ground.obj", 100.0f, "tennis.jpg", 0.0f, -0.6f, -5.0f,1000000.0f,0.0f));
        addobject(new MISObject("ball1", assetManager, "spheresmall.obj", 1.0f / 10.0f, "ball.jpg", -5.0f, 2.0f, 0.0f, 2000.0f, 0.3f));
        addobject(new MISObject("cube", assetManager, "cube.obj", 5.0f, "tennis.jpg", 5.0f, 3.5f, 0.0f,1000000.0f,0.5f));
        getobject("ball1").posacceleration(gravity);
        getobject("ball1").posspeed(speed);
        camera.moveto(campos);
    }

    public void test3(AssetManager assetManager) throws IOException {
        addobject(new MISObject("ground", assetManager, "ground.obj", 100.0f, "tennis.jpg", 0.0f, -1.0f, -5.0f,1000000.0f,0.0f));
        addobject(new MISObject("cube1", assetManager, "cube.obj", 2.0f, "tennis.jpg", 5.0f, 3.5f, 0.0f,100.0f,0.5f));
//        addobject(new MISObject("cube2", assetManager, "cube.obj", 2.0f, "tennis.jpg", 0.0f, 5.5f, 0.0f,100.0f,0.5f));
        getobject("cube1").posacceleration(gravity);
        getobject("cube1").posspeed(speed);
        getobject("cube1").rotationaxis(rot);
        getobject("cube1").rotationangle(40);

//        getobject("cube2").posacceleration(gravity);
        camera.moveto(campos);
    }

    public void test4(AssetManager assetManager) throws IOException {
        addobject(new MISObject("ground", assetManager, "ground.obj", 100.0f, "tennis.jpg", 0.0f, -1.0f, -5.0f,1000000.0f,0.0f));
        addobject(new MISObject("cube1", assetManager, "cube.obj", 1.0f, "tennis.jpg", 5.0f, 3.5f, 0.0f,100.0f,0.1f));
        addobject(new MISObject("cube2", assetManager, "cube.obj", 1.0f, "tennis.jpg", 5.5f, 4.5f, 0.0f,100.0f,0.1f));
        addobject(new MISObject("cube3", assetManager, "cube.obj", 1.0f, "tennis.jpg", 4.5f, 5.5f, 0.0f,100.0f,0.1f));
        getobject("cube1").posacceleration(gravity);
        getobject("cube2").posacceleration(gravity);
        getobject("cube3").posacceleration(gravity);
        getobject("cube1").rotationaxis(rot);
        getobject("cube1").rotationangle(40);
        camera.moveto(campos);
    }


    public void test5(AssetManager assetManager) throws IOException {
        addobject(new MISObject("ground", assetManager, "ground.obj", 100.0f, "tennis.jpg", 0.0f, -1.0f, -5.0f,1000000.0f,0.0f));
        addobject(new MISObject("ball1", assetManager, "spheresmall.obj", 1.0f / 4.0f, "ball.jpg", -0.0f, 2.0f, 10.0f, 2000.0f, 0.4f));
        addobject(new MISObject("BowlingPins0", assetManager, "BowlingPins.obj", 1.0f / 5.0f, "bowling_pin_TEX.jpg", 0.0f, 3.0f, -5.0f, 1000f, 0.4f));
        addobject(new MISObject("BowlingPins1", assetManager, "BowlingPins.obj", 1.0f / 5.0f, "bowling_pin_TEX.jpg", 3.0f, 3.0f, -5.0f, 1000f, 0.4f));
        addobject(new MISObject("BowlingPins2", assetManager, "BowlingPins.obj", 1.0f / 5.0f, "bowling_pin_TEX.jpg", 1.5f, 3.0f, -8.0f, 1000f, 0.4f));
        addlight(new MISLight(GL_LIGHT0,0.0f, 1.0f, -2.0f));
        addlight(new MISLight(GL_LIGHT1,1.0f, 10.0f, -1.0f));
        getobject("ball1").posacceleration(gravity);
        getobject("BowlingPins0").posacceleration(gravity);
        getobject("BowlingPins1").posacceleration(gravity);
        getobject("BowlingPins2").posacceleration(gravity);
        getobject("ball1").posspeed(speed1);
        camera.moveto(campos);
    }

    public boolean statemachine() {
        float [] campos = {0.0f, 0.0f, 18.0f};

//        campos[0] = getobject("cube1").position[0];
        //campos[0] = getobject("ball1").position[0];
        camera.moveto(campos);
        return(true);
    }
}
