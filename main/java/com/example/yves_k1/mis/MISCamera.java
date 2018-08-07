package com.example.yves_k1.mis;

import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLU.gluLookAt;

/**
 * Created by Yves_K1 on 19/06/2018.
 */

public class MISCamera extends MISObject {
        public MISCamera(){
            super("camera");
        }
        public void move(GL10 gl) {
        updateposition();
        gl.glRotatef(-rotation[0], 1.0f, 0.0f, 0.0f);
        gl.glRotatef(-rotation[1], 0.0f, 1.0f, 0.0f);
        gl.glRotatef(-rotation[2], 0.0f, 0.0f, 1.0f);
        gl.glTranslatef(-position[0], -position[1], -position[2]);
    }
}
