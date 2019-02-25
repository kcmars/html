#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_keyc_jnidemo_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_keyc_jnidemo_JniUtils_setStringJni(JNIEnv *env, jclass type) {

    // TODO

    std::string hello = "Jni from C++";
    return env->NewStringUTF(hello.c_str());
}
const char keyValue[] = {
        21, 25, 21, -45, 25, 98, -55, -45, 10, 35, -45, 35,
        26, -5, 25, -65, -78, -99, 85, 45, -5, 10, -0, 11,
        -35, -48, -98, 65, -32, 14, -67, 25, 36, -56, -45, -5,
        12, 15, 35, -15, 25, -14, 62, -25, 33, -45, 55, 12, -8
};

const char iv[] =  {    //16 bit
        -33, 32, -25, 25, 35, -27, 55, -12, -15,32,
        23, 45, -26, 32, 5,16
};

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_keyc_jnidemo_JniUtils_getKey(JNIEnv *env, jclass type) {

    // TODO
    jbyteArray array = env->NewByteArray(sizeof(keyValue));
    jbyte *jbyte1 = env->GetByteArrayElements(array, 0);
    jint i = 0;
    for (i = 0; i < sizeof(keyValue); ++i) {
        jbyte1[i] = (jbyte)i;
    }
    env->GetByteArrayRegion(array, 0, sizeof(keyValue), jbyte1);
    return array;
}

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_keyc_jnidemo_JniUtils_getIv(JNIEnv *env, jclass type) {

    // TODO
    jbyteArray array = env->NewByteArray(sizeof(iv));
    jbyte *jbyte1 = env->GetByteArrayElements(array, 0);
    jint i = 0;
    for (i = 0; i < sizeof(iv); ++i) {
        jbyte1[i] = (jbyte)i;
    }
    env->GetByteArrayRegion(array, 0, sizeof(iv), jbyte1);
    return array;
}