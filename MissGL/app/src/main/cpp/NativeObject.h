//
// Created by Yves_K1 on 11/12/2018.
//

#ifndef MISSGL_NATIVEOBJECT_H
#define MISSGL_NATIVEOBJECT_H


#include <jni.h>
#include <string>

void addobject(const char *name, jint nbtriangle, jfloat *barycenter, jfloat bbray, jfloat *centers, jfloat *ray, jfloat *normals, jfloat *vertices, jfloat m, jfloat elasticity, jfloat friction);
void updateobject(const char *name, jfloat *modelmatrix, jfloat *positionspeed, jfloat rotationspeed, jfloat *rotationaxis, jfloat *rotationcenter);
void getpositionspeed(const char *n, jfloat *positionspeed, int size);
void getrotationaxis(const char *n, jfloat *rotationaxis, int size);
void getrotationcenter(const char *n, jfloat *rotationcenter, int size);
jfloat getrotationspeed(const char *n);
jfloat getrotationacceleration(const char *n);
jboolean getcollisionstate(const char *n);
void collision();

class NativeObject {
public:
    NativeObject();
    NativeObject(const std::string &name);
public:
    jint nbtriangle;
    jfloat bbray;
    std::string name;
    jfloat *barycenter;
    jfloat *centers;
    jfloat *normals;
    jfloat *vertices;
    jfloat *ray;
    jfloat *mvbarycenter;
    jfloat *mvcenters;
    jfloat *mvnormals;
    jfloat *mvvertices;
    jfloat modelmatrix[16];
    jfloat positionspeed[3];
    jfloat newpositionspeed[3];
    jfloat rotationaxis[3];
    jfloat newrotationaxis[3];
    jfloat rotationcenter[3];
    jfloat newrotationcenter[3];
    jfloat rotationspeed;
    jfloat newrotationspeed;
    jfloat rotationacceleration;
    jfloat elasticity;
    jfloat friction;
    jfloat m;
    jboolean collision;
    jint nbimpact;
};

class Impact {
public:
    NativeObject *o1;
    NativeObject *o2;
    jint nid1;
    jint nid2;
    jfloat point[3];
};

#endif //MISSGL_NATIVEOBJECT_H
