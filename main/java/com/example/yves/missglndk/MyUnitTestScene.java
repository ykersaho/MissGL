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
    float [] raxis= {0f, 200.0f, 0.0f};
    float [] speed= {10f, 0.0f, 0.0f};
    float [] speed1= {0.0f, 0.0f, -20.0f};
    float [] speed2= {10.0f, 0.0f, 0.0f};
    float [] campos = {0.0f, 0.0f, 10.0f};
    float [] camrot = {-1.0f, 0.0f, 0.0f};
    float [] rot = {1.0f, 0.0f, 1.0f};

    MyUnitTestScene(AssetManager assetManager) throws IOException {
        test11(assetManager);
    }

    public void test1(AssetManager assetManager) throws IOException {
        addobject(new MISObject("ground", assetManager, "ground.obj", 100.0f, "tennis.jpg", 0.0f, -1.0f, -5.0f,1000000.0f,0.0f));
        addobject(new MISObject("ball1", assetManager, "sphere.obj", 1.0f / 10f, "ball.jpg", -10.0f, 4.0f, 0.0f, 2000.0f, 0.3f));
        getobject("ball1").posacceleration(gravity);
        getobject("ball1").posspeed(speed);
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
        addobject(new MISObject("ball1", assetManager, "spheresmall.obj", 1.0f / 8.0f, "ball.jpg", -5f, 0.0f, 0.0f, 4000.0f, 0.2f));
        addobject(new MISObject("ball2", assetManager, "spheresmall.obj", 1.0f / 8.0f, "ball.jpg", 5f, 0.0f, 0.0f, 4000.0f, 0.8f));
        addlight(new MISLight(GL_LIGHT0,0.0f, 1.0f, -2.0f));
        addlight(new MISLight(GL_LIGHT1,1.0f, 10.0f, -1.0f));
        getobject("ball1").posspeed(speed);
        camera.moveto(campos);
    }

    public void test6(AssetManager assetManager) throws IOException {
        addobject(new MISObject("ground", assetManager, "ground.obj", 100.0f, "tennis.jpg", 0.0f, -1.0f, -5.0f,1000000.0f,0.0f));
        addobject(new MISObject("ball1", assetManager, "sphere.obj", 1.0f / 20.0f, "bowl.png", 1.5f, 2.0f, 20.0f, 4000.0f, 0.2f));
        addobject(new MISObject("BowlingPins0", assetManager, "BowlingPins.obj", 1.0f / 10.0f, "bowling_pin_TEX.jpg", 0.0f, 1.0f, -5.0f, 1000f, 0.6f));
        addobject(new MISObject("BowlingPins1", assetManager, "BowlingPins.obj", 1.0f / 10.0f, "bowling_pin_TEX.jpg", 2.0f, 1.0f, -5.0f, 1000f, 0.6f));
        addobject(new MISObject("BowlingPins2", assetManager, "BowlingPins.obj", 1.0f / 10.0f, "bowling_pin_TEX.jpg", 4.0f, 1.0f, -5.0f, 1000f, 0.6f));
        addobject(new MISObject("BowlingPins3", assetManager, "BowlingPins.obj", 1.0f / 10.0f, "bowling_pin_TEX.jpg", 1.0f, 1.0f, -7.0f, 1000f, 0.6f));
        addobject(new MISObject("BowlingPins4", assetManager, "BowlingPins.obj", 1.0f / 10.0f, "bowling_pin_TEX.jpg", 3.0f, 1.0f, -7.0f, 1000f, 0.6f));
        addobject(new MISObject("BowlingPins5", assetManager, "BowlingPins.obj", 1.0f / 10.0f, "bowling_pin_TEX.jpg", 5.0f, 1.0f, -7.0f, 1000f, 0.6f));
        addlight(new MISLight(GL_LIGHT0,0.0f, 1.0f, -2.0f));
        addlight(new MISLight(GL_LIGHT1,1.0f, 10.0f, -1.0f));
        getobject("ball1").posacceleration(gravity);
        getobject("BowlingPins0").posacceleration(gravity);
        getobject("BowlingPins1").posacceleration(gravity);
        getobject("BowlingPins2").posacceleration(gravity);
        getobject("BowlingPins3").posacceleration(gravity);
        getobject("BowlingPins4").posacceleration(gravity);
        getobject("BowlingPins5").posacceleration(gravity);
        getobject("ball1").posspeed(speed1);
        getobject("ball1").rotspeed(200);
        getobject("ball1").rotationaxis(raxis);
        camera.moveto(campos);
    }

    public void test7(AssetManager assetManager) throws IOException {
        addobject(new MISObject("ground", assetManager, "ground.obj", 100.0f, "tennis.jpg", 0.0f, -1.0f, -5.0f,1000000.0f,0.0f));
        addobject(new MISObject("ball1", assetManager, "spheresmall.obj", 1.0f / 2.0f, "ball.jpg", 0f, 5.0f, 0.0f, 4000.0f, 0.7f));
        addlight(new MISLight(GL_LIGHT0,0.0f, 1.0f, -2.0f));
        addlight(new MISLight(GL_LIGHT1,1.0f, 10.0f, -1.0f));
        getobject("ball1").posacceleration(gravity);
        camera.moveto(campos);
    }

    public void test8(AssetManager assetManager) throws IOException {
        addobject(new MISObject("ground", assetManager, "ground.obj", 100.0f, "tennis.jpg", 0.0f, -1.0f, -5.0f,1000000.0f,0.0f));
        addobject(new MISObject("BowlingPins0", assetManager, "BowlingPins.obj", 1.0f / 10.0f, "bowling_pin_TEX.jpg", 0.0f, 3.0f, 0.0f, 1000f, 0.6f));
        addlight(new MISLight(GL_LIGHT0,0.0f, 1.0f, -2.0f));
        addlight(new MISLight(GL_LIGHT1,1.0f, 10.0f, -1.0f));
        getobject("BowlingPins0").posacceleration(gravity);
        getobject("BowlingPins0").rotationaxis(raxis);
        getobject("BowlingPins0").rotationangle(40);
        camera.moveto(campos);
    }

    public void test9(AssetManager assetManager) throws IOException {
        addobject(new MISObject("ground", assetManager, "ground.obj", 100.0f, "tennis.jpg", 0.0f, -1.0f, -5.0f,1000000.0f,0.0f));
        addobject(new MISObject("ball1", assetManager, "spheresmall.obj", 1.0f / 8.0f, "bowl.png", 1.5f, 2.0f, 15.0f, 4000.0f, 0.2f));
        addlight(new MISLight(GL_LIGHT0,0.0f, 1.0f, -2.0f));
        addlight(new MISLight(GL_LIGHT1,1.0f, 10.0f, -1.0f));
        getobject("ball1").posacceleration(gravity);
        getobject("ball1").posspeed(speed1);
        getobject("ball1").rotspeed(120);
        getobject("ball1").rotationaxis(raxis);
        camera.moveto(campos);
    }


    public void test10(AssetManager assetManager) throws IOException {
        addobject(new MISObject("ground", assetManager, "ground.obj", 100.0f, "tennis.jpg", 0.0f, -1.0f, -5.0f,1000000.0f,0.0f));
        addobject(new MISObject("ball1", assetManager, "sphere.obj", 1.0f / 20.0f, "bowl.png", 1.5f, 2.0f, 10.0f, 1000.0f, 0.2f));
        addobject(new MISObject("ball2", assetManager, "sphere.obj", 1.0f / 20.0f, "bowling_pin_TEX.jpg", 0.0f, 0.0f, -5.0f, 1000f, 0.6f));
        addobject(new MISObject("ball3", assetManager, "sphere.obj", 1.0f / 20.0f, "bowling_pin_TEX.jpg", 2.0f, 0.0f, -5.0f, 1000f, 0.6f));
        addobject(new MISObject("ball4", assetManager, "sphere.obj", 1.0f / 20.0f, "bowling_pin_TEX.jpg", 4.0f, 0.0f, -5.0f, 1000f, 0.6f));
        addobject(new MISObject("ball5", assetManager, "sphere.obj", 1.0f / 20.0f, "bowling_pin_TEX.jpg", 1.0f, 0.0f, -7.0f, 1000f, 0.6f));
        addobject(new MISObject("ball6", assetManager, "sphere.obj", 1.0f / 20.0f, "bowling_pin_TEX.jpg", 3.0f, 0.0f, -7.0f, 1000f, 0.6f));
        addobject(new MISObject("ball7", assetManager, "sphere.obj", 1.0f / 20.0f, "bowling_pin_TEX.jpg", 5.0f, 0.0f, -7.0f, 1000f, 0.6f));
        addlight(new MISLight(GL_LIGHT0,0.0f, 1.0f, -2.0f));
        addlight(new MISLight(GL_LIGHT1,1.0f, 10.0f, -1.0f));
        getobject("ball1").posacceleration(gravity);
        getobject("ball2").posacceleration(gravity);
        getobject("ball3").posacceleration(gravity);
        getobject("ball4").posacceleration(gravity);
        getobject("ball5").posacceleration(gravity);
        getobject("ball6").posacceleration(gravity);
        getobject("ball7").posacceleration(gravity);
        getobject("ball1").posspeed(speed1);
        getobject("ball1").rotspeed(200);
        getobject("ball1").rotationaxis(raxis);
        camera.moveto(campos);
    }

    public void test11(AssetManager assetManager) throws IOException {
        float [] speed1= {0.0f, 6.0f, -14.0f};
        addobject(new MISObject("ground", assetManager, "ground.obj", 100.0f, "ground.jpg", 0.0f, -1.0f, -5.0f,1000000.0f,0.0f));
        addobject(new MISObject("ball1", assetManager, "spheresmall.obj", 1.0f / 10.0f, "petanque1.png", 3f, 0.0f, 10.0f, 1000.0f, 0.3f));
        addobject(new MISObject("ball2", assetManager, "spheresmall.obj", 1.0f / 30.0f, "cochonnet.jpg", 0.0f, -0.6f, -10.0f, 100f, 0.5f));
        addobject(new MISObject("ball3", assetManager, "spheresmall.obj", 1.0f / 10.0f, "petanque3.jpg", 2f, 0.0f, -10.0f, 1000f, 0.3f));
        addobject(new MISObject("ball4", assetManager, "spheresmall.obj", 1.0f / 10.0f, "petanque3.jpg", 4f, 0.0f, -10.0f, 1000f, 0.3f));
        addlight(new MISLight(GL_LIGHT0,0.0f, 10.0f, -20.0f));
        addlight(new MISLight(GL_LIGHT1,1.0f, 100.0f, -1.0f));
        getobject("ball1").posacceleration(gravity);
        getobject("ball2").posacceleration(gravity);
        getobject("ball3").posacceleration(gravity);
        getobject("ball4").posacceleration(gravity);
        getobject("ball1").posspeed(speed1);
        getobject("ball1").rotspeed(200);
        getobject("ball1").rotationaxis(raxis);
        camera.moveto(campos);
    }


    public boolean statemachine() {
        float [] campos = {0.0f, 8.0f, 15.0f};

//        campos[0] = getobject("cube1").position[0];
        //campos[2] = getobject("ball1").position[2]+10.0f;
        //campos[1] = getobject("ball1").position[1]+2;
        camera.moveto(campos);
        camera.rotationaxis(camrot);
        camera.rotationangle(30);
        return(true);
    }
}
