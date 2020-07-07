package org.jeecg.modules.zzj.util.sdk;

public class D11 {
	static {
		System.loadLibrary("D1801_Dll");
	}

	public native int CommOpen(int paramInt);

	public native int CommOpenWithBaud(int paramInt1, int paramInt2);

	public native int CommClose(int paramInt);

	public native int GetDllVersion(int paramInt, byte[] paramArrayOfByte);

	public native int GetSysVersion(int paramInt, byte paramByte, byte[] paramArrayOfByte);

	public native int SendCmd(int paramInt1, byte paramByte, byte[] paramArrayOfByte, int paramInt2);

	public native int Query(int paramInt, byte paramByte, byte[] paramArrayOfByte);

	public native int SensorQuery(int paramInt, byte paramByte, byte[] paramArrayOfByte);

	public native int AutoTestMac(int paramInt, byte paramByte);

	public native int GetCountSum(int paramInt, byte paramByte, byte[] paramArrayOfByte);

	public native int ClearSendCount(int paramInt, byte paramByte);

	public native int ClearRecycleCount(int paramInt, byte paramByte);

	public void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String toHexString2(byte[] b, boolean Phug) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			buffer.append(toHexString2(b[i]));
			if (Phug) {
				buffer.append(" ");
			}
		}
		return buffer.toString();
	}

	public String toHexString2(byte b) {
		char[] buffer = new char[2];
		buffer[0] = Character.forDigit(b >>> 4 & 0xF, 16);
		buffer[1] = Character.forDigit(b & 0xF, 16);
		return new String(buffer);
	}

	public void CopyByte(byte[] dest, byte[] ByteArray, int offset, int length) {
		for (int i = 0; i < length; i++) {
			dest[i] = ByteArray[(i + offset)];
		}
	}

	public String ByteToString(byte[] by) {
		String str = "";
		char ch = '\000';
		for (int i = 0; by[i] != 0; i++) {
			ch = (char) by[i];
			str = str + ch;
		}
		return str;
	}
}
