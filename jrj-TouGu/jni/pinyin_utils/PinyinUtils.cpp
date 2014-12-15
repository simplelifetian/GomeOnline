/*
 * PinyinUtils.cpp
 *  JNI桥接
 *  Created on: Oct 31, 2012
 *      Author: yaochangwei
 */
#include <jni.h>
#include <stdlib.h>
#include <string.h>

#include <string>

#include "PinyinEngine.hpp"

#define TAG "pinyin"
#define DEBUG 1
#include "log.hpp"

#define ASSET_PATH "/data/data/com.snda.youni/files/pinyin.db"

using namespace std;
#ifdef __cplusplus
extern "C" {
#endif

PinyinTool *mPinyinTool = NULL;

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
	JNIEnv* env;
	if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
		return -1;
	}
	D("pinyin search module onload");
	return JNI_VERSION_1_6;
}

jint Java_com_snda_youni_jni_contacts_ContactSearchEngine_newEngine(JNIEnv *env,
		jobject thiz) {
	I("reset engine in.");
	PinyinEngine *p_engine = new PinyinEngine(ASSET_PATH);
	I("reset engine out.");
	return (jint) p_engine;
}

void Java_com_snda_youni_jni_contacts_ContactSearchEngine_deleteEngine(
		JNIEnv *env, jobject thiz, jint pEngine) {
	if(pEngine == 0) {
		return;
	}
	delete (PinyinEngine*) pEngine;
}

void insertItem(JNIEnv *env, jobject thiz, jstring display_name, jstring phone,
		int index, PinyinEngine *engine) {
	I("insert item in");
	if (engine == NULL) {
		E("insert item with the engine NULL! fatal error.!");
	}
	const jchar *display_name_array = env->GetStringChars(display_name, NULL);
	const jsize size = env->GetStringLength(display_name);
	const char *phone_cstr = env->GetStringUTFChars(phone, NULL);
	const char *display_name_cstr = env->GetStringUTFChars(display_name, NULL);
		
	Record *record = new Record(engine, (unsigned short*) display_name_array,
			(int) size, display_name_cstr, phone_cstr, index);

	engine->insertRecord(record);

	env->ReleaseStringChars(display_name, display_name_array);
	env->ReleaseStringUTFChars(phone, phone_cstr);
	I("insert item out");
}

void Java_com_snda_youni_jni_contacts_ContactSearchEngine_insertItem(
		JNIEnv *env, jobject thiz, jint p_engine, jstring display_name,
		jstring phone, int index) {
	insertItem(env, thiz, display_name, phone, index, (PinyinEngine*) p_engine);
}

jlongArray matchPinyin(JNIEnv *env, jobject thiz, jstring input,
		PinyinEngine *engine) {
	const char *input_cstr = env->GetStringUTFChars(input, NULL);
	int len = strlen(input_cstr);
	if(0 == len) {
		return 0;
	}
	
	const jchar *input_array = env->GetStringChars(input, NULL);
	const jsize size = env->GetStringLength(input);
	bool is_ch = false;
	for (int i = 0; i < size; i++) {
		if (IS_CH(input_array[i])) {
			is_ch = true;
			break;
		}
	}
	D("is ch: %d", is_ch);
	string tmp = input_cstr;
	jlongArray array = NULL;
	if (is_ch) {
		array = engine->computeFull(env, tmp);
	} else {
		array = engine->computePinyin(env, tmp);
	}
	
	D("oh my god!");
	env->ReleaseStringChars(input, input_array);
	env->ReleaseStringUTFChars(input, input_cstr);
	return array;
}

jlongArray Java_com_snda_youni_jni_contacts_ContactSearchEngine_matchPinyin(
		JNIEnv *env, jobject thiz, jint p_engine, jstring input) {
	return matchPinyin(env, thiz, input, (PinyinEngine*) p_engine);
}

jlongArray matchDigit(JNIEnv *env, jobject thiz, jstring input,
		PinyinEngine *engine) {
	const char *input_cstr = env->GetStringUTFChars(input, NULL);
	string tmp = input_cstr;
	jlongArray array = engine->computeDigit(env, tmp);
	env->ReleaseStringUTFChars(input, input_cstr);
	return array;
}

jlongArray Java_com_snda_youni_jni_contacts_ContactSearchEngine_matchDigit(
		JNIEnv *env, jobject thiz, jint p_engine, jstring input) {
	return matchDigit(env, thiz, input, (PinyinEngine*) p_engine);
}

jobjectArray Java_com_snda_youni_jni_contacts_ContactSearchEngine_getPinyin(
		JNIEnv *env, jobject thiz, jint p_engine, jint position, jlong tag) {
	Record *record = ((PinyinEngine*) p_engine)->getRecord(position);			
	return record->getPinyin(env, (long long)tag);
}

jstring Java_com_snda_youni_jni_contacts_ContactSearchEngine_getPhone(
		JNIEnv *env, jobject thiz, jint p_engine, jint position) {
	return ((PinyinEngine*) p_engine)->getRecord(position)->getPhone(env);
}

jlong Java_com_snda_youni_jni_contacts_ContactSearchEngine_test(JNIEnv *env, 
		jobject thiz, jstring jinput, jstring jmatch) {
	unsigned short start = (unsigned short) 4;
	unsigned short end = (unsigned short) (start + 3);
	long long tmp = 0xff00ff0000000000L | end << 16 | start;
	return tmp;
}

jobjectArray Java_com_snda_youni_jni_contacts_PinyinTool_getPinyinArray(JNIEnv *env,
		jobject thiz, jstring input) {

	if (mPinyinTool == NULL) {
		mPinyinTool = new PinyinTool();
	}
	
	return mPinyinTool->getPinyinArray(env, input);
}

jobject Java_com_snda_youni_jni_contacts_PinyinTool_getPinyinParts(JNIEnv *env,
	   jobject thiz, jstring input) {
	if (mPinyinTool == NULL) {
		mPinyinTool = new PinyinTool();
	}
	
	return mPinyinTool->getPinyinParts(env, input);   
}


#ifdef __cplusplus
}
#endif

