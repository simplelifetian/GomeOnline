/*
 * log.hpp
 *
 *  Created on: Oct 31, 2012
 *      Author: yaochangwei
 */
#ifndef _LOG_HPP_
#define _LOG_HPP_

#define DEBUG 0

#ifndef TAG
#define TAG "pinyin"
#endif

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

#endif
