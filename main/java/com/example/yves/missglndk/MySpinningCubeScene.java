package com.example.yves.missglndk;

import android.content.res.AssetManager;
import android.view.MotionEvent;

import java.io.IOException;

/**
 * Created by yves on 05/08/18.
 */

public class MySpinningCubeScene extends MISScene {
    float[] cuberotationaxis = {0.5f, 1.0f, 0.0f};
    float cuberotationspeed = 180.0f;

    MySpinningCubeScene(AssetManager assetManager) throws IOException {
        addobject((MISObject) new MISObject("cube", assetManager, "cube.obj", 1.0f, "cube.png", 0.0f, 0.0f, -3.0f, 20.0f, 0.0f));
        getobject("cube").rotspeed(cuberotationspeed);
        getobject("cube").rotationaxis(cuberotationaxis);
        // that's it !
    }
}