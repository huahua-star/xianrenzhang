package org.jeecg.modules.zzj.util;

import TTCEPackage.Adel;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.LongByReference;

public class PhotoCompareUtil {

    public interface CLibrary extends Library {
        // DLL文件默认路径为项目根目录，若DLL文件存放在项目外，请使用绝对路径。（此处：(Platform.isWindows()?"msvcrt":"c")指本地动态库msvcrt.dll）
        PhotoCompareUtil.CLibrary INSTANCE = (PhotoCompareUtil.CLibrary) Native.loadLibrary("FaceCompare.dll",
                PhotoCompareUtil.CLibrary.class);

        // 声明将要调用的DLL中的方法,可以是多个方法(此处示例调用本地动态库msvcrt.dll中的printf()方法)
         int Init(String dllPath);

         int FaceCompare(String imgFileName);

         int UnInit();
    }
}
