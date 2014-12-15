/*
 * Unit.cpp
 *  字单元，可能为一个英文单词
 *  Created on: Oct 31, 2012
 *      Author: Yaochangwei
 */
#include <cstring>
#include "PinyinEngine.hpp"

#include "log.hpp"

#define TAG "pinyin_search"

static inline char get_digit(const char x) {
	switch (x) {
	case 'a':
	case 'b':
	case 'c':
	case 'A':
	case 'B':
	case 'C':
		return '2';
	case 'd':
	case 'e':
	case 'f':
	case 'D':
	case 'E':
	case 'F':
		return '3';
	case 'g':
	case 'h':
	case 'i':
	case 'G':
	case 'H':
	case 'I':
		return '4';
	case 'j':
	case 'k':
	case 'l':
	case 'J':
	case 'K':
	case 'L':
		return '5';
	case 'm':
	case 'n':
	case 'o':
	case 'M':
	case 'N':
	case 'O':
		return '6';
	case 'p':
	case 'q':
	case 'r':
	case 's':
	case 'P':
	case 'Q':
	case 'R':
	case 'S':
		return '7';
	case 't':
	case 'u':
	case 'v':
	case 'T':
	case 'U':
	case 'V':
		return '8';
	case 'w':
	case 'x':
	case 'y':
	case 'z':
	case 'W':
	case 'X':
	case 'Y':
	case 'Z':
		return '9';
	default:
		return x;
	}
}

Unit::Unit(PinyinEngine *engine, const unsigned short *unicodes, int index,
		const int sum, int unit_index) {
	m_unit_index = unit_index++;
	p_engine = engine;
	p_next = NULL;
	m_multi_tone = 0;
	m_match_tone = 0;
	m_match_count = 0;
	
	is_valid = false;
	if (!sum) {
		is_valid = true;
		return;
	}
	unsigned short unicode = unicodes[index];
	while (!(IS_EN_OR_DIGIT(unicode)) && !IS_CH(unicode)) {
		if (index == sum - 1) {
			break;
		}
		index++;
		unicode = unicodes[index];
	}

	if (IS_EN_OR_DIGIT(unicode)) {
		is_valid = true;
		int i = 0;
		while ((IS_EN_OR_DIGIT(unicode)) && (index < sum)) {
			m_english.insert(i, 1, (char) unicode);
			m_digit.insert(i++, 1, get_digit((char) unicode));
			index++;
			unicode = unicodes[index];
		}
		index--;
		m_multi_tone = 1;
	} else if (IS_CH(unicode)) {
		is_valid = true;
		if (engine == NULL) {
			E("my god, engine is NULL");
		}
		unsigned short offset = engine->getUnicodeOffset(unicode);
		m_multi_tone = GET_TONE(offset);
		offset = GET_REAL_OFFSET(offset);
		if (m_multi_tone) {
			p_index_pointer = engine->multitone + offset;
		} else {
			p_index_pointer = engine->position + (unicode - CH_START);
			m_multi_tone = 1;
		}
	}

	if (index != sum - 1 && index < 8) {
		index++;
		p_next = new Unit(engine, unicodes, index, sum, unit_index);
		if (!p_next->is_valid) {
			delete p_next;
			p_next = NULL;
		}
	}
}

Unit::~Unit() {	
	W("Unit delete in!");
	p_index_pointer = NULL;
	if(p_next){
		delete p_next;
	}
	
	W("Unit delete out!");
	
	
}

void Unit::dumpString(string& tmp) {
	if (!m_english.empty()) {
		tmp += "<" + m_english + ">";
	} else {
		tmp += "(";
		for (int i = 0; i < m_multi_tone; i++) {
			tmp += (p_engine->pinyin + p_index_pointer[i] * PINYIN_SIZE);
			if (i != m_multi_tone - 1) {
				tmp += " ";
			}
		}
		tmp += ")";
	}

	if (p_next != NULL) {
		p_next->dumpString(tmp);
	}

	if (!m_unit_index) {
		D(tmp.c_str());
	}
}

/**
 * 获取匹配度
 */
int Unit::getMatchValue(const string& input, unsigned int index, int type,
		unsigned char *match_count) {
	//D("%s", tmp.c_str());
	
	D("Unit: input->%s  index:%d  type%d", input.c_str(), index, type);
	const char * p = input.c_str() + index;
	int size = input.size() - index;

	//如果index已经越接，说明已经完全匹配完毕，直接返回
	if (size <= 0) {
		return 0;
	}

	//初始化最大分数为-1，代表不匹配
	int max_value = -1;
	unsigned char next_match_count = 0;

	for (int i = 0; i < m_multi_tone; i++) {

		const char* name;

		//初始化用来匹配的字符串
		if (type == TYPE_DIGIT) {
			if (!m_digit.empty()) {
				name = m_digit.c_str();
			} else {
				name = p_engine->digit + p_index_pointer[i] * PINYIN_SIZE;
			}
		} else {
			if (!m_english.empty()) {
				name = m_english.c_str();
			} else {
				name = p_engine->pinyin + p_index_pointer[i] * PINYIN_SIZE;
			}
		}

		int len = strlen(name);

		//如果此字无读音或者首字母不匹配，从下一个开始匹配
		if ((!len || ((name[0] != p[0]) && (name[0] + 32 != p[0])))) {
			continue;
		}

		//赋予首字母权重
		int value = NORMAL_WEIGHT * (10 - m_unit_index);
		value = value > 0 ? value : NORMAL_WEIGHT;
		if (index == input.size() - 1) {
			if (value > max_value) {
				*match_count = 1;
				m_match_tone = i;
				max_value = value;
				D("i return immiedately!!!");
				return max_value;
			}
			continue;
		} else if (p_next) {
			int next_unit_value = this->getNextUnitMatchValue(input, index + 1,
					type, &next_match_count);
			if (IS_MATCH(next_unit_value)) {
				int tmp_value = value + next_unit_value;
				if (tmp_value > max_value) {
					*match_count = 1;
					m_match_tone = i;
					max_value = tmp_value;
					this->getNext()->m_match_count = next_match_count;
				}
			}
		}

		//为内部字母算权重
		int j;
		for (j = 1; j < len && j < size; j++) {
			if (name[j] == p[j] || name[j] + 32 == p[j]) {
				value += SUB_WEIGHT;

				if (index + j == input.size() - 1) {
					if (value > max_value) {
						*match_count = 1 + j;
						m_match_tone = i;
						max_value = value;
					}
					return max_value;
				} else if (p_next) {
					int next_unit_value = this->getNextUnitMatchValue(input,
							index + j + 1, type, &next_match_count);
					if (IS_MATCH(next_unit_value)) {
						int tmp_value = value + next_unit_value;
						if (tmp_value > max_value) {
							*match_count = 1 + j;
							this->getNext()->m_match_count = next_match_count;
							m_match_tone = i;
							max_value = tmp_value;
						}
					}
				}
			} else {
				break;
			}
		}

	}
	
	if (index != (input.size() - 1) && p_next) {
				int next_unit_value = this->getNextUnitMatchValue(input, index,
						type, &next_match_count);
				if (next_unit_value != -1) {
					max_value = next_unit_value;
					this->getNext()->m_match_count = next_match_count;
				}
			}
	
	D("max_value %d", max_value);

	return max_value;
}



