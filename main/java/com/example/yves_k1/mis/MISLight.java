package com.example.yves_k1.mis;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLES30;

import static android.opengl.GLES10.GL_AMBIENT;
import static android.opengl.GLES10.GL_DIFFUSE;
import static android.opengl.GLES10.GL_LIGHT0;
import static android.opengl.GLES10.GL_LIGHTING;
import static android.opengl.GLES10.GL_POSITION;
import static android.opengl.GLES10.GL_SPECULAR;
import static android.opengl.GLES10.glEnable;
import static android.opengl.GLES10.glLightfv;


/**
 * Created by Yves_K1 on 19/06/2018.
 */

public class MISLight extends MISObject {
    int lid = GL_LIGHT0;

    public MISLight(int id) {
        super("light");lid = id;
    }

    public void move() {
        float light_ambient[] = {2.0f, 2.0f, 2.0f, 1.0f};
        float light_diffuse[] = {2.0f, 2.0f, 2.0f, 1.0f};
        float light_specular[] = {4.0f, 4.0f, 4.0f, 1.0f};
        FloatBuffer buffer = null;

        ByteBuffer byteBuf = ByteBuffer.allocateDirect(4 * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        buffer = byteBuf.asFloatBuffer();

        updateposition();
        float light_position[] = {position[0], position[1], position[2], 0.0f};

        glEnable(GL_LIGHTING);    // Active l'éclairage
        glEnable(lid);
        buffer.put(light_ambient);
        buffer.position(0);
        glLightfv(lid, GL_AMBIENT, buffer);
        buffer.put(light_diffuse);
        buffer.position(0);
        glLightfv(lid, GL_DIFFUSE, buffer);
        buffer.put(light_specular);
        buffer.position(0);
        glLightfv(lid, GL_SPECULAR, buffer);
        buffer.put(light_position);
        buffer.position(0);
        glLightfv(lid, GL_POSITION, buffer);
    }
}