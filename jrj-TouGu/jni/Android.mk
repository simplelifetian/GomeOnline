LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := pinyin_utils
LOCAL_SRC_FILES := pinyin_utils/PinyinUtils.cpp pinyin_utils/PinyinEngine.cpp pinyin_utils/Unit.cpp pinyin_utils/Record.cpp pinyin_utils/PinyinTool.cpp
LOCAL_LDLIBS := -llog
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE    := phone_loc
LOCAL_SRC_FILES := phone_loc/loc.c  phone_loc/index_130.c phone_loc/index_131.c phone_loc/index_132.c phone_loc/index_133.c phone_loc/index_134.c\
 phone_loc/index_135.c phone_loc/index_136.c phone_loc/index_137.c phone_loc/index_138.c phone_loc/index_139.c\
 phone_loc/index_145.c phone_loc/index_147.c phone_loc/index_150.c phone_loc/index_151.c phone_loc/index_152.c\
 phone_loc/index_153.c phone_loc/index_155.c phone_loc/index_156.c phone_loc/index_157.c phone_loc/index_158.c\
 phone_loc/index_159.c phone_loc/index_168.c phone_loc/index_175.c phone_loc/index_176.c phone_loc/index_177.c\
 phone_loc/index_180.c phone_loc/index_181.c phone_loc/index_182.c phone_loc/index_183.c phone_loc/index_186.c\
 phone_loc/index_187.c phone_loc/index_188.c phone_loc/index_189.c phone_loc/step_130.c phone_loc/step_131.c\
 phone_loc/step_132.c phone_loc/step_133.c phone_loc/step_134.c phone_loc/step_135.c phone_loc/step_136.c\
 phone_loc/step_137.c phone_loc/step_138.c phone_loc/step_139.c phone_loc/step_145.c phone_loc/step_147.c\
 phone_loc/step_150.c phone_loc/step_151.c phone_loc/step_152.c phone_loc/step_153.c phone_loc/step_155.c\
 phone_loc/step_156.c phone_loc/step_157.c phone_loc/step_158.c phone_loc/step_159.c phone_loc/step_168.c\
 phone_loc/step_175.c phone_loc/step_176.c phone_loc/step_177.c phone_loc/step_180.c phone_loc/step_181.c\
 phone_loc/step_182.c phone_loc/step_183.c phone_loc/step_186.c phone_loc/step_187.c phone_loc/step_188.c\
 phone_loc/step_189.c
LOCAL_LDLIBS := -llog
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE    := patch_util
LOCAL_SRC_FILES := patch_util/patch_util.c \
                	patch_util/blocksort.c \
                    patch_util/bzip2.c \
                    patch_util/bzlib.c \
                    patch_util/compress.c \
                    patch_util/crctable.c \
                    patch_util/decompress.c \
                    patch_util/huffman.c \
                    patch_util/randtable.c \
                    
LOCAL_LDLIBS := -lm -llog
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE    := appinfo
LOCAL_SRC_FILES := security/security.c security/sha256.c
LOCAL_LDLIBS := -llog
include $(BUILD_SHARED_LIBRARY)


include $(CLEAR_VARS)

LOCAL_MODULE := libaudioencoding
LOCAL_SRC_FILES := prebuild/$(TARGET_ARCH_ABI)/libaudioencoding.so

include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE := libencoding
LOCAL_SRC_FILES := prebuild/$(TARGET_ARCH_ABI)/libencoding.so

include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE := libffmpeginvoke
LOCAL_SRC_FILES := prebuild/$(TARGET_ARCH_ABI)/libffmpeginvoke.so

include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE := libwineplayerjni
LOCAL_SRC_FILES := prebuild/$(TARGET_ARCH_ABI)/libwineplayerjni.so

include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE := locSDK
LOCAL_SRC_FILES := prebuild/$(TARGET_ARCH_ABI)/liblocSDK3.so

include $(PREBUILT_SHARED_LIBRARY)
