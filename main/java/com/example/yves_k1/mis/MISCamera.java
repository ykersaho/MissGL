package com.example.yves_k1.mis;

import android.opengl.Matrix;

/**
 * Created by Yves_K1 on 19/06/2018.
 */

public class MISCamera extends MISObject {
        public MISCamera(){
            super("camera");
        }
        public void move() {
            updateposition();
            Matrix.setIdentityM(mModelMatrix, 0);
            Matrix.rotateM(mModelMatrix, 0, -rotation[0], 1.0f, 0.0f, 0.0f);
            Matrix.rotateM(mModelMatrix, 0, -rotation[1], 0.0f, 1.0f, 0.0f);
            Matrix.rotateM(mModelMatrix, 0, -rotation[2], 0.0f, 0.0f, 1.0f);
            Matrix.translateM(mModelMatrix, 0, -position[0], -position[1], -position[2]);
        }
}
