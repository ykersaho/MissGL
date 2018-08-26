package com.example.yves_k1.mis;

import android.content.res.AssetManager;
import android.view.MotionEvent;

import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by yves on 05/08/18.
 */

public class MyDemoScene  extends MISScene {
    float[] gravity = {0.0f, -9.0f, 0.0f};  // gravity 9 m/s2
    float[] zero = {0.0f, 0.0f, 0.0f};
    float[] cameraposition = {0.0f, 1.0f, 6.0f};   // camera postion in meter
    float[] camerarotation = {0.0f, 0.0f, 0.0f};   // camera rotation in degrees
    float[] camerapositionspeed = {0.0f, 1.0f, .0f};  // camera rotationegrees
    float[] camerarotationspeed = {-5.0f, 0.0f, 0.0f};  // camera rotation speed degrees/s
    float[] sphereposition = {1.0f, 3.0f, -4.0f};
    float[] spherespeed = {-1.0f, 0.0f, 0.0f};
    float[] sphererotationspeed = {20.0f, 60.0f, 0.0f};
    int sphereweight = 500;  // weight in gram
    float sphereelasticity = 0.7f;   // medium elasticity
    float[] cubeposition = {-1.0f, 1.0f, -4.0f};
    float[] cubespeed = {1.0f, 0.0f, 0.0f};
    float[] cuberotationspeed = {70.0f, 30.0f, 10.0f};
    float cubeelasticity = 0.8f;   // very elastic
    int cubeweight = 800;  // weight in gram
    float[] groundposition = {0.0f, 0.0f, -5.0f};
    MISObject sphere = new MISObject("sphere");
    MISObject cube = new MISObject("cube");
    MISObject ground = new MISObject("ground");

    MyDemoScene(GL10 gl, AssetManager assetManager) throws IOException {
        ground.loadobj(assetManager, "ground.obj", 20.0f);
        ground.loadtexture(assetManager, "ground.jpg");
        ground.weight(1000000); // earth is very heavy ! never move.
        ground.moveto(groundposition);
        sphere.loadobj(assetManager, "sphere.obj", 1.0f / 40.0f);
        sphere.loadtexture(assetManager, "sphere.gif");
        sphere.moveto(sphereposition);
        sphere.posspeed(spherespeed);
        sphere.rotspeed(sphererotationspeed);
        sphere.weight(sphereweight);
        sphere.posacceleration(gravity); // apply gravity else the object will float
        sphere.elasticity(sphereelasticity);
        cube.loadobj(assetManager, "cube.obj", 1.0f/2.0f);
        cube.loadtexture(assetManager, "cube.png");
        cube.moveto(cubeposition);
        cube.posspeed(cubespeed);
        cube.posacceleration(gravity);
        cube.elasticity(cubeelasticity);
        cube.rotspeed(cuberotationspeed);
        cube.weight(cubeweight);
        camera.moveto(cameraposition);
        camera.rotateto(camerarotation);
        camera.rotspeed(camerarotationspeed);
        camera.posspeed(camerapositionspeed);
        addobject((MISObject)sphere);
        addobject((MISObject)cube);
        addobject((MISObject)ground);
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