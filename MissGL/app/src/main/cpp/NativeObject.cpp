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
    memcpy(positionspeed, o->newpositionspeed, size);
}

void getrotationaxis(const char *n, jfloat *rotationaxis, int size) {
    NativeObject *o = hmap[std::string(n)];
    memcpy(rotationaxis, o->newrotationaxis, size);
}

void getrotationcenter(const char *n, jfloat *rotationcenter, int size) {
    NativeObject *o = hmap[std::string(n)];
    memcpy(rotationcenter, o->newrotationcenter, size);
}

jfloat getrotationspeed(const char *n) {
    NativeObject *o = hmap[std::string(n)];
    return(o->newrotationspeed);
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
    obj->impacts = new NativeObject::impact[1000];
    obj->nbimpacts= 0;
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
    o->nbimpacts = 0;
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
    for(i=0;i<o1->nbimpacts;i++) {
        float dx= cc[0] - o1->impacts[i].point[0];
        float dy= cc[1] - o1->impacts[i].point[1];
        float dz= cc[2] - o1->impacts[i].point[2];
        if(dx*dx+dy*dy+dz*dz < 0.0001)
            return;
    }
    o1->impacts[o1->nbimpacts].point[0] = cc[0];
    o1->impacts[o1->nbimpacts].point[1] = cc[1];
    o1->impacts[o1->nbimpacts].point[2] = cc[2];
    o1->impacts[o1->nbimpacts].nid = idj;
    o1->impacts[o1->nbimpacts].o = o2;
    o1->nbimpacts++;
    o1->collision = true;
    o2->impacts[o2->nbimpacts].point[0] = cc[0];
    o2->impacts[o2->nbimpacts].point[1] = cc[1];
    o2->impacts[o2->nbimpacts].point[2] = cc[2];
    o2->impacts[o2->nbimpacts].nid = idi;
    o2->impacts[o2->nbimpacts].o = o1;
    o2->nbimpacts++;
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

void impact(NativeObject *o1, NativeObject *o2, float *n2, float *VOUT, float *RA, float *RS, float *RC, float *ccc) {
    float G[3];
    float VR[3];
    float V1[3];
    float V2[3];
    float V1X[3];
    float V1Y[3];
    float N[3];
    float VP[3];
    float VP1[3];
    float lv1,lg,ln,lvp;
    memcpy(V1, o1->positionspeed, sizeof(V1));
    memcpy(V2, o2->positionspeed, sizeof(V2));
    memcpy(RA, o1->rotationaxis, sizeof(o1->rotationaxis));
    memcpy(RC, o1->rotationcenter, sizeof(o1->rotationcenter));
    *RS=o1->rotationspeed;
    VOUT[0] = V1[0];
    VOUT[1] = V1[1];
    VOUT[2] = V1[2];

    ln=(float) sqrt(n2[0]*n2[0]+n2[1]*n2[1]+n2[2]*n2[2]);
    if(ln > 0.0000001) {
        N[0] = n2[0] / ln;
        N[1] = n2[1] / ln;
        N[2] = n2[2] / ln;
    }

    float s1 = (V1[0]*N[0]+V1[1]*N[1]+V1[2]*N[2]);
    float s2 = (V2[0]*N[0]+V2[1]*N[1]+V2[2]*N[2]);

    if(s1 > s2)
        return; // no impact

    // translation impact
    float e1 = o1->m / (o1->m + o2->m);
    float e2 = o2->m / (o1->m + o2->m);
    float f = e2 * (s2 - s1) * o1->elasticity + e1 * s1 + e2 * s2;
    float resistance = (1.0f-o1->friction*o2->friction);
    V1X[0] = s1 * N[0];
    V1X[1] = s1 * N[1];
    V1X[2] = s1 * N[2];
    V1Y[0] = V1[0] - s1 * N[0];
    V1Y[1] = V1[1] - s1 * N[1];
    V1Y[2] = V1[2] - s1 * N[2];

    VOUT[0] = f*N[0] + resistance*V1Y[0];
    VOUT[1] = f*N[1] + resistance*V1Y[1];
    VOUT[2] = f*N[2] + resistance*V1Y[2];


    // rotation impact
    G[0] = o1->mvbarycenter[0] - ccc[0];
    G[1] = o1->mvbarycenter[1] - ccc[1];
    G[2] = o1->mvbarycenter[2] - ccc[2];
    lg=(float) sqrt(G[0]*G[0]+G[1]*G[1]+G[2]*G[2]);
    if(lg > 0.0000001) {
        G[0] = G[0]/lg;
        G[1] = G[1]/lg;
        G[2] = G[2]/lg;
    }
    VR[0] = e2*V1X[0] + e2*0.2f*V1Y[0];
    VR[1] = e2*V1X[1] + e2*0.2f*V1Y[1];
    VR[2] = e2*V1X[2] + e2*0.2f*V1Y[2];

    VP[0] = G[1]*VR[2]-G[2]*VR[1];
    VP[1] = G[2]*VR[0]-G[0]*VR[2];
    VP[2] = G[0]*VR[1]-G[1]*VR[0];
    lvp=(float) sqrt(VP[0]*VP[0]+VP[1]*VP[1]+VP[2]*VP[2]);

    if(lvp > 0.0000001f) {
        VP[0] = (VP[0]/lg) * 180.0f / 3.1415926f;;
        VP[1] = (VP[1]/lg) * 180.0f / 3.1415926f;;
        VP[2] = (VP[2]/lg) * 180.0f / 3.1415926f;;
        RA[0] = RA[0]*resistance+VP[0];
        RA[1] = RA[1]*resistance+VP[1];
        RA[2] = RA[2]*resistance+VP[2];
        memcpy(RC, ccc, sizeof(o1->rotationcenter));
    }
}

void impacts(NativeObject *o1) {
    NativeObject *o2;
    int i;
    float VO[3];
    float PS[3];
    float RA[3];
    float RS;
    float RC[3];
    float SRA[3];
    float SRS;
    float SRC[3];
    int nid;

    PS[0]=PS[1]=PS[2]=0.0f;
    SRA[0]=SRA[1]=SRA[2]=0.0f;
    SRC[0]=SRC[1]=SRC[2]=0.0f;
    SRS=0.0f;

    for (i = 0; i < o1->nbimpacts; i++) {
        nid = o1->impacts[i].nid;
        o2 = o1->impacts[i].o;
        impact(o1, o1->impacts[i].o, &o2->mvnormals[4 * nid], VO, RA, &RS, RC,
                   o1->impacts[i].point);
        SRA[0] += RA[0];
        SRA[1] += RA[1];
        SRA[2] += RA[2];
        SRC[0] += RC[0];
        SRC[1] += RC[1];
        SRC[2] += RC[2];
        SRS += RS;
        PS[0] += VO[0];
        PS[1] += VO[1];
        PS[2] += VO[2];
    }
    PS[0] = PS[0] / o1->nbimpacts;
    PS[1] = PS[1] / o1->nbimpacts;
    PS[2] = PS[2] / o1->nbimpacts;
    SRA[0] = SRA[0] / o1->nbimpacts;
    SRA[1] = SRA[1] / o1->nbimpacts;
    SRA[2] = SRA[2] / o1->nbimpacts;
    SRC[0] = SRC[0] / o1->nbimpacts;
    SRC[1] = SRC[1] / o1->nbimpacts;
    SRC[2] = SRC[2] / o1->nbimpacts;
    SRS = SRS / o1->nbimpacts;

    RA[0] = SRA[0];
    RA[1] = SRA[1];
    RA[2] = SRA[2];
    SRA[0] = 0;
    SRA[1] = 0;
    SRA[2] = 0;
    VO[0] = PS[0];
    VO[1] = PS[1];
    VO[2] = PS[2];
    PS[0] = 0;
    PS[1] = 0;
    PS[2] = 0;
    for (i = 0; i< o1->nbimpacts; i++) {
        // verify rotation constraints
        float lr,maxlr,s;
        float r[3];
        float vp[3];
        float *N;
        r[0] = o1->impacts[i].point[0] - SRC[0];
        r[1] = o1->impacts[i].point[1] - SRC[1];
        r[2] = o1->impacts[i].point[2] - SRC[2];
        lr = (float) sqrt(r[0] * r[0] + r[1] * r[1] + r[2] * r[2]);
        nid = o1->impacts[i].nid;
        N = &o1->impacts[i].o->mvnormals[4 * nid];
        SRA[0] += RA[0];
        SRA[1] += RA[1];
        SRA[2] += RA[2];
        PS[0] += VO[0];
        PS[1] += VO[1];
        PS[2] += VO[2];
        // no constraint if points are too close
         if (lr > 0.001) {
             vp[0] = r[1] * RA[2] - r[2] * RA[1];
             vp[1] = r[2] * RA[0] - r[0] * RA[2];
             vp[2] = r[0] * RA[1] - r[1] * RA[0];
             s = vp[0] * N[0] + vp[1] * N[1] + vp[2] * N[2];
             if ((s > 0) && (o1->impacts[i].o->m >= o1->m)) {
                 maxlr = lr;
                 // remove constraint
                 r[0] /= lr;
                 r[1] /= lr;
                 r[2] /= lr;
                 vp[0] = r[1] * N[2] - r[2] * N[1];
                 vp[1] = r[2] * N[0] - r[0] * N[2];
                 vp[2] = r[0] * N[1] - r[1] * N[0];
                 s = RA[0] * vp[0] + RA[1] * vp[1] + RA[2] * vp[2];
                 SRA[0] += - s * vp[0];
                 SRA[1] += - s * vp[1];
                 SRA[2] += - s * vp[2];
                 if (lr > maxlr) {
                     SRC[0] = o1->impacts[i].point[0];
                     SRC[1] = o1->impacts[i].point[1];
                     SRC[2] = o1->impacts[i].point[2];}
                }
            }
            // verify translation constraints
            s = VO[0] * N[0] + VO[1] * N[1] + VO[2] * N[2];
            if ((s < 0.0f) && (o1->impacts[i].o->m >= o1->m)) {
                PS[0] += - s * N[0];
                PS[1] += - s * N[1];
                PS[2] += - s * N[2];
        }
    }

    o1->newpositionspeed[0] = PS[0] / o1->nbimpacts;
    o1->newpositionspeed[1] = PS[1] / o1->nbimpacts;
    o1->newpositionspeed[2] = PS[2] / o1->nbimpacts;
    o1->newrotationaxis[0] = SRA[0] / o1->nbimpacts;
    o1->newrotationaxis[1] = SRA[1] / o1->nbimpacts;
    o1->newrotationaxis[2] = SRA[2] / o1->nbimpacts;
    o1->newrotationcenter[0] = SRC[0];
    o1->newrotationcenter[1] = SRC[1];
    o1->newrotationcenter[2] = SRC[2];
    o1->newrotationspeed = SRS;
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

void collision() {
    NativeObject *o1;
    NativeObject *o2;
    int i,j,k;

    // first generate all impacts
    for (i=0;i<objects.size();i++) {
        o1 = objects[i];
        for (j = 0; j < objects.size(); j++) {
            o2 = objects[j];
            if (o1 != o2) {
                collision(o1, o2);
            }
        }
    }

    // then compute collision
    for (i=0;i<objects.size();i++) {
        o1 = objects[i];
        // ignore if static objects or no collision
        if((o1->m==0) || (o1->m >=1000000) || (o1->m == -1) || (o1->collision == false)) {
            o1->collision = false;
            o1->newpositionspeed[0] = o1->positionspeed[0];
            o1->newpositionspeed[1] = o1->positionspeed[1];
            o1->newpositionspeed[2] = o1->positionspeed[2];
            o1->newrotationaxis[0] = o1->rotationaxis[0];
            o1->newrotationaxis[1] = o1->rotationaxis[1];
            o1->newrotationaxis[2] = o1->rotationaxis[2];
            o1->newrotationcenter[0] = o1->rotationcenter[0];
            o1->newrotationcenter[1] = o1->rotationcenter[1];
            o1->newrotationcenter[2] = o1->rotationcenter[2];
            o1->newrotationspeed = o1->rotationspeed;
            continue;
        }
        o1->newpositionspeed[0] = 0.0f;
        o1->newpositionspeed[1] = 0.0f;
        o1->newpositionspeed[2] = 0.0f;
        o1->newrotationaxis[0] = 0.0f;
        o1->newrotationaxis[1] = 0.0f;
        o1->newrotationaxis[2] = 0.0f;
        o1->newrotationcenter[0] = 0.0f;
        o1->newrotationcenter[1] = 0.0f;
        o1->newrotationcenter[2] = 0.0f;
        o1->newrotationspeed = 0.0f;
        o1->nbcollisions = 0;
        impacts(o1);
        float ra = (float) sqrt(o1->newrotationaxis[0]*o1->newrotationaxis[0]+o1->newrotationaxis[1]*o1->newrotationaxis[1]+o1->newrotationaxis[2]*o1->newrotationaxis[2]);
        o1->newrotationspeed = ra;
        if(ra < 0.001f){
            o1->newrotationaxis[0] = 0.0001f;
            o1->newrotationaxis[1] = 0.0f;
            o1->newrotationaxis[2] = 0.0f;
            o1->newrotationspeed = 0.0f;
        }
    }
}

NativeObject::NativeObject() {
}
