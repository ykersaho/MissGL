package com.example.yves_k1.mis;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

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

    public void draw(GL10 gl) {
        int i;
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        for(i=0;i<lights.size();i++) {
            lights.get(i).move(gl);
        }
        camera.move(gl);
        for(i=0;i<objects.size();i++) {
            objects.get(i).draw(gl);
        }
        MISCollision colission = new MISCollision(this);
        MISPicking picking = new MISPicking(this);
    }
}
