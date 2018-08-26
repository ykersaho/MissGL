package com.example.yves_k1.mis;

import android.opengl.GLES30;

import java.util.ArrayList;

import static android.opengl.GLES10.glLoadIdentity;

/**
 * Created by Yves_K1 on 19/06/2018.
 */

public class MISScene {
    ArrayList<MISObject> objects;
    ArrayList<MISLight> lights;
    MISCamera camera;
    float pickpointer[] = {0.0f, 0.0f};

    MISScene() {
        objects = new ArrayList<MISObject>();
        lights = new ArrayList<MISLight>();
        camera = new MISCamera();
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
        glLoadIdentity();
        for(i=0;i<lights.size();i++) {
            lights.get(i).move();
        }
        camera.move();
        for(i=0;i<objects.size();i++) {
            objects.get(i).draw();
        }
        MISCollision colission = new MISCollision(this);
        MISPicking picking = new MISPicking(this);
    }
}
