#include <jni.h>
#include <string>
#include "NativeObject.h"

extern "C"
JNIEXPORT void JNICALL
Java_com_example_yves_missglndk_MISObject_addnativeobject(JNIEnv *env, jobject instance, jstring name_,
                                         jint nbtriangle, jfloatArray barycenter_, jfloat bbray,
                                         jfloatArray centers_, jfloatArray ray_, jfloatArray normals_,
                                         jfloatArray vertices_, jfloat m, jfloat elasticity) {
    const char *name = env->GetStringUTFChars(name_, 0);
    jfloat *barycenter = env->GetFloatArrayElements(barycenter_, NULL);
    jfloat *centers = env->GetFloatArrayElements(centers_, NULL);
    jfloat *normals = env->GetFloatArrayElements(normals_, NULL);
    jfloat *ray = env->GetFloatArrayElements(ray_, NULL);
    jfloat *vertices = env->GetFloatArrayElements(vertices_, NULL);

    addobject(name, nbtriangle, barycenter, bbray, centers, ray, normals, vertices, m, elasticity);

    env->ReleaseStringUTFChars(name_, name);
    env->ReleaseFloatArrayElements(barycenter_, barycenter, 0);
    env->ReleaseFloatArrayElements(centers_, centers, 0);
    env->ReleaseFloatArrayElements(normals_, normals, 0);
    env->ReleaseFloatArrayElements(vertices_, vertices, 0);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_yves_missglndk_MISObject_updatenativeobject(JNIEnv *env, jobject instance, jstring name_,
                                            jfloatArray modelmatrix_, jfloatArray positionspeed_, jfloat rotationspeed,
                                         jfloatArray rotationaxis_) {
    const char *name = env->GetStringUTFChars(name_, 0);
    jfloat *positionspeed = env->GetFloatArrayElements(positionspeed_, NULL);
    jfloat *rotationaxis = env->GetFloatArrayElements(rotationaxis_, NULL);
    jfloat *modelmatrix= env->GetFloatArrayElements(modelmatrix_, NULL);

    updateobject(name, modelmatrix, positionspeed, rotationspeed, rotationaxis);

    env->ReleaseStringUTFChars(name_, name);
    env->ReleaseFloatArrayElements(positionspeed_, positionspeed, 0);
    env->ReleaseFloatArrayElements(rotationaxis_, rotationaxis, 0);
    env->ReleaseFloatArrayElements(modelmatrix_, modelmatrix, 0);
}

extern "C"
JNIEXPORT jfloatArray  JNICALL
Java_com_example_yves_missglndk_MISObject_getpositionspeed(JNIEnv *env, jobject instance, jstring name_)
{
    jfloatArray result;
    jfloat positionspeed[3];
    const char *name = env->GetStringUTFChars(name_, 0);
    result = env->NewFloatArray(sizeof(positionspeed));

    getpositionspeed(name, positionspeed, sizeof(positionspeed));

    env->SetFloatArrayRegion(result, 0, sizeof(positionspeed), positionspeed);

    env->ReleaseStringUTFChars(name_, name);
    return result;
}

extern "C"
JNIEXPORT jfloatArray  JNICALL
Java_com_example_yves_missglndk_MISObject_getrotationaxis(JNIEnv *env, jobject instance, jstring name_)
{
    jfloatArray result;
    jfloat rotationaxis[3];
    const char *name = env->GetStringUTFChars(name_, 0);
    result = env->NewFloatArray(sizeof(rotationaxis));

    getrotationaxis(name, rotationaxis, sizeof(rotationaxis));

    env->SetFloatArrayRegion(result, 0, sizeof(rotationaxis), rotationaxis);

    env->ReleaseStringUTFChars(name_, name);
    return result;
}

extern "C"
JNIEXPORT jfloat JNICALL
Java_com_example_yves_missglndk_MISObject_getrotationspeed(JNIEnv *env, jobject instance, jstring name_)
{
    const char *name = env->GetStringUTFChars(name_, 0);

    jfloat rotationspeed = getrotationspeed(name);

    env->ReleaseStringUTFChars(name_, name);
    return rotationspeed;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_yves_missglndk_MISCollision_collision__Ljava_lang_String_2Ljava_lang_String_2(JNIEnv *env,
                                                                              jobject instance,
                                                                              jstring n1_,
                                                                              jstring n2_) {
    const char *n1 = env->GetStringUTFChars(n1_, 0);
    const char *n2 = env->GetStringUTFChars(n2_, 0);

    collision(n1, n2);

    env->ReleaseStringUTFChars(n1_, n1);
    env->ReleaseStringUTFChars(n2_, n2);
}

