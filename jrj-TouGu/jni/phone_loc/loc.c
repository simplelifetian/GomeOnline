//
//  loc.c
//  loc
//
//  Created by Jerry Yao on 13-5-30.
//  Copyright (c) 2013年 fruitranger inc. All rights reserved.
//

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <jni.h>

#include "loc.h"

#define STEP 16

int s_table[] = { 0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2, 3, 2, 3, 3,
    4, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 1, 2, 2, 3, 2,
    3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4,
    5, 4, 5, 5, 6, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 2,
    3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 2, 3, 3, 4, 3, 4, 4,
    5, 3, 4, 4, 5, 4, 5, 5, 6, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5,
    6, 6, 7, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 2, 3, 3,
    4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 2, 3, 3, 4, 3, 4, 4, 5, 3,
    4, 4, 5, 4, 5, 5, 6, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6,
    7, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 3, 4, 4, 5, 4,
    5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5,
    6, 5, 6, 6, 7, 4, 5, 5, 6, 5, 6, 6, 7, 5, 6, 6, 7, 6, 7, 7, 8 };

unsigned short left_and_pre[] = {0xfffe, 0xfffc, 0xfff8, 0xfff0, 0xffe0, 0xffc0, 0xff80, 0xff00};
unsigned char right_and_pre[] = {0x01, 0x03, 0x07, 0x0f, 0x1f, 0x3f, 0x7f, 0xff};

unsigned char left_and[] = {0xff, 0x7f, 0x3f, 0x1f, 0x0f, 0x07, 0x03, 0x01};
unsigned char right_and[] = {0x80, 0xc0, 0xe0, 0xf0, 0xf8, 0xfc, 0xfe, 0xff};

int get_1_count(short input, int x) {
	input = (short) (input << (16 - x));
	return s_table[(input & 0xff00) >> 8] + s_table[input & 0xff];
}

static unsigned char* get_index_pointer(const unsigned int category)
{
    unsigned char *pointer = NULL;
    switch (category) {
        case 30:
            pointer = index_130;
            break;
        case 31:
            pointer = index_131;
            break;
        case 32:
            pointer = index_132;
            break;
        case 33:
            pointer = index_133;
            break;
        case 34:
            pointer = index_134;
            break;
        case 35:
            pointer = index_135;
            break;
        case 36:
            pointer = index_136;
            break;
        case 37:
            pointer = index_137;
            break;
        case 38:
            pointer = index_138;
            break;
        case 39:
            pointer = index_139;
            break;
        case 45:
            pointer = index_145;
            break;
        case 47:
            pointer = index_147;
            break;
        case 50:
            pointer = index_150;
            break;
        case 51:
            pointer = index_151;
            break;
        case 52:
            pointer = index_152;
            break;
        case 53:
            pointer = index_153;
            break;
        case 55:
            pointer = index_155;
            break;
        case 56:
            pointer = index_156;
            break;
        case 57:
            pointer = index_157;
            break;
        case 58:
            pointer = index_158;
            break;
        case 59:
            pointer = index_159;
            break;
        case 68:
            pointer = index_168;
            break;
        case 75:
            pointer = index_175;
            break;
        case 76:
            pointer = index_176;
            break;
        case 77:
            pointer = index_177;
            break;
        case 80:
            pointer = index_180;
            break;
        case 81:
            pointer = index_181;
            break;
        case 82:
            pointer = index_182;
            break;
        case 83:
            pointer = index_183;
            break;
        case 86:
            pointer = index_186;
            break;
        case 87:
            pointer = index_187;
            break;
        case 88:
            pointer = index_188;
            break;
        case 89:
            pointer = index_189;
            break;
        default:
            break;
    }
    
    return pointer;
}

static inline unsigned int* get_step_pointer(const unsigned int category)
{
    
    unsigned int *pointer = NULL;
    switch (category) {
        case 30:
            pointer = step_130;
            break;
        case 31:
            pointer = step_131;
            break;
        case 32:
            pointer = step_132;
            break;
        case 33:
            pointer = step_133;
            break;
        case 34:
            pointer = step_134;
            break;
        case 35:
            pointer = step_135;
            break;
        case 36:
            pointer = step_136;
            break;
        case 37:
            pointer = step_137;
            break;
        case 38:
            pointer = step_138;
            break;
        case 39:
            pointer = step_139;
            break;
        case 45:
            pointer = step_145;
            break;
        case 47:
            pointer = step_147;
            break;
        case 50:
            pointer = step_150;
            break;
        case 51:
            pointer = step_151;
            break;
        case 52:
            pointer = step_152;
            break;
        case 53:
            pointer = step_153;
            break;
        case 55:
            pointer = step_155;
            break;
        case 56:
            pointer = step_156;
            break;
        case 57:
            pointer = step_157;
            break;
        case 58:
            pointer = step_158;
            break;
        case 59:
            pointer = step_159;
            break;
        case 68:
            pointer = step_168;
            break;
        case 75:
            pointer = step_175;
            break;
        case 76:
            pointer = step_176;
            break;
        case 77:
            pointer = step_177;
            break;
        case 80:
            pointer = step_180;
            break;
        case 81:
            pointer = step_181;
            break;
        case 82:
            pointer = step_182;
            break;
        case 83:
            pointer = step_183;
            break;
        case 86:
            pointer = step_186;
            break;
        case 87:
            pointer = step_187;
            break;
        case 88:
            pointer = step_188;
            break;
        case 89:
            pointer = step_189;
            break;
        default:
            break;
    }
    
    return pointer;
}

int get_city_code(const char *input)
{
    if(input == NULL || strlen(input) < 7) {
        return CITY_UNSUPPORT;
    }
    
    char s_category[3];
    char s_index[10];
    memcpy(s_category, input + 1, 2);
    memcpy(s_index, input + 3, 4);
    unsigned int category = atoi(s_category);
    unsigned int index = atoi(s_index);
    
	int index_value = index / STEP;
	int index_step = index % STEP;
    
//    printf("%d  %d\n", category, index);
    
    unsigned int *step_data = (unsigned int*)get_step_pointer(category);
    if(step_data == NULL) {
        return CITY_UNSUPPORT;
    }
//    printf("index_value: %d, index_step:%d\n", index_value, index_step);
	unsigned int step_value = step_data[index_value];
	unsigned short weight_value = (step_value & 0xffff0000) >> 16;
    
    //if the weight_value is 0, then the step_value is the city_code;
	if(weight_value == 0) {
//		printf("weight is 0\n");
		return step_value;
	}
    
//	printf("step data: %x\n", step_value);
    
	short index_data_pivot = step_value & 0xffff;
    
	unsigned int step = index_data_pivot + get_1_count(weight_value, index_step);
//    printf("step %d  %d\n", index_data_pivot, step);
    unsigned char *index_data = (unsigned char*)get_index_pointer(category);
    unsigned int byte_index = step * 9 / 8;
    unsigned int start = step % 8;
        
    unsigned short left = index_data[byte_index];
    unsigned short right = index_data[byte_index + 1];
    
    unsigned short value = ((left & left_and[start]) << (1 + start)) |
            ((right & right_and[start]) >> (7 - start));
    
    return value;
}

jint Java_com_snda_youni_loc_PhoneLocUtils_getCityCode(JNIEnv *env,
        jobject thiz, jstring phone) {
    jsize phone_len = (*env)->GetStringUTFLength(env, phone);
    const jbyte *phone_chars = (*env)->GetStringUTFChars(env, phone, NULL);
    jint city_code = get_city_code((const char*)phone_chars);
    (*env)->ReleaseStringUTFChars(env, phone, phone_chars);
    return city_code;
}