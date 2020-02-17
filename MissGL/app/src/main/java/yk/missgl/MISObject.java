package yk.missgl;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.Matrix;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_CW;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_NEAREST;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_REPEAT;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glTexParameterf;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLUtils.texImage2D;
import static java.lang.Thread.sleep;

/**
 * Created by Yves_K1 on 18/06/2018.
 */

public class MISObject {
    float[] mModelMatrix = new float[16];
    float[] mMotionMatrix = new float[16];
    String name;
    boolean collision=false;
    boolean picked=false;
    float   elasticity = 0.6f;
    float   friction = 0.2f;
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
    float rotationaxis[] = {0.0000001f, 0.0f, 0.0f};
    float positionspeed[] = {0.0f, 0.0f, 0.0f};
    float rotationcenter[] = {0.0f, 0.0f, 0.0f};
    float rotationangle = 0.0f;
    float rotationspeed = 0.0f;
    float positionacceleration[] = {0.0f, 0.0f, 0.0f};
    float rotationacceleration = 0.0f;
    FloatBuffer matrixBuffer = null;
    FloatBuffer vertexBuffer = null;
    FloatBuffer normalBuffer = null;
    FloatBuffer colorBuffer = null;
    ShortBuffer indexBuffer = null;
    FloatBuffer textureBuffer = null;
    int[] texturesid = new int[20];
    int nbtexture=0;
    int texture=0;
    Bitmap bitmap = null;
    float m;

    int nbtriangle;
    long t0;
    ArrayList<MISObject> objects = new ArrayList<MISObject>();

    public MISObject(String s){
        name = s;
    }
    public MISObject(String s, AssetManager asset, String object, float scale, String texture, float posx, float posy, float posz, float m, float elasticity, float friction) throws IOException {
        name = s;
        this.m=m;
        this.elasticity=elasticity;
        this.friction=friction;
        loadobj(asset, object, scale);
        glGenTextures(20, texturesid, 0);
        loadtexture(asset, texture);
        position[0] = posx;
        position[1] = posy;
        position[2] = posz;
        updatematrix();
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
        updatematrix();
    }

    public void loadtexture(AssetManager assetManager, String name) {
        InputStream input = null;
        try {
            input = assetManager.open(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap = BitmapFactory.decodeStream(input);

        glBindTexture(GL_TEXTURE_2D, texturesid[nbtexture++]);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    void define(float [] vertices, float [] normals, float [] textures, short[] indices){

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

        byteBuf = ByteBuffer.allocateDirect(indices.length * 2);
        byteBuf.order(ByteOrder.nativeOrder());
        indexBuffer = byteBuf.asShortBuffer();
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
            float ln = Matrix.length(this.normals[4*i], this.normals[4*i+1],this.normals[4*i+2] );
            this.normals[4*i] /= ln;
            this.normals[4*i+1] /= ln;
            this.normals[4*i+2] /= ln;
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
        addnativeobject(name, nbtriangle, this.barycenter, this.bbray, this.centers, this.ray, this.normals, this.vertices,m,elasticity,friction);
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

    void updatematrix(){
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, position[0], position[1], position[2]);
        Matrix.rotateM(mModelMatrix, 0, rotationangle, rotationaxis[0], rotationaxis[1], rotationaxis[2]);
        rotationcenter[0] = barycenter[0] + position[0];
        rotationcenter[1] = barycenter[1] + position[1];
        rotationcenter[2] = barycenter[2] + position[2];
    }

    void moveto(float [] pos){
        position[0] = pos[0];
        position[1] = pos[1];
        position[2] = pos[2];
        updatematrix();
        t0 = System.nanoTime();
    }

    void moveto(float x, float y, float z){
        position[0] = x;
        position[1] = y;
        position[2] = z;
        updatematrix();
        t0 = System.nanoTime();
    }

    void rotationangle(float rot) {
        rotationangle = rot;
        updatematrix();
        t0 = System.nanoTime();
    }

    void rotationaxis(float [] rot){
        rotationaxis[0] = rot[0];
        rotationaxis[1] = rot[1];
        rotationaxis[2] = rot[2];
        updatematrix();
        t0 = System.nanoTime();
    }

    void rotationaxis(float x, float y, float z){
        rotationaxis[0] = x;
        rotationaxis[1] = y;
        rotationaxis[2] = z;
        rotationspeed = (float) Math.sqrt(rotationaxis[0]*rotationaxis[0]+rotationaxis[1]*rotationaxis[1]+rotationaxis[2]*rotationaxis[2]);
        updatematrix();
        t0 = System.nanoTime();
    }

    void posspeed(float [] pos){
        positionspeed[0] = pos[0];
        positionspeed[1] = pos[1];
        positionspeed[2] = pos[2];
    }

    void rotspeed(float rot){
        rotationspeed= rot;
    }

    void rotcenter(float [] rot){
        rotationcenter[0] = rot[0];
        rotationcenter[1] = rot[1];
        rotationcenter[2] = rot[2];
    }

    void posacceleration(float [] pos){
        positionacceleration[0] = pos[0];
        positionacceleration[1] = pos[1];
        positionacceleration[2] = pos[2];
    }

    void rotacceleration(float rot){
        rotationacceleration = rot;
    }

    void updateposition() {
        long t1 = System.nanoTime();
        long ldt = t1 - t0;
        float dt;
        if (ldt > 20000000)
            ldt = 20000000;   // CPU is too slow no real time
        t0 = t1;
        dt = (float) ((double) ldt / 1000000000.0);
        Matrix.setIdentityM(mMotionMatrix, 0);
        Matrix.translateM(mMotionMatrix, 0, positionspeed[0] * dt, positionspeed[1] * dt, positionspeed[2] * dt);
        Matrix.translateM(mMotionMatrix, 0, rotationcenter[0], rotationcenter[1], rotationcenter[2]);
        Matrix.rotateM(mMotionMatrix, 0, rotationspeed * dt, rotationaxis[0], rotationaxis[1], rotationaxis[2]);
        Matrix.translateM(mMotionMatrix, 0, -rotationcenter[0], -rotationcenter[1], -rotationcenter[2]);
        Matrix.multiplyMM(mModelMatrix, 0, mMotionMatrix, 0, mModelMatrix, 0);        //Matrix.translateM(mModelMatrix, 0, rotationcenter[0], rotationcenter[1], rotationcenter[2]);

        rotationcenter[0] += positionspeed[0] * dt;
        rotationcenter[1] += positionspeed[1] * dt;
        rotationcenter[2] += positionspeed[2] * dt;
        positionspeed[0] += positionacceleration[0] * dt;
        positionspeed[1] += positionacceleration[1] * dt;
        positionspeed[2] += positionacceleration[2] * dt;
        rotationspeed += rotationacceleration * dt;

        Matrix.multiplyMV(mvbarycenter, 0, mModelMatrix, 0, barycenter, 0);
        position[0] = mvbarycenter[0];
        position[1] = mvbarycenter[1];
        position[2] = mvbarycenter[2];
        rotationcenter[0] = mvbarycenter[0];
        rotationcenter[1] = mvbarycenter[1];
        rotationcenter[2] = mvbarycenter[2];
        updatenativeobject(name, mModelMatrix, positionspeed, rotationspeed, rotationaxis, rotationcenter);

//        for (int i=0;i<nbtriangle;i++){
//            Matrix.multiplyMV(mvcenters, i*4, mModelMatrix, 0, centers, i*4);
//            Matrix.multiplyMV(mvnormals, i*4, mModelMatrix, 0, normals, i*4);
//            Matrix.multiplyMV(mvvertices, i*12, mModelMatrix, 0, vertices, i*12);
//            Matrix.multiplyMV(mvvertices, i*12+4, mModelMatrix, 0, vertices, i*12+4);
//            Matrix.multiplyMV(mvvertices, i*12+8, mModelMatrix, 0, vertices, i*12+8);
//        }
    }

    public void draw(MISShader shader, float[] viewMatrix, float[] projectionMatrix) {
        float[] lightpos = {1.0f, 100.0f, 1.0f, 1.0f};
        float[] color = {1.0f, 1.0f, 1.0f, 1.0f};
        float[] shadowcolor = {0.1f, 0.1f, 0.1f, 1.0f};
        int vertexCount = nbtriangle * 3;
        float[] pmatrix = new float[16];
        float[] mvmatrix = new float[16];
        float[] identity= new float[16];
        float[] shadowmatrix = new float[16];
        updateposition();

        Matrix.setIdentityM(shadowmatrix, 0);
        shadowmatrix[5]=0.0001f;
        glEnable(GL_TEXTURE_2D);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texturesid[texture]);
        Matrix.setIdentityM(identity, 0);
        if(m != 0) {
            Matrix.multiplyMM(mvmatrix, 0, viewMatrix, 0, mModelMatrix, 0);
        }
        else {
            Matrix.multiplyMM(mvmatrix, 0, identity, 0, mModelMatrix, 0);
        }

        Matrix.multiplyMM(pmatrix, 0, projectionMatrix, 0, mvmatrix, 0);

        // Add program to OpenGL ES environment
        glUseProgram(shader.mProgram);

        // Enable a handle to the triangle vertices
        glEnableVertexAttribArray(shader.mPositionHandle);
        glEnableVertexAttribArray(shader.mNormalHandle);
        glEnableVertexAttribArray(shader.mTexCoordnateHandle);

        // Prepare the triangle coordinate data
        glVertexAttribPointer(shader.mPositionHandle, 3, GL_FLOAT, false,12, vertexBuffer);
        glVertexAttribPointer(shader.mNormalHandle, 3, GL_FLOAT, false,12, normalBuffer);
        glVertexAttribPointer(shader.mTexCoordnateHandle, 2, GL_FLOAT, false,8, textureBuffer);

        // Pass the projection and view transformation to the shader
        glUniformMatrix4fv(shader.mMVPMatrixHandle, 1, false, pmatrix, 0);
        glUniformMatrix4fv(shader.mMVMatrixHandle, 1, false, mvmatrix, 0);

        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        glUniform1i(shader.mTextureHandle, 0);

        glUniform4fv(shader.mLightPosHandle, 1, lightpos, 0);
        glUniform4fv(shader.mColorHandle, 1, color, 0);

        // Draw the triangle
        glDrawArrays(GL_TRIANGLES, 0, vertexCount);

        // shadow
        if((m <1000000.0f) && (m > 0)) {
            // Pass the projection and view transformation to the shader
            Matrix.multiplyMM(mvmatrix, 0, shadowmatrix, 0, mModelMatrix, 0);
            Matrix.multiplyMM(mvmatrix, 0, viewMatrix, 0, mvmatrix, 0);
            Matrix.multiplyMM(pmatrix, 0, projectionMatrix, 0, mvmatrix, 0);

            glUniformMatrix4fv(shader.mMVPMatrixHandle, 1, false, pmatrix, 0);
            glUniformMatrix4fv(shader.mMVMatrixHandle, 1, false, mvmatrix, 0);

            glUniform4fv(shader.mColorHandle, 1, shadowcolor, 0);

            // Draw the triangle
            glDrawArrays(GL_TRIANGLES, 0, vertexCount);
        }

        // Disable vertex array
        glDisableVertexAttribArray(shader.mPositionHandle);
        glDisableVertexAttribArray(shader.mNormalHandle);
        glDisableVertexAttribArray(shader.mTexCoordnateHandle);
    }
    public native void addnativeobject(String name, int nbtriangle, float [] barycenter, float bbray, float [] centers, float [] ray, float [] normals, float [] vertices, float m, float elasticity, float friction);
    public native void updatenativeobject(String name, float [] modelmatrix, float [] positionspeed, float rotationspeed, float [] rotationaxis, float [] rotationcenter );
    public native float [] getpositionspeed(String name);
    public native float [] getrotationaxis(String name);
    public native float [] getrotationcenter(String name);
    public native float  getrotationspeed(String name);
    public native float  getrotationacceleration(String name);
    public native boolean  getcollisionstate(String name);
}
