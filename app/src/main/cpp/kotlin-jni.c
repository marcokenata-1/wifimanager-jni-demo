#include <string.h>
#include <jni.h>

JNIEXPORT jint JNICALL Java_com_marcokenata_wifijnidemo_MainActivity_sumTwoValues
  (JNIEnv * env, jobject obj, jint value1, jint value2) {
        return (value1 + value2);
}