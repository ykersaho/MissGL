package com.example.yves_k1.mis;

import android.opengl.GLES30;
import android.opengl.Matrix;

import java.util.ArrayList;

import static android.opengl.GLES10.glLoadIdentity;

/**
 * Created by Yves_K1 on 19/06/2018.
 */

public class MISScene {
    float viewratio = 1.0f;
    ArrayList<MISObject> objects;
    ArrayList<MISLight> lights;
    MISCamera camera;
    MISShader shader;
    float pickpointer[] = {0.0f, 0.0f};
    float[] mProjectionMatrix = new float[16];

    MISScene() {
        objects = new ArrayList<MISObject>();
        lights = new ArrayList<MISLight>();
        camera = new MISCamera();
        shader = new MISShader();
    }

    public void viewratio(float r) {
        viewratio = r;
    }

    public void addobject(MISObject obj){
        objects.add(obj);
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
        for(i=0;i<objects.size();i++) {
            objects.get(i).draw(shader, camera.mModelMatrix, mProjectionMatrix);
        }
        MISCollision colission = new MISCollision(this);
        MISPicking picking = new MISPicking(this);
    }
}
