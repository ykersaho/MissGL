package com.example.yves_k1.mis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Yves_K1 on 20/06/2018.
 */

public class MISObjloader {
    ArrayList<Float> v = new ArrayList<Float>();
    ArrayList<Float> vn = new ArrayList<Float>();
    ArrayList<Float> vt = new ArrayList<Float>();
    ArrayList<Integer> fv = new ArrayList<Integer>();
    ArrayList<Integer> fvn = new ArrayList<Integer>();
    ArrayList<Integer> fvt = new ArrayList<Integer>();
    float[] vertices;
    float[] normals;
    float[] textures;
    float[] barycenter = {0.0f, 0.0f, 0.0f};
    int[] indices;

    MISObjloader(InputStream input, float scale) throws IOException {
        int i=0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        while (true) {
            String s = reader.readLine();
            if (s == null) break;
            if (s.length() > 0) {
                StringTokenizer st = new StringTokenizer(s, " /");
                String ss = st.nextToken();
                if (ss.compareTo("v") == 0) {
                    for (i = 0; i < 3; i++) {
                        v.add(Float.parseFloat(st.nextToken()));
                    }
                } else if (ss.compareTo("vn") == 0) {
                    for (i = 0; i < 3; i++) {
                        vn.add(Float.parseFloat(st.nextToken()));
                    }
                } else if (ss.compareTo("vt") == 0) {
                    for (i = 0; i < 2; i++) {
                        vt.add(Float.parseFloat(st.nextToken()));
                    }
                } else if (ss.compareTo("f") == 0) {
                    for (i = 0; i < 3; i++) {
                        fv.add(Integer.parseInt(st.nextToken()) - 1);
                        fvt.add(Integer.parseInt(st.nextToken()) - 1);
                        fvn.add(Integer.parseInt(st.nextToken()) - 1);
                    }
                }
            }
        }
        // Make It Simple
        vertices = new float[fv.size()*3];
        normals  = new float[fv.size()*3];
        textures = new float[fv.size()*2];
        indices  = new int[fv.size()];
        for(i=0;i<fv.size();i++) {
            vertices[3*i] = v.get(3*fv.get(i))*scale;
            vertices[3*i+1] = v.get(3*fv.get(i)+1)*scale;
            vertices[3*i+2] = v.get(3*fv.get(i)+2)*scale;
            normals[3*i]  = vn.get(3*fvn.get(i));
            normals[3*i+1]  = vn.get(3*fvn.get(i)+1);
            normals[3*i+2]  = vn.get(3*fvn.get(i)+2);
            textures[2*i] = vt.get(2*fvt.get(i));
            textures[2*i+1] = vt.get(2*fvt.get(i)+1);
            indices[i] = (int) i;
            barycenter[0] += vertices[3*i];
            barycenter[1] += vertices[3*i+1];
            barycenter[2] += vertices[3*i+2];
        }
        barycenter[0] /= fv.size();
        barycenter[1] /= fv.size();
        barycenter[2] /= fv.size();
        // translate object to the origin
        for(i=0;i<fv.size();i++) {
            vertices[3*i] -= barycenter[0];
            vertices[3*i+1] -= barycenter[1];
            vertices[3*i+2] -= barycenter[2];
        }
    }
}