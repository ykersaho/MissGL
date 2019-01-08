//
// Created by Yves_K1 on 11/12/2018.
//

#include "NativeObject.h"
#include <vector>
#include <map>
#include <string>
#include <cmath>

std::vector <NativeObject *> objects;
std::map<std::string, NativeObject *> hmap;

void getpositionspeed(const char *n, jfloat *positionspeed, int size) {
    NativeObject *o = hmap[std::string(n)];
    memcpy(positionspeed, o->positionspeed, size);
}

void getrotationaxis(const char *n, jfloat *rotationaxis, int size) {
    NativeObject *o = hmap[std::string(n)];
    memcpy(rotationaxis, o->rotationaxis, size);
}

jfloat getrotationspeed(const char *n) {
    NativeObject *o = hmap[std::string(n)];
    return(o->rotationspeed);
}


void addobject(const char *name, jint nbtriangle, jfloat *barycenter, jfloat bbray, jfloat *centers, jfloat *ray, jfloat *normals, jfloat *vertices, jfloat m, jfloat elasticity) {
NativeObject *obj = new NativeObject();
obj->name = name;
obj->nbtriangle = nbtriangle;
obj->barycenter = new float[4];
obj->mvbarycenter = new float[4];
memcpy(obj->barycenter, barycenter, 4*sizeof(float));
obj->bbray = bbray;
obj->centers = new float[4*nbtriangle];
obj->mvcenters = new float[4*nbtriangle];
memcpy(obj->centers, centers, 4*nbtriangle*sizeof(float));
obj->ray = new float[4*nbtriangle];
memcpy(obj->ray, ray, 4*nbtriangle*sizeof(float));
obj->normals = new float[4*nbtriangle];
obj->mvnormals = new float[4*nbtriangle];
memcpy(obj->normals, normals, 4*nbtriangle*sizeof(float));
obj->vertices = new float[4*3*nbtriangle];
obj->mvvertices = new float[4*3*nbtriangle];
memcpy(obj->vertices, vertices, 4*3*nbtriangle*sizeof(float));
obj->m = m;
obj->elasticity = elasticity;
objects.push_back(obj);
hmap[obj->name] = obj;
};

void multiply(int nb, float *m, float *vin, float *vout) {
    for(int i=0;i<nb;i++){
        *vout++ = m[0] * vin[0] + m[4] * vin [1] + m[8]  * vin[2] + m[12] * vin[3];
        *vout++ = m[1] * vin[0] + m[5] * vin [1] + m[9]  * vin[2] + m[13] * vin[3];
        *vout++ = m[2] * vin[0] + m[6] * vin [1] + m[10] * vin[2] + m[14] * vin[3];
        *vout++ = m[3] * vin[0] + m[7] * vin [1] + m[11] * vin[2] + m[15] * vin[3];
        vin += 4;
    }
}

void updateobject(const char *name, jfloat *modelmatrix, jfloat *positionspeed, jfloat rotationspeed, jfloat *rotationaxis){
    NativeObject *o = hmap[std::string(name)];
    if(o==NULL)
        return;
    memcpy(o->positionspeed, positionspeed, sizeof(o->positionspeed));
    memcpy(o->rotationaxis, rotationaxis, sizeof(o->rotationaxis));
    memcpy(o->modelmatrix, modelmatrix, sizeof(o->modelmatrix));
    o->rotationspeed=rotationspeed;
    multiply(o->nbtriangle*3,o->modelmatrix, o->vertices, o->mvvertices);
    multiply(o->nbtriangle,o->modelmatrix, o->normals, o->mvnormals);
    multiply(o->nbtriangle,o->modelmatrix, o->centers, o->mvcenters);
    multiply(1,o->modelmatrix, o->barycenter, o->mvbarycenter);
}

    int nbcol=0;
    float cc[]  = {0.0f, 0.0f, 0.0f, 0.0f};
    float ccc[]  = {0.0f, 0.0f, 0.0f, 0.0f};
    float cn1[] = {0.0f, 0.0f, 0.0f, 0.0f};
    float cn2[] = {0.0f, 0.0f, 0.0f, 0.0f};
    int *ct1;
    int *ct2;
    int ct1l;
    int ct2l;

    bool collision(float c1[], float r1, float v1[], float c2[], float r2, float v2[]){
        float sv1[3*4];
        float sc1[4];
        float sr1;
        float sv2[3*4];
        float sc2[4];
        float sr2;
        float dx = (c1[0]-c2[0]);
        float dy = (c1[1]-c2[1]);
        float dz = (c1[2]-c2[2]);
        float d1 = dx*dx+dy*dy+dz*dz;
        float d2 = r1+r2;
        d2 = d2*d2;
        if(d1 < d2) {
            nbcol++;
            if(d2 < 0.001) {
                cc[0] = (c1[0] + c2[0]) / 2;
                cc[1] = (c1[1] + c2[1]) / 2;
                cc[2] = (c1[2] + c2[2]) / 2;
                return(true);
            }
            if (r2 < r1) {
                memcpy(sv1, v2, sizeof(sv1));
                memcpy(sv2, v1, sizeof(sv2));
                sr2 = r1/2;
                sr1 = r2;
                sc1[0] = c2[0];
                sc1[1] = c2[1];
                sc1[2] = c2[2];
            }
            else {
                memcpy(sv1, v1, sizeof(sv1));
                memcpy(sv2, v2, sizeof(sv2));
                sr2 = r2/2;
                sr1 = r1;
                sc1[0] = c1[0];
                sc1[1] = c1[1];
                sc1[2] = c1[2];
            }
            float m1[4];
            float m2[4];
            float m3[4];
            m1[0] = (sv2[0] + sv2[4]) / 2;
            m1[1] = (sv2[1] + sv2[5]) / 2;
            m1[2] = (sv2[2] + sv2[6]) / 2;
            m2[0] = (sv2[4] + sv2[8]) / 2;
            m2[1] = (sv2[5] + sv2[9]) / 2;
            m2[2] = (sv2[6] + sv2[10]) / 2;
            m3[0] = (sv2[8] + sv2[0]) / 2;
            m3[1] = (sv2[9] + sv2[1]) / 2;
            m3[2] = (sv2[10] + sv2[2]) / 2;
            float tv[12];
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

    bool collision(float *c1, float r1, float *c2, float r2) {
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

    bool collision(float *c1, float *r1, float *v1, float *n1, float *c2, float *r2, float *v2, float *n2){
        int i, j;
        int nbc=0;
        cn1[0] = 0;
        cn1[1] = 0;
        cn1[2] = 0;
        cn2[0] = 0;
        cn2[1] = 0;
        cn2[2] = 0;
        ccc[0] = 0;
        ccc[1] = 0;
        ccc[2] = 0;
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
                    d1 = abs(dx*n1[4*idi]+dy*n1[4*idi+1]+dz*n1[4*idi+2]);
                    d2 = abs(dx*n2[4*idj]+dy*n2[4*idj+1]+dz*n2[4*idj+2]);
                    if((d1 < r2[idj]) && (d2 < r1[idi])) {
                        float tv1[12];
                        float tv2[12];
                        float tc1[4];
                        float tc2[4];
                        float tn1[4];
                        float tn2[4];
                        float tr1;
                        float tr2;
                        memcpy(tv1, v1 + idi * 12, sizeof(tv1));
                        memcpy(tv2, v2 + idj * 12, sizeof(tv2));
                        memcpy(tc1, c1 + idi * 4, sizeof(tc1));
                        memcpy(tc2, c2 + idj * 4, sizeof(tc2));
                        memcpy(tn1, n1 + idi * 4, sizeof(tn1));
                        memcpy(tn2, n2 + idj * 4, sizeof(tn2));
                        tr1 = r1[idi];
                        tr2 = r2[idj];
                        if (collision(tc1, tr1, tv1, tc2, tr2, tv2)) {
                            cn1[0] += tn1[0];
                            cn1[1] += tn1[1];
                            cn1[2] += tn1[2];
                            cn2[0] += tn2[0];
                            cn2[1] += tn2[1];
                            cn2[2] += tn2[2];
                            ccc[0] += cc[0];
                            ccc[1] += cc[1];
                            ccc[2] += cc[2];
                            nbc++;
                        }
                    }
                }
            }
        }
        if(nbc > 0) {
            cn1[0] /= nbc;
            cn1[1] /= nbc;
            cn1[2] /= nbc;
            cn2[0] /= nbc;
            cn2[1] /= nbc;
            cn2[2] /= nbc;
            ccc[0] /= nbc;
            ccc[1] /= nbc;
            ccc[2] /= nbc;
            return (true);
        }
        else {
            return (false);
        }
    }

    void impact(NativeObject *o1, float *n1, NativeObject *o2, float *n2, float *VOUT) {
        float G[3];
        float V1[3];
        float V2[3];
        float V1X[3];
        float V1Y[3];
        float N[3];
        float R[3];
        float VP[3];
        float lv1,lg,ln,lvp;
        memcpy(V1, o1->positionspeed, sizeof(V1));
        memcpy(V2, o2->positionspeed, sizeof(V2));
        VOUT[0] = V1[0];
        VOUT[1] = V1[1];
        VOUT[2] = V1[2];
        G[0] = o1->mvbarycenter[0] - ccc[0];
        G[1] = o1->mvbarycenter[1] - ccc[1];
        G[2] = o1->mvbarycenter[2] - ccc[2];

        lg=(float) sqrt(G[0]*G[0]+G[1]*G[1]+G[2]*G[2]);
        G[0] = G[0]/lg;
        G[1] = G[1]/lg;
        G[2] = G[2]/lg;
        ln=(float) sqrt(n2[0]*n2[0]+n2[1]*n2[1]+n2[2]*n2[2]);
        N[0] = n2[0]/ln;
        N[1] = n2[1]/ln;
        N[2] = n2[2]/ln;
        VP[0] = G[1]*V1[2]-G[2]*V1[1];
        VP[1] = G[2]*V1[0]-G[0]*V1[2];
        VP[2] = G[0]*V1[1]-G[1]*V1[0];
        lvp=(float) sqrt(VP[0]*VP[0]+VP[1]*VP[1]+VP[2]*VP[2]);
        VP[0] = VP[0]/lvp;
        VP[1] = VP[1]/lvp;
        VP[2] = VP[2]/lvp;
        float s1 = (V1[0]*N[0]+V1[1]*N[1]+V1[2]*N[2]);
        float s2 = (V2[0]*N[0]+V2[1]*N[1]+V2[2]*N[2]);
        float e1 = (o1->m - o2->m)  / (o1->m + o2->m);
        float e2 = 2 * o2->m / (o1->m + o2->m);
        if(o1->m < 1000000) {
            lv1=(float) sqrt(V1[0]*V1[0]+V1[1]*V1[1]+V1[2]*V1[2]);
            if(lvp != 0.0f)
                memcpy(o1->rotationaxis, VP, sizeof(o1->rotationaxis));
            o1->rotationspeed = (lvp/lg)*(180.0f/3.1416926f);
            float f = e1 * s1 + e2 * s2;
            if(s1 <= s2) {
                V1X[0] = f * N[0] * o1->elasticity;
                V1X[1] = f * N[1] * o1->elasticity;
                V1X[2] = f * N[2] * o1->elasticity;
            }
            else {
                V1X[0] = s1 * N[0];
                V1X[1] = s1 * N[1];
                V1X[2] = s1 * N[2];
            }
            V1Y[0] = (V1[0] - s1 * N[0]) * 0.8f;
            V1Y[1] = (V1[1] - s1 * N[1]) * 0.8f;
            V1Y[2] = (V1[2] - s1 * N[2]) * 0.8f;
            VOUT[0] = V1X[0] + V1Y[0];
            VOUT[1] = V1X[1] + V1Y[1];
            VOUT[2] = V1X[2] + V1Y[2];
        }
    }

void collision(const char *n1, const char *n2) {
    NativeObject *o1 = hmap[std::string(n1)];
    NativeObject *o2 = hmap[std::string(n2)];
    float zero[] = {0.0f, 0.0f, 0.0f};
    int i;
    nbcol = 0;

    if ((o1->m == 0) || (o2->m == 0))
        return;

    if ((o1->m >= 1000000) && (o2->m >= 1000000))
        return;

    // Eliminate most obvious case with bounding box
    float dx = (o1->mvbarycenter[0] - o2->mvbarycenter[0]);
    float dy = (o1->mvbarycenter[1] - o2->mvbarycenter[1]);
    float dz = (o1->mvbarycenter[2] - o2->mvbarycenter[2]);
    float d1 = dx * dx + dy * dy + dz * dz;
    float d2 = o1->bbray + o2->bbray;
    d2 = d2 * d2;
    if (d1 > d2)
        return;


    // Eliminate most obvious cases for object 1
    ct1 = new int[o1->nbtriangle];
    ct1l = 0;
    for (i = 0; i < o1->nbtriangle; i++) {
        dx = (o1->mvcenters[4 * i] - o2->mvbarycenter[0]);
        dy = (o1->mvcenters[4 * i + 1] - o2->mvbarycenter[1]);
        dz = (o1->mvcenters[4 * i + 2] - o2->mvbarycenter[2]);
        d1 = dx * dx + dy * dy + dz * dz;
        d2 = o1->ray[i] + o2->bbray;
        d2 = d2 * d2;
        if (d1 < d2) {
            d1 = abs(o1->mvnormals[4 * i] * dx + o1->mvnormals[4 * i + 1] * dy +
                     o1->mvnormals[4 * i + 2] * dz);
            if (d1 < o2->bbray) {
                ct1[ct1l++] = i;
            }
        }
    }

    // Eliminate most obvious cases for object 2
    ct2 = new int[o2->nbtriangle];
    ct2l = 0;
    for (i = 0; i < o2->nbtriangle; i++) {
        dx = (o2->mvcenters[4 * i] - o1->mvbarycenter[0]);
        dy = (o2->mvcenters[4 * i + 1] - o1->mvbarycenter[1]);
        dz = (o2->mvcenters[4 * i + 2] - o1->mvbarycenter[2]);
        d1 = dx * dx + dy * dy + dz * dz;
        d2 = o2->ray[i] + o1->bbray;
        d2 = d2 * d2;
        if (d1 < d2)
            d1 = abs(o2->mvnormals[4 * i] * dx + o2->mvnormals[4 * i + 1] * dy +
                     o2->mvnormals[4 * i + 2] * dz);
        if (d1 < o1->bbray) {
            ct2[ct2l++] = i;
        }
    }

    if (collision(o1->mvcenters, o1->ray, o1->mvvertices, o1->mvnormals, o2->mvcenters, o2->ray,
                  o2->mvvertices, o2->mvnormals)) {
    float V1[3];
    float V2[3];
    impact(o1, cn1, o2, cn2, V1);
    impact(o2, cn2, o1, cn1, V2);
    memcpy(o1->positionspeed, V1, sizeof(o1->positionspeed));
    memcpy(o2->positionspeed, V2, sizeof(o2->positionspeed));
    }
    delete ct1;
    delete ct2;
}

NativeObject::NativeObject() {
}
