package com.example.yves_k1.mis;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import android.opengl.GLES10;
import android.opengl.GLES11;
import android.opengl.GLES20;
import android.opengl.GLES30;

import static android.opengl.GLES10.GL_BLEND;
import static android.opengl.GLES10.GL_CW;
import static android.opengl.GLES10.GL_FLOAT;
import static android.opengl.GLES10.GL_LINEAR;
import static android.opengl.GLES10.GL_NEAREST;
import static android.opengl.GLES10.GL_NORMAL_ARRAY;
import static android.opengl.GLES10.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES10.GL_REPEAT;
import static android.opengl.GLES10.GL_SRC_ALPHA;
import static android.opengl.GLES10.GL_TEXTURE_2D;
import static android.opengl.GLES10.GL_TEXTURE_COORD_ARRAY;
import static android.opengl.GLES10.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES10.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES10.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES10.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES10.GL_TRIANGLES;
import static android.opengl.GLES10.GL_VERTEX_ARRAY;
import static android.opengl.GLES10.glBindTexture;
import static android.opengl.GLES10.glBlendFunc;
import static android.opengl.GLES10.glDisableClientState;
import static android.opengl.GLES10.glDrawElements;
import static android.opengl.GLES10.glEnable;
import static android.opengl.GLES10.glEnableClientState;
import static android.opengl.GLES10.glFrontFace;
import static android.opengl.GLES10.glGenTextures;
import static android.opengl.GLES10.glLoadIdentity;
import static android.opengl.GLES10.glNormalPointer;
import static android.opengl.GLES10.glPopMatrix;
import static android.opengl.GLES10.glPushMatrix;
import static android.opengl.GLES10.glRotatef;
import static android.opengl.GLES10.glTexCoordPointer;
import static android.opengl.GLES10.glTexParameterf;
import static android.opengl.GLES10.glTranslatef;
import static android.opengl.GLES10.glVertexPointer;
import static android.opengl.GLES11.GL_MODELVIEW_MATRIX;
import static android.opengl.GLES11.glGetFloatv;
import static android.opengl.GLES20.GL_UNSIGNED_INT;


/**
 * Created by Yves_K1 on 18/06/2018.
 */

public class MISObject {
    String name;
    boolean collision=false;
    boolean picked=false;
    float   elasticity = 0.6f;
    float[] barycenter = {0.0f, 0.0f, 0.0f, 1.0f};
    float[] mvbarycenter = new float[4];
    float[] vertices;
    float[] mvvertices;
    float[] centers;
    float[] mvcenters;
    float[] normals;
    float[] mvnormals;
    float[] ray;
    float bbray;
    float position[] = {0.0f, 0.0f, 0.0f};
    float rotation[] = {0.0f, 0.0f, 0.0f};
    float positionspeed[] = {0.0f, 0.0f, 0.0f};
    float rotationspeed[] = {0.0f, 0.0f, 0.0f};
    float positionacceleration[] = {0.0f, 0.0f, 0.0f};
    float rotationacceleration[] = {0.0f, 0.0f, 0.0f};
    FloatBuffer matrixBuffer = null;
    FloatBuffer vertexBuffer = null;
    FloatBuffer normalBuffer = null;
    FloatBuffer colorBuffer = null;
    IntBuffer indexBuffer = null;
    FloatBuffer textureBuffer = null;
    int[] texturesid = new int[1];
    Bitmap bitmap = null;
    float m;

    int nbtriangle;
    long t0;
    ArrayList<MISObject> objects = new ArrayList<MISObject>();

    public MISObject(String s){
        name = s;
    }
    void loadobj(AssetManager assetManager, String name, float scale) throws IOException {
        InputStream input = null;
        try {
            input = assetManager.open(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MISObjloader obj = new MISObjloader(input, scale);
        define(obj.vertices, obj.normals, obj.textures, obj.indices);
    }

    public void loadtexture(AssetManager assetManager, String name) {
        InputStream input = null;
        try {
            input = assetManager.open(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap = BitmapFactory.decodeStream(input);

        glGenTextures(1, texturesid, 0);
        glBindTexture(GL_TEXTURE_2D, texturesid[0]);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,
                GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER,
                GL_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S,
                GL_REPEAT);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T,
                GL_REPEAT);
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    void define(float [] vertices, float [] normals, float [] textures, int [] indices){

        float mat[] = new float[16];
        Matrix.setIdentityM(mat, 0);
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(16 * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        matrixBuffer = byteBuf.asFloatBuffer();
        matrixBuffer.put(mat);
        matrixBuffer.position(0);

        byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuf.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        byteBuf = ByteBuffer.allocateDirect(normals.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        normalBuffer = byteBuf.asFloatBuffer();
        normalBuffer.put(normals);
        normalBuffer.position(0);

        byteBuf = ByteBuffer.allocateDirect(textures.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuf.asFloatBuffer();
        textureBuffer.put(textures);
        textureBuffer.position(0);

        byteBuf = ByteBuffer.allocateDirect(indices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        indexBuffer = byteBuf.asIntBuffer();
        indexBuffer.put(indices);
        indexBuffer.position(0);
        nbtriangle = indices.length / 3;

        this.vertices = new float[12*nbtriangle];
        this.mvvertices = new float[12*nbtriangle];
        this.normals= new float[4*nbtriangle];
        this.mvnormals= new float[4*nbtriangle];
        this.centers= new float[4*nbtriangle];
        this.mvcenters = new float[4*nbtriangle];
        this.ray = new float[nbtriangle];
        // WARNING the provided model must have barycenter at (0,0,0)
        for(int i=0;i<nbtriangle;i++){
            this.centers[4*i] = (vertices[9*i]+vertices[9*i+3]+vertices[9*i+6]) / 3;
            this.centers[4*i+1] = (vertices[9*i+1]+vertices[9*i+4]+vertices[9*i+7]) / 3;
            this.centers[4*i+2] = (vertices[9*i+2]+vertices[9*i+5]+vertices[9*i+8]) / 3;
            this.centers[4*i+3] = 1.0f;
            this.normals[4*i] = normals[9*i]+normals[9*i+3]+normals[9*i+6];
            this.normals[4*i+1] = normals[9*i+1]+normals[9*i+4]+normals[9*i+7];
            this.normals[4*i+2] = normals[9*i+2]+normals[9*i+5]+normals[9*i+8];
            this.normals[4*i+3] = 0.0f;
            float dx1 = vertices[9*i]   - this.centers[4*i];
            float dy1 = vertices[9*i+1] - this.centers[4*i+1];
            float dz1 = vertices[9*i+2] - this.centers[4*i+2];
            float dx2 = vertices[9*i+3] - this.centers[4*i];
            float dy2 = vertices[9*i+4] - this.centers[4*i+1];
            float dz2 = vertices[9*i+5] - this.centers[4*i+2];
            float dx3 = vertices[9*i+6] - this.centers[4*i];
            float dy3 = vertices[9*i+7] - this.centers[4*i+1];
            float dz3 = vertices[9*i+8] - this.centers[4*i+2];
            float d1=dx1*dx1+dy1*dy1+dz1*dz1;
            float d2=dx2*dx2+dy2*dy2+dz2*dz2;
            float d3=dx3*dx3+dy3*dy3+dz3*dz3;
            float d=d1;
            if(d2>d1) d = d2;
            if(d3>d) d = d3;
            this.ray[i] = (float)Math.sqrt(d);
            this.vertices[12*i] = vertices[9*i];
            this.vertices[12*i+1] = vertices[9*i+1];
            this.vertices[12*i+2] = vertices[9*i+2];
            this.vertices[12*i+3] = 1.0f;
            this.vertices[12*i+4] = vertices[9*i+3];
            this.vertices[12*i+5] = vertices[9*i+4];
            this.vertices[12*i+6] = vertices[9*i+5];
            this.vertices[12*i+7] = 1.0f;
            this.vertices[12*i+8] = vertices[9*i+6];
            this.vertices[12*i+9] = vertices[9*i+7];
            this.vertices[12*i+10] = vertices[9*i+8];
            this.vertices[12*i+11] = 1.0f;
        }
        bbray = 0;
        // the barycenter may have been forced so use it
        for(int i=0;i<3*nbtriangle;i++) {
            float dx = vertices[3*i]-barycenter[0];
            float dy = vertices[3*i+1]-barycenter[1];
            float dz = vertices[3*i+2]-barycenter[2];
            float d = (float)Math.sqrt(dx*dx+dy*dy+dz*dz);
            if(d > bbray) bbray= d;
        }
        t0 = System.nanoTime();
    }

    void weight(int w){
        m=w;
    }

    void setcollisionstate(boolean c){
        collision=c;
    }

    void setbarycenter(float [] pos){
        barycenter[0] = pos[0];
        barycenter[1] = pos[1];
        barycenter[2] = pos[2];
        barycenter[3] = 1;
        t0 = System.nanoTime();
    }

    void elasticity(float el){
        elasticity = el;
    }

    void moveto(float [] pos){
        position[0] = pos[0];
        position[1] = pos[1];
        position[2] = pos[2];
        t0 = System.nanoTime();
    }

    void rotateto(float [] rot){
        rotation[0] = rot[0];
        rotation[1] = rot[1];
        rotation[2] = rot[2];
        t0 = System.nanoTime();
    }

    void posspeed(float [] pos){
        positionspeed[0] = pos[0];
        positionspeed[1] = pos[1];
        positionspeed[2] = pos[2];
    }

    void rotspeed(float [] rot){
        rotationspeed[0] = rot[0];
        rotationspeed[1] = rot[1];
        rotationspeed[2] = rot[2];
    }

    void posacceleration(float [] pos){
        positionacceleration[0] = pos[0];
        positionacceleration[1] = pos[1];
        positionacceleration[2] = pos[2];
    }

    void rotacceleration(float [] rot){
        rotationacceleration[0] = rot[0];
        rotationacceleration[1] = rot[1];
        rotationacceleration[2] = rot[2];
    }

    void updateposition(){
        long t1 = System.nanoTime();
        long dt = t1 - t0;
        if(dt > 20000000)
            dt = 20000000;   // CPU is too slow no real time
        t0=t1;
        position[0] += positionspeed[0]*dt/1000000000.0f;
        position[1] += positionspeed[1]*dt/1000000000.0f;
        position[2] += positionspeed[2]*dt/1000000000.0f;
        rotation[0] += rotationspeed[0]*dt/1000000000.0f;
        rotation[1] += rotationspeed[1]*dt/1000000000.0f;
        rotation[2] += rotationspeed[2]*dt/1000000000.0f;
        positionspeed[0] += positionacceleration[0] * dt / 1000000000.0f;
        positionspeed[1] += positionacceleration[1] * dt / 1000000000.0f;
        positionspeed[2] += positionacceleration[2] * dt / 1000000000.0f;
        rotationspeed[0] += rotationacceleration[0] * dt / 1000000000.0f;
        rotationspeed[1] += rotationacceleration[1] * dt / 1000000000.0f;
        rotationspeed[2] += rotationacceleration[2] * dt / 1000000000.0f;
    }

    public void draw() {
        updateposition();
        glPushMatrix();
        if(m==0)
            glLoadIdentity();
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, texturesid[0]);
        glTranslatef(position[0], position[1], position[2]);
        glRotatef(rotation[0], 1.0f, 0.0f, 0.0f);
        glRotatef(rotation[1], 0.0f, 1.0f, 0.0f);
        glRotatef(rotation[2], 0.0f, 0.0f, 1.0f);

        glFrontFace(GL_CW);


        glVertexPointer(3, GL_FLOAT, 0, vertexBuffer);
        glNormalPointer( GL_FLOAT, 0, normalBuffer);
        glTexCoordPointer(2, GL_FLOAT, 0, textureBuffer);

        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        glDrawElements(GL_TRIANGLES, nbtriangle*3, GL_UNSIGNED_INT, indexBuffer);

        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);
        glDisableClientState(GL_VERTEX_ARRAY);


        glLoadIdentity();
        glTranslatef(position[0], position[1], position[2]);
        glRotatef(rotation[0], 1.0f, 0.0f, 0.0f);
        glRotatef(rotation[1], 0.0f, 1.0f, 0.0f);
        glRotatef(rotation[2], 0.0f, 0.0f, 1.0f);
        float mat[] = new float[16];
        Matrix.setIdentityM(mat, 0);
        matrixBuffer.position(0);
        glGetFloatv(GL_MODELVIEW_MATRIX, matrixBuffer);
        matrixBuffer.get(mat);
        Matrix.multiplyMV(mvbarycenter, 0, mat, 0, barycenter, 0);
        for (int i=0;i<nbtriangle;i++){
            Matrix.multiplyMV(mvcenters, i*4, mat, 0, centers, i*4);
            Matrix.multiplyMV(mvnormals, i*4, mat, 0, normals, i*4);
            Matrix.multiplyMV(mvvertices, i*12, mat, 0, vertices, i*12);
            Matrix.multiplyMV(mvvertices, i*12+4, mat, 0, vertices, i*12+4);
            Matrix.multiplyMV(mvvertices, i*12+8, mat, 0, vertices, i*12+8);
        }
        glPopMatrix();
    }
}
