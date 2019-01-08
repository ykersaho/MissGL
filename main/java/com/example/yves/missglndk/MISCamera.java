package com.example.yves.missglndk;

import android.opengl.Matrix;

/**
 * Created by Yves_K1 on 19/06/2018.
 */

public class MISCamera extends MISObject {
        float[] mViewMatrix = new float[16];

        public MISCamera(){
            super("camera");
        }
        public void move() {
            updateposition();
            Matrix.invertM(mViewMatrix, 0, mModelMatrix, 0);
            Matrix.multiplyMV(mvbarycenter, 0, mModelMatrix, 0, barycenter, 0);
            position[0] = mvbarycenter[0];
            position[1] = mvbarycenter[1];
            position[2] = mvbarycenter[2];
        }
}
