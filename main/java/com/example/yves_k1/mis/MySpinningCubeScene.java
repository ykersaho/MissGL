package com.example.yves_k1.mis;

import android.content.res.AssetManager;
import android.view.MotionEvent;

import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by yves on 05/08/18.
 */

public class MySpinningCubeScene extends MISScene {
    float[] cameraposition = {0.0f, 1.0f, 6.0f};   // camera postion in meter
    float[] camerarotation = {0.0f, 0.0f, 0.0f};   // camera rotation in degrees
    float[] cubeposition = {0.0f, 0.0f, -2.0f};
    float[] cuberotationspeed = {70.0f, 30.0f, 10.0f};
    MISObject cube = new MISObject("cube");

    MySpinningCubeScene(GL10 gl, AssetManager assetManager) throws IOException {
        cube.loadobj(assetManager, "cube.obj", 1.0f/2.0f);
        cube.loadtexture(gl, assetManager, "cube.png");
        cube.moveto(cubeposition);
        cube.rotspeed(cuberotationspeed);
        camera.moveto(cameraposition);
        addobject((MISObject)cube);
        // that's it !
    }

    // main scene state machine
    public void statemachine() {
        // not used for this demo
    }

    // touch screen event management
    public boolean onTouchEvent(MotionEvent e) {
        // touch event not used for this demo
        return (false);
    }
}