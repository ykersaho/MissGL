package com.example.yves_k1.mis;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import static javax.microedition.khronos.opengles.GL10.GL_AMBIENT;
import static javax.microedition.khronos.opengles.GL10.GL_DIFFUSE;
import static javax.microedition.khronos.opengles.GL10.GL_LIGHT0;
import static javax.microedition.khronos.opengles.GL10.GL_LIGHTING;
import static javax.microedition.khronos.opengles.GL10.GL_LIGHT_MODEL_AMBIENT;
import static javax.microedition.khronos.opengles.GL10.GL_POSITION;
import static javax.microedition.khronos.opengles.GL10.GL_SPECULAR;

/**
 * Created by Yves_K1 on 19/06/2018.
 */

public class MISLight extends MISObject {
    int lid = GL_LIGHT0;

    public MISLight(int id) {
        super("light");lid = id;
    }

    public void move(GL10 gl) {
        float light_ambient[] = {2.0f, 2.0f, 2.0f, 1.0f};
        float light_diffuse[] = {2.0f, 2.0f, 2.0f, 1.0f};
        float light_specular[] = {4.0f, 4.0f, 4.0f, 1.0f};
        FloatBuffer buffer = null;

        ByteBuffer byteBuf = ByteBuffer.allocateDirect(4 * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        buffer = byteBuf.asFloatBuffer();

        updateposition();
        float light_position[] = {position[0], position[1], position[2], 0.0f};

        gl.glEnable(GL_LIGHTING);    // Active l'éclairage
        gl.glEnable(lid);
        buffer.put(light_ambient);
        buffer.position(0);
        gl.glLightfv(lid, GL_AMBIENT, buffer);
        buffer.put(light_diffuse);
        buffer.position(0);
        gl.glLightfv(lid, GL_DIFFUSE, buffer);
        buffer.put(light_specular);
        buffer.position(0);
        gl.glLightfv(lid, GL_SPECULAR, buffer);
        buffer.put(light_position);
        buffer.position(0);
        gl.glLightfv(lid, GL_POSITION, buffer);
    }
}