package com.example.yves_k1.mis;

import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static android.opengl.GLES11.glGetFloatv;
import static android.opengl.GLES20.glGetIntegerv;
import static android.opengl.GLES20.glReadPixels;
import static android.opengl.GLU.gluUnProject;
import static javax.microedition.khronos.opengles.GL11.GL_MODELVIEW_MATRIX;
import static javax.microedition.khronos.opengles.GL11.GL_PROJECTION_MATRIX;
import static javax.microedition.khronos.opengles.GL11.GL_VIEWPORT;
import static javax.microedition.khronos.opengles.GL11ExtensionPack.GL_DEPTH_COMPONENT;

/**
 * Created by yves on 29/07/18.
 */

public class MISPicking {
    void intersect(float [] p1, float [] p2, MISObject o) {
        float dp1p2;
        float d;
        float [] p1p2 = {0.0f, 0.0f,0.0f, 0.0f};
        float [] p1pb = {0.0f, 0.0f,0.0f, 0.0f};
        float [] vproj = {0.0f, 0.0f,0.0f, 0.0f};
        float scalar;

        p1p2[0] = p2[0] - p1[0];
        p1p2[1] = p2[1] - p1[1];
        p1p2[2] = p2[2] - p1[2];
        p1pb[0] = o.mvbarycenter[0]- p1[0];
        p1pb[1] = o.mvbarycenter[1]- p1[1];
        p1pb[2] = o.mvbarycenter[2]- p1[2];
        dp1p2 = (float)Math.sqrt(p1p2[0]*p1p2[0]+p1p2[1]*p1p2[1]+p1p2[2]*p1p2[2]);
        p1p2[0] /= dp1p2;
        p1p2[1] /= dp1p2;
        p1p2[2] /= dp1p2;

        scalar = p1p2[0]*p1pb[0]+p1p2[1]*p1pb[1]+p1p2[2]*p1pb[2];
        vproj[0] = o.mvbarycenter[0]-(p1[0] + scalar * p1p2[0]);
        vproj[1] = o.mvbarycenter[1]-(p1[1] + scalar * p1p2[1]);
        vproj[2] = o.mvbarycenter[2]-(p1[2] + scalar * p1p2[2]);
        d = (float)Math.sqrt(vproj[0]*vproj[0]+vproj[1]*vproj[1]+vproj[2]*vproj[2]);


        if(d < o.bbray)
            o.picked=true;
        else
            o.picked=false;
    }

    MISPicking(MISScene s) {
        int i;
        FloatBuffer matrixBuffer = null;
        IntBuffer viewBuffer = null;
        float [] zero = {0.0f, 0.0f,0.0f, 0.0f};
        float matview[] = new float[16];
        float matproj[] = new float[16];
        int viewport[] = new int[4];
        float [] p1 = new float[4];
        float [] p2 = new float[4];
        float identity[] = new float[16];
        Matrix.setIdentityM(identity, 0);
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(16 * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        matrixBuffer = byteBuf.asFloatBuffer();
        matrixBuffer.position(0);
        glGetFloatv(GL_MODELVIEW_MATRIX, matrixBuffer);
        matrixBuffer.get(matview);
        matrixBuffer.position(0);
        glGetFloatv(GL_PROJECTION_MATRIX, matrixBuffer);
        matrixBuffer.get(matproj);
        ByteBuffer byteBufViewPort = ByteBuffer.allocateDirect(4 * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        viewBuffer = byteBuf.asIntBuffer();
        viewBuffer.position(0);
        glGetIntegerv(GL_VIEWPORT, viewBuffer);
        viewBuffer.get(viewport);
        gluUnProject(s.pickpointer[0], viewport[3]-s.pickpointer[1], 0.0f, identity, 0, matproj, 0, viewport, 0, p1, 0);
        gluUnProject(s.pickpointer[0], viewport[3]-s.pickpointer[1], 1.0f, identity, 0, matproj, 0, viewport, 0, p2, 0);
        p1[0] /= p1[3];
        p1[1] /= p1[3];
        p1[2] /= p1[3];
        p2[0] /= p2[3];
        p2[1] /= p2[3];
        p2[2] /= p2[3];
        for(i=0;i<s.objects.size();i++){
                if(s.objects.get(i).m==0)
                    intersect(p1, p2, s.objects.get(i));
        }
        gluUnProject(s.pickpointer[0], viewport[3]-s.pickpointer[1], 0.0f, matview, 0, matproj, 0, viewport, 0, p1, 0);
        gluUnProject(s.pickpointer[0], viewport[3]-s.pickpointer[1], 1.0f, matview, 0, matproj, 0, viewport, 0, p2, 0);
        p1[0] /= p1[3];
        p1[1] /= p1[3];
        p1[2] /= p1[3];
        p2[0] /= p2[3];
        p2[1] /= p2[3];
        p2[2] /= p2[3];
        for(i=0;i<s.objects.size();i++){
            if(s.objects.get(i).m!=0)
                intersect(p1, p2, s.objects.get(i));
        }
    }
}
