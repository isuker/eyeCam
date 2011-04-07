/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class ch_hsr_eyecam_colormodel_ColorTransform */

#ifndef _Included_ch_hsr_eyecam_colormodel_ColorTransform
#define _Included_ch_hsr_eyecam_colormodel_ColorTransform
#ifdef __cplusplus
extern "C" {
#endif
#undef ch_hsr_eyecam_colormodel_ColorTransform_COLOR_EFFECT_NONE
#define ch_hsr_eyecam_colormodel_ColorTransform_COLOR_EFFECT_NONE 0L
#undef ch_hsr_eyecam_colormodel_ColorTransform_COLOR_EFFECT_SIMULATE
#define ch_hsr_eyecam_colormodel_ColorTransform_COLOR_EFFECT_SIMULATE 1L
#undef ch_hsr_eyecam_colormodel_ColorTransform_COLOR_EFFECT_FALSE_COLORS
#define ch_hsr_eyecam_colormodel_ColorTransform_COLOR_EFFECT_FALSE_COLORS 2L
#undef ch_hsr_eyecam_colormodel_ColorTransform_COLOR_EFFECT_INTENSIFY_DIFFERENCE
#define ch_hsr_eyecam_colormodel_ColorTransform_COLOR_EFFECT_INTENSIFY_DIFFERENCE 3L
#undef ch_hsr_eyecam_colormodel_ColorTransform_COLOR_EFFECT_BLACK
#define ch_hsr_eyecam_colormodel_ColorTransform_COLOR_EFFECT_BLACK 4L
/*
 * Class:     ch_hsr_eyecam_colormodel_ColorTransform
 * Method:    setEffect
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_ch_hsr_eyecam_colormodel_ColorTransform_setEffect
  (JNIEnv *, jclass, jint);

/*
 * Class:     ch_hsr_eyecam_colormodel_ColorTransform
 * Method:    setPartialEffect
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_ch_hsr_eyecam_colormodel_ColorTransform_setPartialEffect
  (JNIEnv *, jclass, jint);

/*
 * Class:     ch_hsr_eyecam_colormodel_ColorTransform
 * Method:    transformImageToBitmap
 * Signature: ([BIILandroid/graphics/Bitmap;)V
 */
JNIEXPORT void JNICALL Java_ch_hsr_eyecam_colormodel_ColorTransform_transformImageToBitmap
  (JNIEnv *, jclass, jbyteArray, jint, jint, jobject);

/*
 * Class:     ch_hsr_eyecam_colormodel_ColorTransform
 * Method:    transformImageToBuffer
 * Signature: ([BII[B)V
 */
JNIEXPORT void JNICALL Java_ch_hsr_eyecam_colormodel_ColorTransform_transformImageToBuffer
  (JNIEnv *, jclass, jbyteArray, jint, jint, jbyteArray);

#ifdef __cplusplus
}
#endif
#endif
