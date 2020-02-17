package yk.missgl;

import android.content.Context;
import android.content.res.AssetManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

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
    GLSurfaceView view;

    private SensorManager mSensorManager;

    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        AssetManager assetManager = getAssets();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        view = new GLSurfaceView(this);
        view.setEGLContextClientVersion(2);
        renderer = new OpenGLRenderer(this);
        view.setRenderer(renderer);
        setContentView(view);
    }
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return(renderer.scene.onTouchEvent(e));
    }
    private final SensorEventListener mSensorListener = new SensorEventListener() {
        // action quand le capteur bouge
        public void onSensorChanged(SensorEvent se) {
            //renderer.scene.onSensorEvent(se);
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };
}

class OpenGLRenderer implements GLSurfaceView.Renderer {
    AssetManager asset;
    MyScene scene;

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
            scene = new MyScene(asset);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        try {
            scene.draw();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        glViewport(0, 0, width, height);
        scene.viewratio((float)width / (float)height);
    }
}

