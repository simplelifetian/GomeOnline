/*
 * EngineLoader.cpp
 *
 *  Created on: Oct 31, 2012
 *      Author: yaochangwei
 */
#include "stdlib.h"
#include "stdio.h"
#include <algorithm>

#include "PinyinEngine.hpp"

using namespace std;

#define DEBUG 1
#define TAG "pinyin_search"

#include "log.hpp"

PinyinEngine::PinyinEngine(const char *path) {
	initialize(path);
}

PinyinEngine::~PinyinEngine() {
	E("PinyinEngine start dealloc");

	//删除分配的数组
	delete[] pinyin;
	delete[] digit;
	delete[] position;
	delete[] multitone;

	//将Record都删除
	vector<Record*>::iterator iter;
	for (iter = records.begin(); iter != records.end(); iter++) {
		delete *iter;
	}
	records.clear();

	//将缓存的结果集合清空。
	//map<string, vector<Record*>*>::iterator map_iter;
	//for (map_iter = result_cache.begin(); map_iter != result_cache.end();
	//		map_iter++) {
	//	map_iter->second->clear();
	//	delete map_iter->second;
	//}
	//result_cache.clear();
	match_results = NULL;
	E("PinyinEngine start dealloc out");
}

void PinyinEngine::initialize(const char *path) {

	//initialize the memory.
	pinyin = new char[PINYIN_MEM_SIZE];
	digit = new char[PINYIN_MEM_SIZE];
	position = new unsigned short[POSITION];
	multitone = new unsigned short[MULTITONE];

	//load file.
	FILE *file = fopen(path, "r");
	if (file == NULL) {
		E("no such file %s", path);
	}

	fread(pinyin, PINYIN_MEM_SIZE, 1, file);
	fread(digit, PINYIN_MEM_SIZE, 1, file);
	fread(position, POSITION, 2, file);
	fread(multitone, MULTITONE, 2, file);
	fclose(file);

}

unsigned short PinyinEngine::getUnicodeOffset(
		const unsigned short unicode) const {
	return position[unicode - CH_START];
}

void PinyinEngine::insertRecord(Record *record) {
	records.push_back(record);
}

bool sort_unit(const Unit *s1, const Unit *s2) {
	return s1->m_match_value > s2->m_match_value;
}

jlongArray PinyinEngine::compute(JNIEnv *env, string &input, int type) {
	//准备进行匹配的搜索集合，加快搜索结果, 为了保证路径信息正确。每次需要重新查询
	map<string, vector<Record*>*>::iterator map_iter;
	vector<Record*> *source_record = NULL;
	int size = input.size();
	if (size > 1) {
		string tmp = input.substr(1, input.size() - 1);
		if ((map_iter = result_cache.find(tmp)) != result_cache.end()) {
			source_record = map_iter->second;
		}
	}

	//如果未找到预定义搜索集合，使用大集合进行搜索
	if (source_record == NULL) {
		source_record = &records;
	}
	
	D("input is:%s", input.c_str());

	match_results = new vector<Record*>();
	vector<Record*>::iterator iter;
	D("type is : %d", type);
	m_match_type = type;
	switch (type) {
	case TYPE_DIGIT:
		D("type digit!");
		for (iter = source_record->begin(); iter != source_record->end();
				iter++) {
			if ((*iter)->computeDigitMatchValue(input)) {
				match_results->push_back(*iter);
				D("the value is %d", (*iter)->m_match_value);
			}
		}
		break;	
	case TYPE_PINYIN:
		D("type pinyin!");
		for (iter = source_record->begin(); iter != source_record->end();
				iter++) {
			D("name is: %s", (*iter)->m_display_name.c_str());
			if ((*iter)->computePinyinMatchValue(input)) {
				match_results->push_back(*iter);
			}
			D("name is: %s out", (*iter)->m_display_name.c_str());
		}
		break;
	case TYPE_FULL_EQUALS:
		for (iter = source_record->begin(); iter != source_record->end();
				iter++) {
			if ((*iter)->computeFullMatchValue(input)) {
				match_results->push_back(*iter);
			}
		}
		break;
	}

	sort(match_results->begin(), match_results->end(), sort_unit);
	result_cache[input] = match_results;
	
	int array_size = match_results->size();
	long long *paths = new long long[array_size * 2];
	int i = 0;
	for (iter = match_results->begin(); iter != match_results->end();
				iter++, i++) {
		paths[i] = (*iter)->m_cursor_index;
		W("value: %d", (*iter)->m_match_value);
		if ((*iter)->m_match_value > 0) {	
			paths[i + array_size] = (*iter)->getMatchPath();
		} else {
			paths[i + array_size] = type == TYPE_FULL_EQUALS ? (*iter)->getNamePath():(*iter)->getPhonePath();
			W("phone path: %ld", paths[i + array_size]);
		}
		
	}
	
	jlongArray longArray = env->NewLongArray(array_size * 2);
	env->SetLongArrayRegion(longArray, 0, array_size * 2, paths);
	delete paths;
	return longArray;
}

int PinyinEngine::getMatchCount() {
	return match_results == NULL ? 0 : match_results->size();
}

Record* PinyinEngine::getRecord(int i) {
	return records.at(i);
}

