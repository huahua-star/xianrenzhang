package TTCEPackage;

import com.sun.jna.*;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;

public class Adel {

    public interface CLibrary extends Library {
        // DLL文件默认路径为项目根目录，若DLL文件存放在项目外，请使用绝对路径。（此处：(Platform.isWindows()?"msvcrt":"c")指本地动态库msvcrt.dll）
        CLibrary INSTANCE = (CLibrary) Native.loadLibrary("C:\\Users\\HTWD\\Desktop\\仙人掌\\MainDll.dll",
                CLibrary.class);

        // 声明将要调用的DLL中的方法,可以是多个方法(此处示例调用本地动态库msvcrt.dll中的printf()方法)
        int Init(int software, String server, String username, int port, int Encoder, int TMEncoder);

        int NewKey(String room, String gate, String stime, String guestname, String guestid, int overflag,int Breakfast, long cardno, String track1, String track2);

        int DupKey(String room, String gate, String stime, String guestname, String guestid, int overflag,int Breakfast, long cardno, String track1, String track2);

        int ReadCard(Pointer room, String gate, Pointer stime, String guestname,
                            String guestId, String track1, String trck2, LongByReference cardno, int st,int Breakfast);

         int CheckOut(String room, LongByReference cardno);
    }


}
