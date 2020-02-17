package yk.missgl;

import android.content.res.AssetManager;
import android.view.MotionEvent;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static android.opengl.GLES10.GL_LIGHT0;
import static android.opengl.GLES10.GL_LIGHT1;

public class MyScene extends MyBocce {
    public MyScene(AssetManager assetManager) throws IOException { super(assetManager);
    }
}
