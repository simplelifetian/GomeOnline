#include "PinyinEngine.hpp"

#define ASSET_PATH "/data/data/com.snda.youni/files/pinyin.db"
#define EN_TO_LOWER(x) ((x) >= 'A' && (x) <= 'Z') ? ((x) + 32) : (x)
PinyinTool::PinyinTool() {
	//initialize the memory.
	pinyin = new char[PINYIN_MEM_SIZE];
	digit = new char[PINYIN_MEM_SIZE];
	position = new unsigned short[POSITION];
	multitone = new unsigned short[MULTITONE];
	FILE *file = fopen(ASSET_PATH, "r");
	if (file == NULL) {
		E("no such file %s", ASSET_PATH);
	}
	fread(pinyin, PINYIN_MEM_SIZE, 1, file);
	fread(digit, PINYIN_MEM_SIZE, 1, file);
	fread(position, POSITION, 2, file);
	fread(multitone, MULTITONE, 2, file);
	fclose(file);
}

PinyinTool::~PinyinTool() {
	delete[] pinyin;
	delete[] digit;
	delete[] position;
	delete[] multitone;
}

char* PinyinTool::getPinyin(const unsigned short unicode) {
	int offset = position[unicode - CH_START];
	I("offset: %d\n", offset);
	int m_multi_tone = GET_TONE(offset);
	offset = GET_REAL_OFFSET(offset);
	I("real offset: %d\n", offset);
	unsigned short* p_index_pointer;
	if (m_multi_tone) {
		p_index_pointer = multitone + offset;
	} else {
		p_index_pointer = position + (unicode - CH_START);
	}
	return pinyin + p_index_pointer[0] * PINYIN_SIZE;
}


static int isUtfByte(char utf8) {
	switch (utf8 >> 4) {
	case 0x08:
	case 0x09:
	case 0x0a:
	case 0x0b:
	case 0x0f:
		return 0;
	}
	return 1;
}
jobjectArray PinyinTool::getPinyinArray(JNIEnv *env, jstring input) {
	jobjectArray strings = env->NewObjectArray(2, env->FindClass("java/lang/String"), 
							env->NewStringUTF(""));
	if(input == NULL) {
		return strings;
	}
	D("PinyinTool::getPinyinArray begin");
	
	const jchar *unicodes = env->GetStringChars(input, NULL);
	const jsize size = env->GetStringLength(input);

	string result1;
	string result2;
	int i = 0;
	int j = 0;
	bool is_last_ch_or_space = true;
	char three = 0;
	for(i = 0; i < size; i++) {
		unsigned short unicode = unicodes[i];
		D("unicode: %d", unicode);
		if(IS_CH(unicode)){			
			char* unit_tone = getPinyin(unicode);
			if(strlen(unit_tone) > 0) {			
				result1 += unit_tone;
				result2 += unit_tone[0];
			}
			is_last_ch_or_space = true;
		}else{
			if (((char)unicode) == ' ') {
				is_last_ch_or_space = true;
				continue;
			}

			three = 0;
			unsigned char utf8 = (char) unicode;
			switch (utf8 >> 4) {
			case 0x00:
			case 0x01:
			case 0x02:
			case 0x03:
			case 0x04:
			case 0x05:
			case 0x06:
			case 0x07:
				// Bit pattern 0xxx. No need for any extra bytes.
				D("is 0xxx, add: %c", utf8);
				result1 += EN_TO_LOWER(utf8);
				if (is_last_ch_or_space) {
					result2 += EN_TO_LOWER(utf8);
				}
				break;
			case 0x08:
			case 0x09:
			case 0x0a:
			case 0x0b:
			case 0x0f:
				/*
				 * Bit pattern 10xx or 1111, which are illegal start bytes.
				 * Note: 1111 is valid for normal UTF-8, but not the
				 * modified UTF-8 used here.
				 */
				D("is 10xx or 1111, add: ?");
				result1 += EN_TO_LOWER('?');
				if (is_last_ch_or_space) {
					result2 += EN_TO_LOWER('?');
				}
				break;
			case 0x0e:
				// Bit pattern 1110, so there are two additional bytes.
				D("is 1110");
				i++;
				utf8 = (char) unicodes[i];
				D("next: %c", utf8);
				if ((utf8 & 0xc0) != 0x80) {
					i--;
					result1 += EN_TO_LOWER('?');
					if (is_last_ch_or_space) {
						result2 += EN_TO_LOWER('?');
					}
					D("add: ?");
					break;
				}
				three = 1;
				// Fall through to take care of the final byte.
			case 0x0c:
			case 0x0d:
				// Bit pattern 110x, so there is one additional byte.
				if (!three) {
					D("is 110x");
				}
				i++;
				utf8 = (char) unicodes[i];
				D("next: %c", utf8);
				if ((utf8 & 0xc0) != 0x80) {
					i--;
					if (three) {
						i--;
					}
					result1 += EN_TO_LOWER('?');
					if (is_last_ch_or_space) {
						result2 += EN_TO_LOWER('?');
					}
					D("add: ?");
					break;
				}
				if (three) {
					utf8 = (char) unicodes[i - 2];
					result1 += EN_TO_LOWER(utf8);
					if (is_last_ch_or_space) {
						result2 += EN_TO_LOWER(utf8);
					}
					D("add: %c", utf8);
				}

				utf8 = (char) unicodes[i - 1];
				result1 += EN_TO_LOWER(utf8);
				if (is_last_ch_or_space) {
					result2 += EN_TO_LOWER(utf8);
				}
				D("add: %c", utf8);

				utf8 = (char) unicodes[i];
				result1 += EN_TO_LOWER(utf8);
				if (is_last_ch_or_space) {
					result2 += EN_TO_LOWER(utf8);
				}
				D("add: %c", utf8);
				break;
			}

//			if(isUtfByte((char)unicode)) {
//				result1 += EN_TO_LOWER((char)unicode);
//			}
			
//			if (is_last_ch_or_space) {
//				result2 += EN_TO_LOWER((char)unicode);
//			}
			is_last_ch_or_space = false;

		}	
	}
	D("result1: %s", result1.c_str());
	D("result2: %s", result2.c_str());
	
	env->SetObjectArrayElement(strings, 0, env->NewStringUTF(result1.c_str()));
	env->SetObjectArrayElement(strings, 1, env->NewStringUTF(result2.c_str()));
	env->ReleaseStringChars(input, unicodes);
	D("PinyinTool::getPinyinArray end");
	return strings;
}

jobject PinyinTool::getPinyinParts(JNIEnv *env, jstring input)
{
	jclass cls = env->FindClass("java/util/ArrayList");
	jmethodID constructor= env->GetMethodID(cls, "<init>", "()V");
	jobject arrayList = env->NewObject(cls, constructor);
	if(input == NULL) {
		return arrayList;
	}	
	
	const jchar *unicodes = env->GetStringChars(input, NULL);
	const jsize size = env->GetStringLength(input);
	   
	
	jmethodID add = env->GetMethodID(cls, "add", "(Ljava/lang/Object;)Z");
	
	int i = 0;
	bool last_is_ch = false;
	string not_ch_str;
	for(i = 0; i < size; i++) {
		unsigned short unicode = unicodes[i];
		if(IS_CH(unicode)){	
			if(!not_ch_str.empty()) {
				env->CallBooleanMethod(arrayList, add, env->NewStringUTF(not_ch_str.c_str()));
				not_ch_str.clear();
			}
			
			char* unit_tone = getPinyin(unicode);
			jstring tmp;
			if(strlen(unit_tone) > 0) {			
				tmp = env->NewStringUTF(unit_tone);
				env->CallBooleanMethod(arrayList, add, tmp);
			} else {
				tmp = env->NewStringUTF("");
				env->CallBooleanMethod(arrayList, add, tmp);
			}
			env->DeleteLocalRef(tmp);
		}else{
			if(((char)unicode) == ' ') {
				if(!not_ch_str.empty()) {
					jstring tmp = env->NewStringUTF(not_ch_str.c_str());
					env->CallBooleanMethod(arrayList, add, tmp);
					not_ch_str.clear();
					env->DeleteLocalRef(tmp);
				}
				continue;
			}
					
			if(isUtfByte((char)unicode)) {
				not_ch_str += EN_TO_LOWER((char)unicode);
			}

		}	
	}
	
	if( !not_ch_str.empty()) {
		env->CallBooleanMethod(arrayList, add, env->NewStringUTF(not_ch_str.c_str()));
		not_ch_str.clear();
	}
	env->ReleaseStringChars(input, unicodes);
	return arrayList;
}

