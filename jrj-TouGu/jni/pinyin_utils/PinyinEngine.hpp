/*
 * EngineLoader.h
 *
 *  Created on: Oct 31, 2012
 *      Author: yaochangwei
 */

#ifndef ENGINELOADER_H_
#define ENGINELOADER_H_

#include <string>
#include <vector>
#include <map>
#include <jni.h>

#include "log.hpp"

using namespace std;

#define PINYIN_COUNT 416
#define PINYIN_SIZE 7
#define PINYIN_MEM_SIZE PINYIN_COUNT * PINYIN_SIZE
#define POSITION 20902
#define MULTITONE 2633
#define CH_START 0x4e00
#define CH_END 0X9fa5

#define GET_TONE(x) (((*((char*)&x + 1)) & 0xf0) >> 4)
#define GET_REAL_OFFSET(x) (x & 0x0fff)

#define CH_START 0x4e00
#define CH_END 0X9fa5
#define EN_START 0x41
#define EN_END 0x7a
#define DIGIT_START 0x30
#define DIGIT_END 0x39
#define IS_EN_OR_DIGIT(x) (((x) >= EN_START && (x) <= EN_END) \
					|| ((x) >= DIGIT_START && (x) <= DIGIT_END)) ?\
					true : false
#define IS_CH(x) ((x) >= CH_START && (x) <= CH_END) ? true : false

#define HEAD_WEIGHT  100
#define NORMAL_WEIGHT 10
#define SUB_WEIGHT 1

#define TYPE_DIGIT 0
#define TYPE_PINYIN 1
#define TYPE_FULL_EQUALS 2

//注意，右边必须为(输入)小写, 否则出错
#define EQUALS_IGNORE_CASE(x, y) ((x) == (y) || (x) == ((y) + 32))
#define UNEQUALS_IGNORE_CASE(x, y) ((x) != (y) && (x) == ((y) + 32))
#define IS_MATCH(x) ((x) != -1)
#define HAS_NEXT_UNIT(x) ((x).p_next != NULL)

#define PHONE_PATH(x) (0xff00ff0000000000L | (x))
#define NAME_PATH(x) (0x00ff00ff00000000L | (x))

class Record;
class Unit;

/*
 * Pinyin Engine 保存所有数据以及结果
 */
 

 
class PinyinEngine {
public:
	PinyinEngine(const char *path);
	virtual ~PinyinEngine();
	char *pinyin;
	char *digit;
	unsigned short *position;
	unsigned short *multitone;
	unsigned short getUnicodeOffset(const unsigned short unicode) const;
	void initialize(const char *path);

	void insertRecord(Record* record);
	jlongArray computeDigit(JNIEnv *env,string& input) {
		return compute(env, input, TYPE_DIGIT);
	}
	jlongArray computePinyin(JNIEnv *env,string& input) {
		return compute(env, input, TYPE_PINYIN);
	}

	jlongArray computeFull(JNIEnv *env, string& input) {
		return compute(env, input, TYPE_FULL_EQUALS);
	}

	inline int getMatchType() {
		return m_match_type;
	}

	int getMatchCount();
	Record* getRecord(int i);

private:
	vector<Record*> records;
	vector<Record*> *match_results;
	map<string, vector<Record*>*> result_cache;
	jlongArray compute(JNIEnv *env, string& input, int type);

	int m_match_type;
};

class Unit {
public:
	unsigned char m_match_count; //the matcher number.
	int m_match_value;
	unsigned char m_match_tone;
	unsigned char m_multi_tone; //读音数目，如果是英文，则为0
	
	Unit(PinyinEngine *engine, const unsigned short*, int, const int, int);
	virtual ~Unit();

	void dumpString(string&);

	bool computeDigitMatchValue(const string& input) {
		resetMatchPath();
		m_match_value = getMatchValue(input, 0, TYPE_DIGIT, &m_match_count);
		cleanup(input);
		return m_match_value != -1;
	}

	bool computePinyinMatchValue(const string& input) {
		resetMatchPath();
		m_match_value = getMatchValue(input, 0, TYPE_PINYIN, &m_match_count);
		cleanup(input);
		return m_match_value != -1;
	}

	void cleanup(const string& input) {
		Unit *unit = this;
		int len = input.size();
		int i = 0;
		while (unit) {
			i += unit->m_match_count;
			if (i > len) {
				unit->m_match_count = 0;
			}
			unit = unit->p_next;
		}
	}

	//TODO 计算combo分数
	void computeComboValue() {
		Unit *unit = this;
		while (unit) {
			unit = unit->p_next;
		}
	}

	inline Unit* getNext() {
		return p_next;
	}

	inline const char* getPinyin(int tone) {

		if (!m_english.empty()) {
			return m_english.c_str();
		} else {
			return p_engine->pinyin + p_index_pointer[tone] * PINYIN_SIZE;
		}
	}

	inline const char* getMatchedPinyin() {
		return getPinyin(m_match_tone);
	}

	bool isValidUnit() {
		return is_valid;
	}

protected:
	string m_english; //英文字符串
	string m_digit; //英文字符串对应数字		
	unsigned char m_unit_index; //第几个字的索引
	unsigned short *p_index_pointer; //指向真实名字的偏移量的坐标指针。(可能对应不同数组)
	Unit *p_next;

	PinyinEngine *p_engine;

	int getMatchValue(const string& input, unsigned int index, int type,
			unsigned char *match_count);
	int getNextUnitMatchValue(const string &input, unsigned int index, int type,
			unsigned char *match_count) {
		return p_next->getMatchValue(input, index, type, match_count);
	}

	void resetMatchPath() {
		Unit *unit = this;
		while (unit) {
			unit->m_match_count = 0;
			unit = unit->p_next;
		}
	}

	bool is_valid; //是否为有效的单元。
};

class Record: public Unit {
public:
	string m_display_name; //全名
	string m_phone_number; //电话号码
	int m_match_len; //电话号码匹配或者全匹配的长度。通常为输入的长度
	int m_cursor_index;
	int m_unit_count; //字的个数

	Record(PinyinEngine *engine, const unsigned short* unicodes, const int sum,
			const char* display_name, const char* phone, int cursor_index) :
			Unit(engine, unicodes, 0, sum, 0) {
		m_display_name = display_name;
		m_cursor_index = cursor_index;
		m_unit_count = 0;
		if (phone != NULL) {
			m_phone_number = phone;
		}

		if (display_name != NULL) {
			m_display_name = display_name;
		}
		
		
		
		
		m_unit_count = 0;
		Unit *p = this;
		while(p) {
			m_unit_count++;
			p = p->getNext();
		}
	
	}
	
	virtual ~Record();

	jstring getPhone(JNIEnv *env);
	jobjectArray getPinyin(JNIEnv *env, long long tag);
	jintArray getMatchedPath(JNIEnv *env);


	bool computeFullMatchValue(const string& input);

	bool computeDigitMatchValue(const string& input);
	
	long long getMatchPath();
	long long getPhonePath();
	long long getNamePath();
};

class PinyinTool {
public:
	PinyinTool();
	~PinyinTool();
	jobjectArray getPinyinArray(JNIEnv *env, jstring input);
	jobject getPinyinParts(JNIEnv *env, jstring input);
private:
	char *pinyin;
	char *digit;
	unsigned short *position;
	unsigned short *multitone;
	char* getPinyin(const unsigned short unicode);
	
};


#endif /* ENGINELOADER_H_ */
