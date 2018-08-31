package com.example.yves_k1.mis;

import static java.lang.Math.abs;
import static java.lang.Math.decrementExact;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.sqrt;

/**
 * Created by yves on 24/06/18.
 */

public class MISCollision {
    int nbcol=0;
    float [] cc  = {0.0f, 0.0f, 0.0f, 0.0f};
    float [] cn1 = {0.0f, 0.0f, 0.0f, 0.0f};
    float [] cn2 = {0.0f, 0.0f, 0.0f, 0.0f};
    int [] ct1;
    int [] ct2;
    int ct1l;
    int ct2l;

    boolean collision(float c1[], float r1, float v1[], float c2[], float r2, float v2[]){
        float [] sv1 = new float[3*4];
        float [] sc1 = new float[4];
        float sr1;
        float [] sv2 = new float[3*4];
        float [] sc2 = new float[4];
        float sr2;
        float dx = (c1[0]-c2[0]);
        float dy = (c1[1]-c2[1]);
        float dz = (c1[2]-c2[2]);
        float d1 = dx*dx+dy*dy+dz*dz;
        float d2 = r1+r2;
        d2 = d2*d2;
        if(d1 < d2) {
            nbcol++;
            if(d2 < 0.01) {
                cc[0] = (c1[0] + c2[0]) / 2;
                cc[1] = (c1[1] + c2[1]) / 2;
                cc[2] = (c1[2] + c2[2]) / 2;
                return(true);
            }
            if (r2 < r1) {
                System.arraycopy(v2, 0, sv1, 0, sv1.length);
                System.arraycopy(v1, 0, sv2, 0, sv2.length);
                sr2 = r1/2;
                sr1 = r2;
                sc1[0] = c2[0];
                sc1[1] = c2[1];
                sc1[2] = c2[2];
            }
            else {
                System.arraycopy(v1, 0, sv1, 0, sv1.length);
                System.arraycopy(v2, 0, sv2, 0, sv2.length);
                sr2 = r2/2;
                sr1 = r1;
                sc1[0] = c1[0];
                sc1[1] = c1[1];
                sc1[2] = c1[2];
            }
            float [] m1 = new float[4];
            float [] m2 = new float[4];
            float [] m3 = new float[4];
            m1[0] = (sv2[0] + sv2[4]) / 2;
            m1[1] = (sv2[1] + sv2[5]) / 2;
            m1[2] = (sv2[2] + sv2[6]) / 2;
            m2[0] = (sv2[4] + sv2[8]) / 2;
            m2[1] = (sv2[5] + sv2[9]) / 2;
            m2[2] = (sv2[6] + sv2[10]) / 2;
            m3[0] = (sv2[8] + sv2[0]) / 2;
            m3[1] = (sv2[9] + sv2[1]) / 2;
            m3[2] = (sv2[10] + sv2[2]) / 2;
            float [] tv = new float[12];
            tv[0] = sv2[0];
            tv[1] = sv2[1];
            tv[2] = sv2[2];
            tv[4] = m1[0];
            tv[5] = m1[1];
            tv[6] = m1[2];
            tv[8] = m3[0];
            tv[9] = m3[1];
            tv[10] = m3[2];
            sc2[0] = (tv[0]+tv[4]+tv[8])/3;
            sc2[1] = (tv[1]+tv[5]+tv[9])/3;
            sc2[2] = (tv[2]+tv[6]+tv[10])/3;
            if(collision(sc1, sr1, sv1, sc2, sr2, tv))
                        return(true);
            tv[0] = m1[0];
            tv[1] = m1[1];
            tv[2] = m1[2];
            tv[4] = m2[0];
            tv[5] = m2[1];
            tv[6] = m2[2];
            tv[8] = m3[0];
            tv[9] = m3[1];
            tv[10] = m3[2];
            sc2[0] = (tv[0]+tv[4]+tv[8])/3;
            sc2[1] = (tv[1]+tv[5]+tv[9])/3;
            sc2[2] = (tv[2]+tv[6]+tv[10])/3;
            if(collision(sc1, sr1, sv1, sc2, sr2, tv))
                return(true);
            tv[0] = m1[0];
            tv[1] = m1[1];
            tv[2] = m1[2];
            tv[4] = sv2[4];
            tv[5] = sv2[5];
            tv[6] = sv2[6];
            tv[8] = m2[0];
            tv[9] = m2[1];
            tv[10] = m2[2];
            sc2[0] = (tv[0]+tv[4]+tv[8])/3;
            sc2[1] = (tv[1]+tv[5]+tv[9])/3;
            sc2[2] = (tv[2]+tv[6]+tv[10])/3;
            if(collision(sc1, sr1, sv1, sc2, sr2, tv))
                return(true);
            tv[0] = m3[0];
            tv[1] = m3[1];
            tv[2] = m3[2];
            tv[4] = m2[0];
            tv[5] = m2[1];
            tv[6] = m2[2];
            tv[8] = sv2[8];
            tv[9] = sv2[9];
            tv[10] = sv2[10];
            sc2[0] = (tv[0]+tv[4]+tv[8])/3;
            sc2[1] = (tv[1]+tv[5]+tv[9])/3;
            sc2[2] = (tv[2]+tv[6]+tv[10])/3;
            if(collision(sc1, sr1, sv1, sc2, sr2, tv))
                        return(true);
            }
        return(false);
    }

    boolean collision(float c1[], float r1, float c2[], float r2) {
        float dx = (c1[0]-c2[0]);
        float dy = (c1[1]-c2[1]);
        float dz = (c1[2]-c2[2]);
        float d1 = dx*dx+dy*dy+dz*dz;
        float d2 = r1+r2;
        d2 = d2*d2;
        if(d1 < d2)
            return(true);
        else
            return(false);
    }

    boolean collision(float c1[], float r1[], float v1[], float n1[], float c2[], float r2[], float v2[], float n2[]){
        int i, j;
        for(i=0;i<ct1l;i++){
            for(j=0;j<ct2l;j++) {
                int idi = ct1[i];
                int idj = ct2[j];
                float dx = (c1[4*idi]-c2[4*idj]);
                float dy = (c1[4*idi+1]-c2[4*idj+1]);
                float dz = (c1[4*idi+2]-c2[4*idj+2]);
                float d1 = dx*dx+dy*dy+dz*dz;
                float d2 = r1[idi]+r2[idj];
                d2 = d2*d2;
                if(d1 < d2) {
                    float [] tv1 = new float[12];
                    float [] tv2 = new float[12];
                    float [] tc1 = new float[4];
                    float [] tc2 = new float[4];
                    float [] tn1 = new float[4];
                    float [] tn2 = new float[4];
                    float tr1;
                    float tr2;
                    System.arraycopy(v1, idi * 12, tv1, 0, tv1.length);
                    System.arraycopy(v2, idj * 12, tv2, 0, tv2.length);
                    System.arraycopy(c1, idi * 4, tc1, 0, tc1.length);
                    System.arraycopy(c2, idj * 4, tc2, 0, tc2.length);
                    System.arraycopy(n1, idi * 4, tn1, 0, tn1.length);
                    System.arraycopy(n2, idj * 4, tn2, 0, tn2.length);
                    tr1 = r1[idi];
                    tr2 = r2[idj];
                    if(collision(tc1, tr1, tv1, tc2, tr2, tv2)){
                        cn1[0] = tn1[0];
                        cn1[1] = tn1[1];
                        cn1[2] = tn1[2];
                        cn2[0] = tn2[0];
                        cn2[1] = tn2[1];
                        cn2[2] = tn2[2];
                        return(true);
                    }
                }
            }
        }
        return(false);
    }
    void impact(MISObject o1, float [] n1, MISObject o2, float [] n2) {
        float [] G = new float[3];
        float [] V = new float[3];
        float [] N = new float[3];
        float [] R = new float[3];
        float [] VP = new float[3];
        float l;
        G[0] = o1.mvbarycenter[0] - cc[0];
        G[1] = o1.mvbarycenter[1] - cc[1];
        G[2] = o1.mvbarycenter[2] - cc[2];
        l=(float) Math.sqrt(G[0]*G[0]+G[1]*G[1]+G[2]*G[2]);
        G[0] = G[0]/l;
        G[1] = G[1]/l;
        G[2] = G[2]/l;
        l=(float) Math.sqrt(n2[0]*n2[0]+n2[1]*n2[1]+n2[2]*n2[2]);
        N[0] = n2[0]/l;
        N[1] = n2[1]/l;
        N[2] = n2[2]/l;
        System.arraycopy(o1.rotationspeed,0, R, 0, R.length);
        System.arraycopy(o1.positionspeed,0, V, 0, V.length);
        VP[0] = G[1]*V[2]-G[2]*V[1];
        VP[1] = G[2]*V[0]-G[0]*V[2];
        VP[2] = G[0]*V[1]-G[1]*V[0];
        float s = V[0]*N[0]+V[1]*N[1]+V[2]*N[2];
        float e = 2 * o2.m / (o1.m + o2.m);
        if(o1.m < 1000000) {
            if(s < 0.0) {
                V[0] = (V[0] - e * s * N[0] * o1.elasticity) * 0.95f;
                V[1] = (V[1] - e * s * N[1] * o1.elasticity) * 0.95f;
                V[2] = (V[2] - e * s * N[2] * o1.elasticity) * 0.95f;
                o1.posspeed(V);
            }
            R[0] = 360*VP[0] * 0.95f;
            R[1] = 360*VP[1] * 0.95f;
            R[2] = 360*VP[2] * 0.95f;
            o1.rotspeed(R);
        }
    }

    void collision(MISObject o1, MISObject o2) {
        float zero[] = {0.0f,0.0f,0.0f};
        nbcol=0;
        int i;

        o1.setcollisionstate(false);
        o2.setcollisionstate(false);

        if((o1.m ==0) || (o2.m ==0))
            return;

        if((o1.m >= 1000000) && (o2.m >= 1000000))
            return;

        // Eliminate most obvious case with bounding box
        float dx = (o1.mvbarycenter[0]-o2.mvbarycenter[0]);
        float dy = (o1.mvbarycenter[1]-o2.mvbarycenter[1]);
        float dz = (o1.mvbarycenter[2]-o2.mvbarycenter[2]);
        float d1 = dx*dx+dy*dy+dz*dz;
        float d2 = o1.bbray+o2.bbray;
        d2 = d2*d2;
        if(d1 > d2)
            return;


        // Eliminate most obvious cases for object 1
        ct1  = new int[o1.mvcenters.length/4];
        ct1l = 0;
        for(i=0;i<o1.mvcenters.length/4;i++) {
            dx = (o1.mvcenters[4*i]-o2.mvbarycenter[0]);
            dy = (o1.mvcenters[4*i+1]-o2.mvbarycenter[1]);
            dz = (o1.mvcenters[4*i+2]-o2.mvbarycenter[2]);
            d1 = dx*dx+dy*dy+dz*dz;
            d2 = o1.ray[i]+o2.bbray;
            d2 = d2*d2;
            if(d1 < d2)
                ct1[ct1l++]=i;
        }

        // Eliminate most obvious cases for object 2
        ct2  = new int[o2.mvcenters.length/4];
        ct2l = 0;
        for(i=0;i<o2.mvcenters.length/4;i++) {
            dx = (o2.mvcenters[4*i]-o1.mvbarycenter[0]);
            dy = (o2.mvcenters[4*i+1]-o1.mvbarycenter[1]);
            dz = (o2.mvcenters[4*i+2]-o1.mvbarycenter[2]);
            d1 = dx*dx+dy*dy+dz*dz;
            d2 = o2.ray[i]+o1.bbray;
            d2 = d2*d2;
            if(d1 < d2)
                ct2[ct2l++]=i;
        }
        if (!collision(o1.mvcenters, o1.ray, o1.mvvertices, o1.mvnormals, o2.mvcenters, o2.ray, o2.mvvertices, o2.mvnormals))
            return;

        o1.setcollisionstate(true);
        o2.setcollisionstate(true);
        impact(o1, cn1, o2, cn2);
        impact(o2, cn2, o1, cn1);
    }

    MISCollision(MISScene s) {
        int i,j;
        for(i=0;i<s.objects.size()-1;i++){
            for(j=i+1;j<s.objects.size();j++){
                    collision(s.objects.get(i), s.objects.get(j));
            }
        }
    }
}
