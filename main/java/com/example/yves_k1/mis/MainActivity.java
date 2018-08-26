package com.example.yves_k1.mis;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES10.GL_MODELVIEW;
import static android.opengl.GLES10.GL_PROJECTION;
import static android.opengl.GLES10.glClearColor;
import static android.opengl.GLES10.glDepthFunc;
import static android.opengl.GLES10.glEnable;
import static android.opengl.GLES10.glFrustumf;
import static android.opengl.GLES10.glLoadIdentity;
import static android.opengl.GLES10.glMatrixMode;
import static android.opengl.GLES20.glClearDepthf;
import static android.opengl.GLES20.glViewport;
import static java.lang.Math.PI;
import static java.lang.Math.asin;
import static java.lang.Math.sqrt;


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

    MyPongScene scene;
    AssetManager asset;

    OpenGLRenderer(Context c) {
        asset = c.getAssets();
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig unused1) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
        glClearDepthf(1.0f);
        glEnable(GLES30.GL_DEPTH_TEST);
        glDepthFunc(GLES30.GL_LEQUAL);
        try {
            scene = new MyPongScene(asset);
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
        float ratio = (float) width / height;
        glViewport(0, 0, width, height);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glFrustumf(-ratio, ratio, -1, 1, 1.5f, 20f);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }
}

