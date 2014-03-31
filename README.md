tesstwoapp
==========

Tess-two library and a tess app finally setup in gradle 1.10 android studio 0.4.6 (if it matters).android sdk 19

Tess-two was working correctly on eclipse but android studio is causing some errors. It looks like a known error. 
Please see if you guys can replicate and find a solution :

/the error is in TessBaseAPI class System.loadlibrary();
jni/com_googlecode_leptonica_android/readfile.cpp: In function 'jint Java_com_googlecode_leptonica_android_ReadFile_nativeReadFiles(JNIEnv*, jclass, jstring, jstring)':
