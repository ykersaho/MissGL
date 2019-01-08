//
// Created by Yves_K1 on 11/12/2018.
//

#ifndef MISSGL_NATIVEOBJECT_H
#define MISSGL_NATIVEOBJECT_H


#include <jni.h>
#include <string>

void addobject(const char *name, jint nbtriangle, jfloat *barycenter, jfloat bbray, jfloat *centers, jfloat *ray, jfloat *normals, jfloat *vertices, jfloat m, jfloat elasticity);
void updateobject(const char *name, jfloat *modelmatrix, jfloat *positionspeed, jfloat rotationspeed, jfloat *rotationaxis);
void getpositionspeed(const char *n, jfloat *positionspeed, int size);
void getrotationaxis(const char *n, jfloat *rotationaxis, int size);
jfloat getrotationspeed(const char *n);
void collision(const char *n1, const char *n2);

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
    jfloat rotationaxis[3];
    jfloat rotationspeed;
    jfloat elasticity;
    jfloat m;
};


#endif //MISSGL_NATIVEOBJECT_H
