package com.example.yves_k1.mis;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_LEQUAL;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glClearDepthf;
import static android.opengl.GLES20.glDepthFunc;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;

public class MainActivity extends AppCompatActivity {
    OpenGLRenderer renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AssetManager assetManager = getAssets();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide(); //hide the title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        GLSurfaceView view = new GLSurfaceView(this);
        view.setEGLContextClientVersion(2);
        renderer = new OpenGLRenderer(this);
        view.setRenderer(renderer);
        setContentView(view);
    }
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return(renderer.scene.onTouchEvent(e));
    }

}

class OpenGLRenderer implements GLSurfaceView.Renderer {

    MySpinningCubeScene scene;
    AssetManager asset;

    OpenGLRenderer(Context c) {
        asset = c.getAssets();
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig unused1) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
        glClearDepthf(1.0f);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        try {
            scene = new MySpinningCubeScene(asset);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        scene.draw();
        scene.statemachine();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        glViewport(0, 0, width, height);
        scene.viewratio((float)width / (float)height);
    }
}

