package org.jeecg.modules.zzj.common.Invoiqrutil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * 扫码util
 * 
 * @author LIUCHONG 2017年12月11日上午9:27:43
 */
public class ScanCodeUtil {
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };

	/**
	 * MD5 密码加密
	 * 
	 * @param str 加密字符串
	 * @return
	 */
	public static String encode(String str) {
		String result = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 加密后的字符串
			result = byteArrayToHexString(md.digest(str.getBytes("utf-8")));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 转换字节数组为16进制字串
	 * 
	 * @param b 字节数组
	 * @return 16进制字串
	 */
	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString().toUpperCase();
	}

	/**
	 * 转换字符
	 * 
	 * @param b 字节
	 * @return
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * 根据基本信息生成动态二维码字符串
	 * 
	 * @param infor
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encodeDynamicQR(String infor) throws UnsupportedEncodingException {
		// String origin = "3702129877463556
		// 3#10322001#2018010284747{2001#175.70,2002#54.40}#1234";
		StringBuilder stringBuilder = new StringBuilder(infor);
		stringBuilder.append("#");
		stringBuilder.append(ScanCodeUtil.encode(infor));
		System.out.print("开票密文" + stringBuilder);
		String base64Str = Base64.getEncoder().encodeToString(stringBuilder.toString().getBytes("utf-8"));
		return URLEncoder.encode(base64Str, "utf-8");
	}

	public static void main(String[] args) {
		/**
		 * String s="911401096991448160#12345678787#{111111,7,8,9}#2018-01-31
		 * 23:59:59#"; String
		 * qrInfor="OTExNDAxMDk2OTkxNDQ4MTYwIzEyMzQ1Njc4Nzg3I3sxMTExMTEsNyw4LDl9IzIwMTgtMDEtMzEgMjM6NTk6NTkjIzJEMjE3MDkyNkQ0NkRGNDJERDZBQzNCRDE3RUFCREVE";
		 * try { decodeDynamicQR(qrInfor); } catch (UnsupportedEncodingException e) { //
		 * Auto-generated catch block e.printStackTrace(); }
		 **/
		String s1 = "#wangxc#201810098062546343{1002#127,1003#2}#50AB160CB4179709FD597FF876C4379E";
		try {
			System.out.println("http://www.chinaeinv.com/scancode/qr/" + encodeDynamicQR(s1));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
