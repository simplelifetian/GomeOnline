/**
 @Author yaochangwei@snda.com
 Record, 每一个姓名和电话对应一条记录
 */

#include "PinyinEngine.hpp"

#include "log.hpp"

#define MERGE_TONE_AND_PATH(x, y) ((x) | (y) << 4);

#define GET_MERGED_TONE(x) ((x) & 0x0f)
#define GET_MERGED_PATH(x) ((x) >> 4))

#define TAG "pinyin_search"

Record::~Record() {
	
}

/*
 * 获取电话号码Java字符串
 */
jstring Record::getPhone(JNIEnv *env) {
	return env->NewStringUTF(m_phone_number.c_str());
}

#define EN_TO_UPPER(x) ((x) >= 'a' && (x) <= 'z') ? ((x) - 32) : (x)

/*
 * 获取拼音字符串，如果有匹配多音字
 */
jobjectArray Record::getPinyin(JNIEnv *env, long long tag) {
	string pinyin;
	Unit *unit = this;
	int i = 0;
	char *tmp = (char*)&tag;
	char pinyin_tmp[8];
	int len = m_unit_count > 8 ? 8 : m_unit_count;
	jobjectArray array = env->NewObjectArray(len, env->FindClass("java/lang/String"), 0);
	while (unit && i < 8) {
		const char* unit_pinyin = unit->getPinyin(GET_MERGED_TONE(tmp[i]));
		strcpy(pinyin_tmp, unit_pinyin);
		pinyin_tmp[0] = EN_TO_UPPER(pinyin_tmp[0]);		
		env->SetObjectArrayElement(array, i, env->NewStringUTF(pinyin_tmp));
		unit = unit->getNext();
		i++;
	}
	return array;
}

/*
 * 获取匹配的路径
 */
jintArray Record::getMatchedPath(JNIEnv *env) {
	Unit *unit = this;
	int size = 0;
	int len = 0;
	int tmp[128];

	if (this->m_match_value > 0) {
		while (unit) {
			if (unit->m_match_count) {
				tmp[size] = len;
				tmp[size + 1] = len + unit->m_match_count;
				size += 2;
			}
			len += strlen(unit->getMatchedPinyin());
			unit = unit->getNext();
		}

		jintArray intArray = env->NewIntArray(size);
		env->SetIntArrayRegion(intArray, 0, size, tmp);
		return intArray;
	} else {
		jintArray intArray = env->NewIntArray(2);
		tmp[0] = -m_match_value;
		tmp[1] = -(tmp[0] + m_match_len);
		env->SetIntArrayRegion(intArray, 0, 2, tmp);
		return intArray;
	}

}


bool Record::computeFullMatchValue(const string& input) {
		D("my name is: %s, match name is %s", m_display_name.c_str(), input.c_str());
		int match_value = m_display_name.find(input);
		if (match_value < 0) {
			return false;
		}
		
		char* tmp = (char*)m_display_name.c_str();
		
		int real_start = 0;
		for(int i = 0; i < match_value; ++i) {
			if (tmp[i] > 127) {
				i+=2;
			}
			real_start++;
		}
		
		tmp = (char*) input.c_str();
		int real_len = 0;
		for(int i = 0; i < input.size(); i++) {
			if(tmp[i] > 127) {
				i+=2;
			}
			real_len++;
		}

		m_match_value = -real_start;
		m_match_len = real_len;
		return true;
	}

	bool Record::computeDigitMatchValue(const string& input) {
		bool is_matched = Unit::computeDigitMatchValue(input);
		if (!is_matched) {
			int phone_value = m_phone_number.find(input);
			if (phone_value == string::npos) {
				return false;
			} else {
				m_match_value = -phone_value;
				m_match_len = input.size();
				return true;
			}
		}
		return is_matched;
	}

long long Record::getMatchPath() 
{	
	long long tmp = 0;
	unsigned char* a = (unsigned char*)&tmp;
	Unit *unit = this;
	int i= 0;
	while(unit && i < 8) {
		a[i] = MERGE_TONE_AND_PATH(unit->m_match_tone, unit->m_match_count);
		i++;
		unit = unit->getNext();
	}
	return tmp;
}

long long Record::getNamePath()
{
	unsigned short start = (unsigned short)-m_match_value;
	unsigned short end = (unsigned short) (start + m_match_len);
	long long tmp = NAME_PATH((end << 16 | start)); 
	return tmp;
}

long long Record::getPhonePath() {
	unsigned short start = (unsigned short)-m_match_value;
	unsigned short end = (unsigned short) (start + m_match_len);
	long long tmp = PHONE_PATH((end << 16 | start)); 
	return tmp;	
}
