package com.example.yves.missglndk;

/**
 * Created by yves on 31/08/18.
 */

import android.opengl.GLES20;
import android.util.Log;

import static android.content.ContentValues.TAG;
import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_TRUE;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;

public class MISShader {
    String vertexShaderCode1 =
            "attribute vec4 a_Position;" +
                    "void main() {" +
                    "  gl_Position = a_Position;" +
                    "}";

    String vertexShaderCode2 =
            "uniform mat4 u_MVPMatrix;"+       // A constant representing the combined model/view/projection matrix.
            "attribute vec4 a_Position;"+      // Per-vertex position information we will pass in.
            "void main() {"+                     // The entry point for our vertex shader."v_Position = vec3(u_MVMatrix * a_Position);"+        // Transform the vertex into eye space.
            "gl_Position = u_MVPMatrix * a_Position;"+            // Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
            "}";

    String vertexShaderCode =
            "uniform mat4 u_MVPMatrix;"+       // A constant representing the combined model/view/projection matrix.
            "uniform mat4 u_MVMatrix;"+        // A constant representing the combined model/view matrix.
            "attribute vec4 a_Position;"+      // Per-vertex position information we will pass in.
            "attribute vec3 a_Normal;"+        // Per-vertex normal information we will pass in.
            "attribute vec2 a_TexCoordinate;"+ // Per-vertex texture coordinate information we will pass in.
            "varying vec3 v_Position;"+        // This will be passed into the fragment shader.
            "varying vec3 v_Normal;"+          // This will be passed into the fragment shader.
            "varying vec2 v_TexCoordinate;"+   // This will be passed into the fragment shader.
            "void main() {"+                     // The entry point for our vertex shader."v_Position = vec3(u_MVMatrix * a_Position);"+        // Transform the vertex into eye space.
            "v_TexCoordinate = a_TexCoordinate;"+                 // Pass through the texture coordinate.
            "v_Position = vec3(u_MVMatrix * a_Position);"+        // Transform the vertex into eye space
            "v_Normal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));"+ // Transform the normal's orientation into eye space.
            "gl_Position = u_MVPMatrix * a_Position;"+            // Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
            "}";

    String fragmentShaderCode1 =
            "precision mediump float;" +
            "uniform vec4 a_Color;" +
            "void main() {" +
            "gl_FragColor = a_Color;" +
            "}";

    String fragmentShaderCode =
            "precision mediump float;"+        // Set the default precision to medium.
                    "uniform vec3 u_LightPos;"+        // The position of the light in eye space.
                    "uniform sampler2D u_Texture;"+    // The input texture.
                    "varying vec3 v_Position;"+        // Interpolated position for this fragment.
                    "varying vec3 v_Normal;"+          // Interpolated normal for this fragment.
                    "varying vec2 v_TexCoordinate;"+   // Interpolated texture coordinate per fragment.
                    "void main() {"+                     // The entry point for our fragment shader.
                    "float distance = length(u_LightPos - v_Position);"+          // Will be used for attenuation.
                    "vec3 lightVector = normalize(u_LightPos - v_Position);"+     // Get a lighting direction vector from the light to the vertex.
                    "float diffuse = max(dot(v_Normal, lightVector), 0.0);"+      // Calculate the dot product of the light vector and vertex normal.
                    "diffuse = diffuse * (1.0 / (1.0 + (0.10 * distance)));"+     // Add attenuation.
                    "diffuse = diffuse + 0.5;"+                                   // Add ambient lighting
                    "gl_FragColor = (diffuse * texture2D(u_Texture, v_TexCoordinate));"+ // Multiply the color by the diffuse illumination level and texture value to get final output color.
                    "gl_FragColor.a = 1.0;"+
                "}";

    int vertexShader;
    int fragmentShader;
    int mProgram;
    int mPositionHandle;
    int mColorHandle;
    int mNormalHandle;
    int mTexCoordnateHandle;
    int mMVPMatrixHandle;
    int mMVMatrixHandle;
    int mLightPosHandle;
    int mTextureHandle;

    public int loadShader(int type, String shaderCode){
        int shader = glCreateShader(type);
        glShaderSource(shader, shaderCode);
        glCompileShader(shader);
        int[] compiled = new int[1];
        glGetShaderiv(shader, GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            Log.e(TAG, "Could not compile shader " + type + ":");
            Log.e(TAG, " " + glGetShaderInfoLog(shader));
            glDeleteShader(shader);
            shader = 0;
        }
        return shader;
    }

    public MISShader(){
        vertexShader = loadShader(GL_VERTEX_SHADER, vertexShaderCode);
        fragmentShader = loadShader(GL_FRAGMENT_SHADER, fragmentShaderCode);
        // create empty OpenGL ES Program
        mProgram = glCreateProgram();
        // add the vertex shader to program
        glAttachShader(mProgram, vertexShader);
        // add the fragment shader to program
        glAttachShader(mProgram, fragmentShader);
        // creates OpenGL ES program executables
        glLinkProgram(mProgram);
        int[] linkStatus = new int[1];
        glGetProgramiv(mProgram, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] != GL_TRUE) {
            Log.e(TAG, "Could not link programId: ");
            Log.e(TAG, glGetProgramInfoLog(mProgram));
            glDeleteProgram(mProgram);
            mProgram = 0;
        }
        mPositionHandle = glGetAttribLocation(mProgram, "a_Position");
        mColorHandle = glGetUniformLocation(mProgram, "a_Color");
        mNormalHandle = glGetAttribLocation(mProgram, "a_Normal");
        mTexCoordnateHandle = glGetAttribLocation(mProgram, "a_TexCoordinate");
        mMVPMatrixHandle = glGetUniformLocation(mProgram, "u_MVPMatrix");
        mMVMatrixHandle = glGetUniformLocation(mProgram, "u_MVMatrix");
        mTextureHandle = glGetUniformLocation(mProgram, "u_Texture");
        mLightPosHandle = glGetUniformLocation(mProgram, "u_LightPos");
    }
}

