package com.gome.haoyuangong.utils.next;

public class PatchUtil {
    static{
        System.loadLibrary("patch_util");
    }
    public static native int applyPatch(String oldFile, String newFile, String patchFile);
}
