#include <jni.h>
#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <string.h>
#include <unistd.h>

#include "sha256.h"

#define DEBUG 1

#define TAG "security_native"


#if DEBUG
#include <android/log.h>
#define V(x...) __android_log_print(ANDROID_LOG_VERBOSE, TAG, x)
#define I(x...) __android_log_print(ANDROID_LOG_INFO, TAG, x)
#define D(x...) __android_log_print(ANDROID_LOG_DEBUG, TAG, x)
#define W(x...) __android_log_print(ANDROID_LOG_WARN, TAG, x)
#define E(x...) __android_log_print(ANDROID_LOG_ERROR, TAG, x)
#else
#define V(...) do{} while(0)
#define I(...) do{} while(0)
#define D(...) do{} while(0)
#define W(...) do{} while(0)
#define E(...) do{} while(0)
#endif

#define GET_SIGNATURES 0x00000040

#define SHA_256_LEN 32

#define DES_KEY_LEN 24

#define VALID_PACAKGE_NAME "com.snda.youni"

char DEBUG_SIGNATRUE_SHA256[] = {0x0e, 0x34, 0x84, 0x70, 0xdc, 0xe5, 0x93, 0x3c, 
								 0xea, 0xf1, 0x57, 0x87, 0xed, 0x93, 0xe0, 0x49, 
								 0x83, 0x68, 0x48, 0xb5, 0x23, 0x20, 0xbb, 0x60, 
								 0xe5, 0x7b, 0x03, 0x5c, 0x4e, 0xc4, 0xa2, 0x99};

char RELEASAE_SIGNATRUE_SHA256[] = {0x9e, 0x55, 0xd0, 0x2c, 0xd1, 0xea, 0x1d, 0x9c, 
									0xc5, 0xe4, 0x22, 0xd5, 0x0d, 0x11, 0xc2, 0x70, 
									0x52, 0xc5, 0x35, 0xde, 0x57, 0xa6, 0xd4, 0xe6, 
									0xfd, 0xe5, 0xb0, 0x11, 0xea, 0xd7, 0x07, 0x30};

time_t keyword = 0;

inline int isEquals(char* l, char* r, int len)
{
	int i = 0;
	for(i = 0; i < len; i++) {
		if(l[i] != r[i]){
			return 0;
		}
	}
	return 1;
}

inline unsigned char* sha256(char* input, int len)
{
	sha256_context ctx;
    unsigned char *sha256sum = (unsigned char*)malloc(sizeof(char) * SHA_256_LEN);
    sha256_starts(&ctx);
    sha256_update(&ctx, input, strlen(input));
    sha256_finish(&ctx, sha256sum);
    return sha256sum;
}

inline const char* getPackageName(JNIEnv *env, jobject context)
{
	jclass contextClass = (*env)->GetObjectClass(env, context);
	if(error_check(env) || contextClass == NULL){
		return NULL;
	}

	jmethodID getPackageNameMethodID = (*env)->GetMethodID(env, contextClass, "getPackageName", "()Ljava/lang/String;");
	if(error_check(env)){
		return NULL;
	}

	jstring packageName = (*env)->CallObjectMethod(env, context, getPackageNameMethodID);
	if(packageName == NULL){
		return NULL;
	}

	const char* packageNameChar = (*env)->GetStringUTFChars(env, packageName, 0);
	return packageNameChar;
}

inline char* getSignature(JNIEnv *env, jobject context, int* len)
{
	jclass contextClass = (*env)->GetObjectClass(env, context);
	if(error_check(env) || contextClass == NULL){
		return NULL;
	}

	jmethodID getPackageNameMethodID = (*env)->GetMethodID(env, contextClass, "getPackageName", "()Ljava/lang/String;");
	if(error_check(env)){
		return NULL;
	}

	jstring packageName = (*env)->CallObjectMethod(env, context, getPackageNameMethodID);
	if(packageName == NULL){
		return NULL;
	}

	const char* packageNameChar = (*env)->GetStringUTFChars(env, packageName, 0);

	jmethodID getPackageManagerMethodID = (*env)->GetMethodID(env, contextClass, "getPackageManager", "()Landroid/content/pm/PackageManager;");
	if(error_check(env)){
		return NULL;
	}

	jobject packageManager = (*env)->CallObjectMethod(env, context, getPackageManagerMethodID);
	jclass packageManagerClass = (*env)->GetObjectClass(env, packageManager);


	jmethodID getPackageInfoMethodID = (*env)->GetMethodID(env, packageManagerClass, "getPackageInfo", "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
	if(error_check(env)){
		return NULL;
	}

	jobject packageInfo = (*env)->CallObjectMethod(env, packageManager, getPackageInfoMethodID, packageName, GET_SIGNATURES);
	if(packageInfo == NULL){
		return NULL;
	}

	jclass packageInfoClass = (*env)->GetObjectClass(env, packageInfo);
	jfieldID signatureFieldID = (*env)->GetFieldID(env, packageInfoClass, "signatures", "[Landroid/content/pm/Signature;");
	jobjectArray signatures = (*env)->GetObjectField(env, packageInfo, signatureFieldID);
	int size = (*env)->GetArrayLength(env, signatures);
	if(size == 0){
		return NULL;
	}

	jobject signature = (*env)->GetObjectArrayElement(env, signatures, 0);
	jclass signatureClass = (*env)->GetObjectClass(env, signature);
	jmethodID toByteArrayMethodID = (*env)->GetMethodID(env, signatureClass, "toByteArray", "()[B");
	if(error_check(env)){
		return NULL;
	}
	jbyteArray signatureByteArray= (*env)->CallObjectMethod(env, signature, toByteArrayMethodID);
	int byteArrayLen = (*env)->GetArrayLength(env, signatureByteArray);
	char* signatureBytes = (char*)malloc(byteArrayLen);
	(*env)->GetByteArrayRegion(env, signatureByteArray, 0, byteArrayLen, signatureBytes);
	*len = byteArrayLen;
	return signatureBytes;
}

inline int isValidPackage(JNIEnv *env, jobject context)
{	
	const char* packageNameChar = getPackageName(env, context);
	if(packageNameChar == NULL){
		return 0;
	}

	//package name validation
	if(strcmp(packageNameChar, VALID_PACAKGE_NAME) != 0){
		D("package name check failed");
		return 0;
	}

	D("package name check pass");

	//signature validation
	int len = 0;
	char* appSignatrue = getSignature(env, context, &len);
	char* signatrueSha256 = sha256(appSignatrue, len);
	
	if(isEquals(signatrueSha256, DEBUG_SIGNATRUE_SHA256, SHA_256_LEN) || isEquals(signatrueSha256, RELEASAE_SIGNATRUE_SHA256, SHA_256_LEN)){
		D("signature check pass!!");		
		return 1;
	}else{
		D("signature check failed!!");
		return 0;
	}
}

jbyteArray Java_com_snda_youni_jni_AppInfo_getSignatrueSha256(JNIEnv *env, jobject thiz, jobject context)
{
	int len = 0;
	char* signature = getSignature(env, context, &len);
	if(signature == NULL){
		D("signature get failed.");
		return NULL;
	}

	char* sha256sum = sha256(signature, len);
	jbyteArray array = (*env)->NewByteArray(env, SHA_256_LEN);
	(*env)->SetByteArrayRegion(env, array, 0, SHA_256_LEN, sha256sum);
	return array;
}

jbyteArray Java_com_snda_youni_jni_AppInfo_getAppLabel(JNIEnv *env, jobject thiz, jobject context)
{	

	if(!isValidPackage(env, context)){
		return NULL;
	}

	int i = 0;
	char* ret = (char*)malloc(DES_KEY_LEN);
	for(i = 0; i < 6; i++){
		ret[i] = '1' + i;
	}

	ret[18] = '!';
	ret[19] = '@';
	ret[20] = '#';
	ret[21] = '$';
	ret[22] = '%';
	ret[23] = '^';
	ret[6] = 'h';
	ret[7] = 'u';
	ret[8] = 'r';
	ret[9] = ret[8];
	ret[10] = 'a';
	ret[11] = 'y';

	for(i = 12; i < 18; i++){
		ret[i] = ret[i - 6] + ('A' - 'a');
	}

	jbyteArray array = (*env)->NewByteArray(env, DES_KEY_LEN);
	(*env)->SetByteArrayRegion(env, array, 0, DES_KEY_LEN, ret);
	return array;
}

jbyteArray Java_com_snda_youni_jni_AppInfo_sha256(JNIEnv* env, jobject thiz, jbyteArray input)
{
	char* byteArray = (*env)->GetByteArrayElements(env, input, 0);
	const int len = (*env)->GetArrayLength(env, input);
	const unsigned char* sha256sum = sha256(byteArray, len);
	jbyteArray array = (*env)->NewByteArray(env, SHA_256_LEN);	
	(*env)->SetByteArrayRegion(env, array, 0, SHA_256_LEN, sha256sum);
	return array;
}

jbyteArray Java_com_snda_youni_jni_AppInfo_getAppInfo(JNIEnv *env, jobject thiz, jobject context) 
{
	if(!isValidPackage(env, context)){
		return NULL;
	}

	const char* packageNameChar = getPackageName(env, context);

	char appDataPath[1024];
	memset(appDataPath, 0, 1024);
	const char* dataPathFormat = "/data/data/%s/appinfo";
	sprintf(appDataPath, dataPathFormat, packageNameChar);

	if(access(appDataPath, F_OK) == -1) {
		FILE *appinfoFile = fopen(appDataPath, "w+");
		time_t rawtime;
		time(&rawtime);
		keyword = rawtime;
		fwrite(&rawtime, sizeof(time_t), 1, appinfoFile);
		fclose(appinfoFile);
	}

	if(keyword == 0){
		FILE *appinfoFile = fopen(appDataPath, "r");
		time_t rawtime;
		fread(&rawtime, sizeof(time_t), 1, appinfoFile);
		fclose(appinfoFile);
		keyword = rawtime;
	}

	unsigned char* sha256Value = sha256((char*)(&keyword), sizeof(time_t));
	char* desKey = (char*)malloc(DES_KEY_LEN);
	memcpy(desKey, sha256Value + 24, 8);
	memcpy(desKey + 8, sha256Value, 8);
	memcpy(desKey + 16, sha256Value + 16, 8);

	D("the key factory: %d", keyword * 2);

	jbyteArray array = (*env)->NewByteArray(env, DES_KEY_LEN);	
	(*env)->SetByteArrayRegion(env, array, 0, DES_KEY_LEN, desKey);
	return array;
}

int error_check(JNIEnv *env)
{
	if ((*env)->ExceptionCheck(env)) {
 		return 1;
 	}

 	return 0;
}