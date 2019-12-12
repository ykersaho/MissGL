//
// Created by Yves_K1 on 11/12/2018.
//

#include "NativeObject.h"
#include <vector>
#include <map>
#include <string>
#include <cmath>
#include <android/log.h>

std::vector <NativeObject *> objects;
std::map<std::string, NativeObject *> hmap;
Impact gimpacts[10000];
unsigned int gnbimpact=0;

void getpositionspeed(const char *n, jfloat *positionspeed, int size) {
    NativeObject *o = hmap[std::string(n)];
    memcpy(positionspeed, o->positionspeed, size);
}

void getrotationaxis(const char *n, jfloat *rotationaxis, int size) {
    NativeObject *o = hmap[std::string(n)];
    memcpy(rotationaxis, o->rotationaxis, size);
}

void getrotationcenter(const char *n, jfloat *rotationcenter, int size) {
    NativeObject *o = hmap[std::string(n)];
    memcpy(rotationcenter, o->rotationcenter, size);
}

jfloat getrotationspeed(const char *n) {
    NativeObject *o = hmap[std::string(n)];
    return(o->rotationspeed);
}

jfloat getrotationacceleration(const char *n) {
    NativeObject *o = hmap[std::string(n)];
    return(o->rotationacceleration);
}

jboolean getcollisionstate(const char *n) {
    NativeObject *o = hmap[std::string(n)];
    return(o->collision);
}

void addobject(const char *name, jint nbtriangle, jfloat *barycenter, jfloat bbray, jfloat *centers, jfloat *ray, jfloat *normals, jfloat *vertices, jfloat m, jfloat elasticity,jfloat friction) {
    if(hmap[name] != NULL)
        return;
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
    obj->ray = new float[nbtriangle];
    memcpy(obj->ray, ray, nbtriangle*sizeof(float));
    obj->normals = new float[4*nbtriangle];
    obj->mvnormals = new float[4*nbtriangle];
    memcpy(obj->normals, normals, 4*nbtriangle*sizeof(float));
    obj->vertices = new float[4*3*nbtriangle];
    obj->mvvertices = new float[4*3*nbtriangle];
    memcpy(obj->vertices, vertices, 4*3*nbtriangle*sizeof(float));
    obj->m = m;
    obj->elasticity = elasticity;
    objects.push_back(obj);
    obj->elasticity = elasticity;
    obj->friction = friction;
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

void updateobject(const char *name, jfloat *modelmatrix, jfloat *positionspeed, jfloat rotationspeed, jfloat *rotationaxis, jfloat *rotationcenter){
    NativeObject *o = hmap[std::string(name)];
    if(o==NULL)
        return;
    memcpy(o->positionspeed, positionspeed, sizeof(o->positionspeed));
    memcpy(o->rotationaxis, rotationaxis, sizeof(o->rotationaxis));
    memcpy(o->rotationcenter, rotationcenter, sizeof(o->rotationcenter));
    memcpy(o->modelmatrix, modelmatrix, sizeof(o->modelmatrix));
    o->rotationspeed=rotationspeed;
    o->rotationacceleration=0;
    multiply(o->nbtriangle*3,o->modelmatrix, o->vertices, o->mvvertices);
    multiply(o->nbtriangle,o->modelmatrix, o->normals, o->mvnormals);
    multiply(o->nbtriangle,o->modelmatrix, o->centers, o->mvcenters);
    multiply(1,o->modelmatrix, o->barycenter, o->mvbarycenter);
    o->collision=false;
}

bool edgetrianglecollision(float *v1, float *v2, float *c, float *n, float *cp) {
    float dx1 = (v1[0]-c[0]);
    float dy1 = (v1[1]-c[1]);
    float dz1 = (v1[2]-c[2]);
    float d1 = (dx1*n[0]+dy1*n[1]+dz1*n[2]);
    float dx2 = (v2[0]-c[0]);
    float dy2 = (v2[1]-c[1]);
    float dz2 = (v2[2]-c[2]);
    float d2 = (dx2*n[0]+dy2*n[1]+dz2*n[2]);
    if(d1*d2 > 0)
        return(false);
    if(fabs(d1-d2) < 0.0000001)
        return(false);
    cp[0] = (v1[0] * d2 - v2[0] * d1) / (d2 - d1);
    cp[1] = (v1[1] * d2 - v2[1] * d1) / (d2 - d1);
    cp[2] = (v1[2] * d2 - v2[2] * d1) / (d2 - d1);
    return(true);
}

bool pointinsidetriangle(float *p, float *t) {
    float v1[3];
    float v2[3];
    float v3[3];
    float vc1[3];
    float vc2[3];
    float vc3[3];
    float pv1[3];
    float pv2[3];
    float pv3[3];
    float ps;
    v1[0] = t[4] - t[0];
    v1[1] = t[5] - t[1];
    v1[2] = t[6] - t[2];
    v2[0] = t[8] - t[4];
    v2[1] = t[9] - t[5];
    v2[2] = t[10] - t[6];
    v3[0] = t[0] - t[8];
    v3[1] = t[1] - t[9];
    v3[2] = t[2] - t[10];
    vc1[0] = p[0] - t[0];
    vc1[1] = p[1] - t[1];
    vc1[2] = p[2] - t[2];
    vc2[0] = p[0] - t[4];
    vc2[1] = p[1] - t[5];
    vc2[2] = p[2] - t[6];
    vc3[0] = p[0] - t[8];
    vc3[1] = p[1] - t[9];
    vc3[2] = p[2] - t[10];
    pv1[0] = v1[1]*vc1[2]-v1[2]*vc1[1];
    pv1[1] = v1[2]*vc1[0]-v1[0]*vc1[2];
    pv1[2] = v1[0]*vc1[1]-v1[1]*vc1[0];
    pv2[0] = v2[1]*vc2[2]-v2[2]*vc2[1];
    pv2[1] = v2[2]*vc2[0]-v2[0]*vc2[2];
    pv2[2] = v2[0]*vc2[1]-v2[1]*vc2[0];
    pv3[0] = v3[1]*vc3[2]-v3[2]*vc3[1];
    pv3[1] = v3[2]*vc3[0]-v3[0]*vc3[2];
    pv3[2] = v3[0]*vc3[1]-v3[1]*vc3[0];
    ps = pv1[0]*pv2[0]+pv1[1]*pv2[1]+pv1[2]*pv2[2];
    if(ps < 0.0)
        return(false);
    ps = pv1[0]*pv3[0]+pv1[1]*pv3[1]+pv1[2]*pv3[2];
    if(ps < 0.0)
        return(false);
    ps = pv3[0]*pv2[0]+pv3[1]*pv2[1]+pv3[2]*pv2[2];
    if(ps < 0.0)
        return(false);
    return(true);
}

void addimpact(NativeObject *o1,int idi,NativeObject *o2,int idj,float *cc) {
    int i;
    gimpacts[gnbimpact].point[0] = cc[0];
    gimpacts[gnbimpact].point[1] = cc[1];
    gimpacts[gnbimpact].point[2] = cc[2];
    gimpacts[gnbimpact].nid1 = idi;
    gimpacts[gnbimpact].o1 = o1;
    gimpacts[gnbimpact].nid2 = idj;
    gimpacts[gnbimpact].o2 = o2;
    gnbimpact++;
    o1->collision = true;
    o2->collision = true;
}

void edgecollision(NativeObject *o1, int ct1l, int *ct1, NativeObject *o2, int ct2l, int *ct2){
    int i, j;
    int nbc=0;
    float *c1 = o1->mvcenters;
    float *r1 = o1->ray;
    float *v1 = o1->mvvertices;
    float *n1 = o1->mvnormals;
    float *c2 = o2->mvcenters;
    float *r2 = o2->ray;
    float *v2 = o2->mvvertices;
    float *n2 = o2->mvnormals;
    float cc[3];
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
                    if(edgetrianglecollision(v1+idi*12,v1+idi*12+4,c2+idj*4,n2+idj*4, cc)) {
                        if(pointinsidetriangle(cc, v2 + idj * 12)) {
                            addimpact(o1,idi,o2,idj,cc);
                        }
                    }
                    if(edgetrianglecollision(v1+idi*12,v1+idi*12+8,c2+idj*4,n2+idj*4, cc)) {
                        if(pointinsidetriangle(cc, v2 + idj * 12)) {
                            addimpact(o1,idi,o2,idj,cc);
                        }
                    }
                    if(edgetrianglecollision(v1+idi*12+8,v1+idi*12+4,c2+idj*4,n2+idj*4, cc)) {
                        if(pointinsidetriangle(cc, v2 + idj * 12)) {
                            addimpact(o1,idi,o2,idj,cc);
                        }
                    }
                    if(edgetrianglecollision(v2+idj*12,v2+idj*12+4,c1+idi*4,n1+idi*4, cc)) {
                        if(pointinsidetriangle(cc, v1 + idi * 12)) {
                            addimpact(o1,idi,o2,idj,cc);
                        }
                    }
                    if(edgetrianglecollision(v2+idj*12,v2+idj*12+8,c1+idi*4,n1+idi*4, cc)) {
                        if(pointinsidetriangle(cc, v1 + idi * 12)) {
                            addimpact(o1,idi,o2,idj,cc);
                        }
                    }
                    if(edgetrianglecollision(v2+idj*12+8,v2+idj*12+4,c1+idi*4,n1+idi*4, cc)) {
                        if(pointinsidetriangle(cc, v1 + idi * 12)) {
                            addimpact(o1,idi,o2,idj,cc);
                        }
                    }
                }
            }
        }
    }
}

int impact(NativeObject *o1, NativeObject *o2, float *n1, float *n2, float *VOUT, float *RA, float *RS, float *RC, float *ccc) {
    float R1[3];
    float R2[3];
    float G1[3];
    float G2[3];
    float VR[3];
    float VT1[3];
    float VT2[3];
    float VT1X[3];
    float VT2X[3];
    float VT1Y[3];
    float VT2Y[3];

    float VG1[3];
    float VG2[3];
    float VG1X[3];
    float VG2X[3];
    float VG1Y[3];
    float VG2Y[3];

    float VR1[3];
    float VR2[3];
    float VR1X[3];
    float VR2X[3];
    float VR1Y[3];
    float VR2Y[3];
    float N1[3];
    float N2[3];
    float VP[3];
    float VP1[3];
    float coef1,coef2;
    float lv1,lg1,lg2,ln,lvp;
    memcpy(VT1, o1->positionspeed, sizeof(VT1));
    memcpy(VT2, o2->positionspeed, sizeof(VT2));
    memcpy(RA, o1->rotationaxis, sizeof(float)*3);
    memcpy(RC, o1->mvbarycenter, sizeof(float)*3);
    *RS=o1->rotationspeed;
    VOUT[0] = VT1[0];
    VOUT[1] = VT1[1];
    VOUT[2] = VT1[2];

    if((o1->m==0.0f) || (o1->m>=1000000.0f))
        return(0);

    ln=(float) sqrt(n1[0]*n1[0]+n1[1]*n1[1]+n1[2]*n1[2]);
    if(ln > 0.0000001) {
        N1[0] = n1[0] / ln;
        N1[1] = n1[1] / ln;
        N1[2] = n1[2] / ln;
    }

    ln=(float) sqrt(n2[0]*n2[0]+n2[1]*n2[1]+n2[2]*n2[2]);
    if(ln > 0.0000001) {
        N2[0] = n2[0] / ln;
        N2[1] = n2[1] / ln;
        N2[2] = n2[2] / ln;
    }

    // first get the speed vector at the impact (rotation + translation)
    __android_log_print(ANDROID_LOG_ERROR, "TRACKERS", "--------------------------------O1------%d\n",o1);
    R1[0] = o1->mvbarycenter[0] - ccc[0];
    R1[1] = o1->mvbarycenter[1] - ccc[1];
    R1[2] = o1->mvbarycenter[2] - ccc[2];
    VR1[0] = R1[1]*o1->rotationaxis[2]-R1[2]*o1->rotationaxis[1];
    VR1[1] = R1[2]*o1->rotationaxis[0]-R1[0]*o1->rotationaxis[2];
    VR1[2] = R1[0]*o1->rotationaxis[1]-R1[1]*o1->rotationaxis[0];
    G1[0] = o1->mvbarycenter[0] - ccc[0];
    G1[1] = o1->mvbarycenter[1] - ccc[1];
    G1[2] = o1->mvbarycenter[2] - ccc[2];
    lg1=(float) sqrt(G1[0]*G1[0]+G1[1]*G1[1]+G1[2]*G1[2]);
    if(lg1 > 0.001) {
        G1[0] = G1[0] / lg1;
        G1[1] = G1[1] / lg1;
        G1[2] = G1[2] / lg1;
        VR1[0] = VR1[0] / lg1;
        VR1[1] = VR1[1] / lg1;
        VR1[2] = VR1[2] / lg1;
    } else {
        G1[0] = 0;
        G1[1] = 0;
        G1[2] = 0;
        VR1[0] = 0;
        VR1[1] = 0;
        VR1[2] = 0;
    }

    R2[0] = o2->mvbarycenter[0] - ccc[0];
    R2[1] = o2->mvbarycenter[1] - ccc[1];
    R2[2] = o2->mvbarycenter[2] - ccc[2];
    VR2[0] = R2[1]*o2->rotationaxis[2]-R2[2]*o2->rotationaxis[1];
    VR2[1] = R2[2]*o2->rotationaxis[0]-R2[0]*o2->rotationaxis[2];
    VR2[2] = R2[0]*o2->rotationaxis[1]-R2[1]*o2->rotationaxis[0];
    G2[0] = o2->mvbarycenter[0] - ccc[0];
    G2[1] = o2->mvbarycenter[1] - ccc[1];
    G2[2] = o2->mvbarycenter[2] - ccc[2];
    lg2=(float) sqrt(G2[0]*G2[0]+G2[1]*G2[1]+G2[2]*G2[2]);
    if(lg2 > 0.0000001) {
        G2[0] = G2[0] / lg2;
        G2[1] = G2[1] / lg2;
        G2[2] = G2[2] / lg2;
    } else {
        G2[0] = 0;
        G2[1] = 0;
        G2[2] = 0;
    }

    //components among G
    float sg1 = (VT1[0]*G1[0]+VT1[1]*G1[1]+VT1[2]*G1[2]);
    VG1X[0] = sg1 * G1[0];
    VG1X[1] = sg1 * G1[1];
    VG1X[2] = sg1 * G1[2];
    VG1Y[0] = VT1[0] - VG1X[0];
    VG1Y[1] = VT1[1] - VG1X[1];
    VG1Y[2] = VT1[2] - VG1X[2];

    float sg2 = (VT2[0]*G2[0]+VT2[1]*G2[1]+VT2[2]*G2[2]);
    VG2X[0] = sg2 * G2[0];
    VG2X[1] = sg2 * G2[1];
    VG2X[2] = sg2 * G2[2];
    VG2Y[0] = VT2[0] - VG2X[0];
    VG2Y[1] = VT2[1] - VG2X[1];
    VG2Y[2] = VT2[2] - VG2X[2];

    sg2 = (VG2X[0]*G1[0]+VG2X[1]*G1[1]+VG2X[2]*G1[2]);
    VG2X[0] = sg2 * G1[0];
    VG2X[1] = sg2 * G1[1];
    VG2X[2] = sg2 * G1[2];

    //components among Normal
    float st1 = (VG1X[0]*N2[0]+VG1X[1]*N2[1]+VG1X[2]*N2[2]);
    VT1X[0] = st1 * N2[0];
    VT1X[1] = st1 * N2[1];
    VT1X[2] = st1 * N2[2];
    VT1Y[0] = VG1X[0] - VT1X[0];
    VT1Y[1] = VG1X[1] - VT1X[1];
    VT1Y[2] = VG1X[2] - VT1X[2];

    float st2 = (VG2X[0]*N2[0]+VG2X[1]*N2[1]+VG2X[2]*N2[2]);
    VT2X[0] = st2 * N2[0];
    VT2X[1] = st2 * N2[1];
    VT2X[2] = st2 * N2[2];
    VT2Y[0] = VG2X[0] - VT2X[0];
    VT2Y[1] = VG2X[1] - VT2X[1];
    VT2Y[2] = VG2X[2] - VT2X[2];

    float sr1 = (VR1[0]*N2[0]+VR1[1]*N2[1]+VR1[2]*N2[2]);
    VR1Y[0] = VR1[0] - sr1 * N2[0];
    VR1Y[1] = VR1[1] - sr1 * N2[1];
    VR1Y[2] = VR1[2] - sr1 * N2[2];
    VR1X[0] = sr1 * N2[0];
    VR1X[1] = sr1 * N2[1];
    VR1X[2] = sr1 * N2[2];

    float sr2 = (VR2[0]*N1[0]+VR2[1]*N1[1]+VR2[2]*N1[2]);
    VR2Y[0] = VR2[0] - sr2 * N1[0];
    VR2Y[1] = VR2[1] - sr2 * N1[1];
    VR2Y[2] = VR2[2] - sr2 * N1[2];
    VR2X[0] = sr2 * N1[0];
    VR2X[1] = sr2 * N1[1];
    VR2X[2] = sr2 * N1[2];

    // translation impact
    if(sg1 <= sg2) {
        // impact on translation
        float e1 = o1->m / (o1->m + o2->m);
        float e2 = o2->m / (o1->m + o2->m);
        float f = e2 * (sg2 - sg1) * o1->elasticity + e1 * sg1 + e2 * sg2;
        float resistance = (1.0f - o1->friction * o2->friction);

        VOUT[0] = f * G1[0] + resistance*VG1Y[0];
        VOUT[1] = f * G1[1] + resistance*VG1Y[1];
        VOUT[2] = f * G1[2] + resistance*VG1Y[2];

        // impact on rotation
        VP[0] = G1[1] * VG1Y[2] - G1[2] * VG1Y[1];
        VP[1] = G1[2] * VG1Y[0] - G1[0] * VG1Y[2];
        VP[2] = G1[0] * VG1Y[1] - G1[1] * VG1Y[0];

        RA[0] = RA[0] + VP[0]/lg1;
        RA[1] = RA[1] + VP[1]/lg1;
        RA[2] = RA[2] + VP[2]/lg1;

        return(1);
    } else
    // rotation impact
    if(sr1 <= 0) {
        float resistance = (1.0f - o1->friction * o2->friction);
        VR[0] = o1->elasticity * VR1X[0] + resistance*VR1Y[0];
        VR[1] = o1->elasticity * VR1X[1] + resistance*VR1Y[1];
        VR[2] = o1->elasticity * VR1X[2] + resistance*VR1Y[2];

        VP[0] = G1[1] * VR[2] - G1[2] * VR[1];
        VP[1] = G1[2] * VR[0] - G1[0] * VR[2];
        VP[2] = G1[0] * VR[1] - G1[1] * VR[0];

        RA[0] += VP[0]/lg1;
        RA[1] += VP[1]/lg1;
        RA[2] += VP[2]/lg1;
        return(1);
    }
    return(0);
}

void collision(NativeObject *o1, NativeObject *o2) {
    float zero[] = {0.0f, 0.0f, 0.0f};
    int i;

    if ((o1->m == 0) || (o2->m == 0) || (o1->m == -1) || (o2->m == -1))
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
    int *ct1 = new int[o1->nbtriangle];
    int ct1l = 0;
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
    int *ct2 = new int[o2->nbtriangle];
    int ct2l = 0;
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
    edgecollision(o1, ct1l, ct1, o2, ct2l, ct2);
}

int physics(Impact *fimpact) {
    float VO1[3];
    float PS1[3];
    float RA1[3];
    float RS1;
    float RC1[3];
    float VO2[3];
    float PS2[3];
    float RA2[3];
    float RS2;
    float RC2[3];
    __android_log_print(ANDROID_LOG_ERROR, "TRACKERS", "--------------------------------O1------%d\n",fimpact->o1);
    __android_log_print(ANDROID_LOG_ERROR, "TRACKERS", "--------------------------------O2------%d\n",fimpact->o2);

    impact(fimpact->o1, fimpact->o2, &fimpact->o1->mvnormals[4 * fimpact->nid1], &fimpact->o2->mvnormals[4 * fimpact->nid2], VO1, RA1, &RS1, RC1,fimpact->point);
    impact(fimpact->o2, fimpact->o1, &fimpact->o2->mvnormals[4 * fimpact->nid2], &fimpact->o1->mvnormals[4 * fimpact->nid1], VO2, RA2, &RS2, RC2,fimpact->point);

    RS1 = (float) sqrt(RA1[0]*RA1[0]+RA1[1]*RA1[1]+RA1[2]*RA1[2]) * (180.0/3.14926);
    if(RS1 < 0.001f){
        RA1[0] = 0.0f;
        RA1[1] = 0.0f;
        RA1[2] = 0.0001f;
        RS1= 0.0f;
    }

    __android_log_print(ANDROID_LOG_ERROR, "TRACKERS", "--------------------------------O1------%d\n",fimpact->o1);
    fimpact->o1->positionspeed[0] = (jfloat)VO1[0];
    fimpact->o1->positionspeed[1] = (jfloat)VO1[1];
    fimpact->o1->positionspeed[2] = (jfloat)VO1[2];
    fimpact->o1->rotationaxis[0]  = (jfloat)RA1[0];
    fimpact->o1->rotationaxis[1]  = (jfloat)RA1[1];
    fimpact->o1->rotationaxis[2]  = (jfloat)RA1[2];
    fimpact->o2->rotationspeed    = (jfloat)RS1;


    RS2 = (float) sqrt(RA2[0]*RA2[0]+RA2[1]*RA2[1]+RA2[2]*RA2[2]) * (180.0/3.14926);
    if(RS2 < 0.001f){
        RA2[0] = 0.0f;
        RA2[1] = 0.0f;
        RA2[2] = 0.0001f;
        RS2= 0.0f;
    }

    __android_log_print(ANDROID_LOG_ERROR, "TRACKERS", "--------------------------------O2------%d\n",fimpact->o2);
    fimpact->o2->positionspeed[0] = (jfloat)VO2[0];
    fimpact->o2->positionspeed[1] = (jfloat)VO2[1];
    fimpact->o2->positionspeed[2] = (jfloat)VO2[2];
    fimpact->o2->rotationaxis[0]  = (jfloat)RA2[0];
    fimpact->o2->rotationaxis[1]  = (jfloat)RA2[1];
    fimpact->o2->rotationaxis[2]  = (jfloat)RA2[2];
    fimpact->o2->rotationspeed    = (jfloat)RS2;
    return(1);
}

void collision() {
    NativeObject *o1;
    NativeObject *o2;
    int i,j,k;

    // first generate all impacts
    gnbimpact=0;
    for (i=0;i<objects.size();i++) {
        o1 = objects[i];
        for (j = i; j < objects.size(); j++) {
            o2 = objects[j];
            if (o1 != o2) {
                collision(o1, o2);
            }
        }
    }

    // then compute collision
    // equilibrium law : do this until there is no more shock
    //for(j=0;j<10;j++) // 10 times max
    for (i=0;i<gnbimpact;i++) {
        __android_log_print(ANDROID_LOG_ERROR, "TRACKERS", "--------------------------------------%d\n",i);
            physics(&gimpacts[i]);
    }
}

NativeObject::NativeObject() {
}
