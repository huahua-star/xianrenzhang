package org.jeecg.modules.zzj.common.Invoiqrutil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class FiscalCodeHelper {
	private static final boolean hexcase = false;
	private static final int chrsz = 8;

	/**
	 * 生成税控码使用的DateFormat
	 */
	private static final DateFormat fiscalDF = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	public static void main(String[] args) {
		// System.out.print(fiscalDF.format(new Date()));
		int j = 200000;
		for (int i = 0; i < j; i++) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("9137020079402488XM");
			stringBuilder.append("PT000019");
			// stringBuilder.append("百洋商城");
			stringBuilder.append(String.valueOf(i));
			stringBuilder.append("623.0");
			stringBuilder.append("20181009164010312");
			stringBuilder.append("张煜亭");
			stringBuilder.append("230415663287902901");
			System.out.println(hex_hmac_sha1(String.valueOf(i), stringBuilder.toString()));
		}
	}

	/**
	 * 税控码四位加一个空格
	 * 
	 * @param fiscalCode 税控码
	 * @return
	 */
	public static String formatFiscalCode(String fiscalCode) {
		String result = "";
		if (fiscalCode != null && fiscalCode.length() == 20) {
			for (int i = 0; i < fiscalCode.length(); i++) {
				if (i != 0 && i % 4 == 0)
					result = result + " ";
				result = result + fiscalCode.substring(i, i + 1);
			}
		}
		return result;
	}

	private static String binb2hex(int[] binarray) {
		String hex_tab = hexcase ? "0123456789ABCDEFGHIJKLMNOPQISTUVWXYZ" : "0123456789ABCDEFGHIJKLMNOPQISTUVWXYZ";
		String str = "";

		for (int i = 0; i < binarray.length * 4; i++) {
			char a = (char) hex_tab.charAt((binarray[i >> 2] >> ((3 - i % 4) * 8 + 4)) & 0xf);
			str += new Character(a).toString();
		}

		return str;
	}

	private static int[] complete216(int[] oldbin) {
		if (oldbin.length >= 16) {
			return oldbin;
		}

		int[] newbin = new int[16 - oldbin.length];

		for (int i = 0; i < newbin.length; newbin[i] = 0, i++)
			;

		return concat(oldbin, newbin);
	}

	private static int[] concat(int[] oldbin, int[] newbin) {
		int[] retval = new int[oldbin.length + newbin.length];

		for (int i = 0; i < (oldbin.length + newbin.length); i++) {
			if (i < oldbin.length) {
				retval[i] = oldbin[i];
			} else {
				retval[i] = newbin[i - oldbin.length];
			}
		}

		return retval;
	}

	private static int[] core_hmac_sha1(String key, String data) {
		key = (key == null) ? "" : key;
		data = (data == null) ? "" : data;
		int[] bkey = complete216(str2binb(key));

		if (bkey.length > 16) {
			bkey = core_sha1(bkey, key.length() * chrsz);
		}

		int[] ipad = new int[16];
		int[] opad = new int[16];

		for (int i = 0; i < 16; ipad[i] = 0, opad[i] = 0, i++)
			;

		for (int i = 0; i < 16; i++) {
			ipad[i] = bkey[i] ^ 0x36363636;
			opad[i] = bkey[i] ^ 0x5c5c5c5c;
		}

		int[] hash = core_sha1(concat(ipad, str2binb(data)), 512 + data.length() * chrsz);

		return core_sha1(concat(opad, hash), 512 + 160);
	}

	private static int[] core_sha1(int[] x, int len) {
		int size = (len >> 5);
		x = strechbinarray(x, size);
		x[len >> 5] |= 0x80 << (24 - len % 32);
		size = ((len + 64 >> 9) << 4) + 15;
		x = strechbinarray(x, size);
		x[((len + 64 >> 9) << 4) + 15] = len;

		int[] w = new int[80];
		int a = 1732584193;
		int b = -271733879;
		int c = -1732584194;
		int d = 271733878;
		int e = -1009589776;

		for (int i = 0; i < x.length; i += 16) {
			int olda = a;
			int oldb = b;
			int oldc = c;
			int oldd = d;
			int olde = e;

			for (int j = 0; j < 80; j++) {
				if (j < 16) {
					w[j] = x[i + j];
				} else {
					w[j] = rol(w[j - 3] ^ w[j - 8] ^ w[j - 14] ^ w[j - 16], 1);
				}

				int t = safe_add(safe_add(rol(a, 5), sha1_ft(j, b, c, d)), safe_add(safe_add(e, w[j]), sha1_kt(j)));

				e = d;
				d = c;
				c = rol(b, 30);
				b = a;
				a = t;
			}

			a = safe_add(a, olda);
			b = safe_add(b, oldb);
			c = safe_add(c, oldc);
			d = safe_add(d, oldd);
			e = safe_add(e, olde);
		}

		int[] retval = new int[5];

		retval[0] = a;
		retval[1] = b;
		retval[2] = c;
		retval[3] = d;
		retval[4] = e;

		return retval;
	}

	public static String hex_hmac_sha1(String key, String data) {
		return binb2hex(core_hmac_sha1(key, data));
	}

	private static int rol(int num, int cnt) {
		return (num << cnt) | (num >>> (32 - cnt));
	}

	private static int safe_add(int x, int y) {
		int lsw = (int) (x & 0xffff) + (int) (y & 0xffff);
		int msw = (x >> 16) + (y >> 16) + (lsw >> 16);

		return (msw << 16) | (lsw & 0xffff);
	}

	private static int sha1_ft(int t, int b, int c, int d) {
		if (t < 20)
			return (b & c) | ((~b) & d);

		if (t < 40)
			return b ^ c ^ d;

		if (t < 60)
			return (b & c) | (b & d) | (c & d);

		return b ^ c ^ d;
	}

	private static int sha1_kt(int t) {
		return (t < 20) ? 1518500249 : (t < 40) ? 1859775393 : (t < 60) ? -1894007588 : -899497514;
	}

	private static int[] str2binb(String str) {
		str = (str == null) ? "" : str;

		int[] tmp = new int[str.length() * chrsz];
		int mask = (1 << chrsz) - 1;

		for (int i = 0; i < str.length() * chrsz; i += chrsz) {
			tmp[i >> 5] |= ((int) (str.charAt(i / chrsz)) & mask) << (24 - i % 32);
		}

		int len = 0;
		for (int i = 0; i < tmp.length && tmp[i] != 0; i++, len++)
			;

		int[] bin = new int[len];

		for (int i = 0; i < len; i++) {
			bin[i] = tmp[i];
		}

		return bin;
	}

	private static int[] strechbinarray(int[] oldbin, int size) {
		int currlen = oldbin.length;

		if (currlen >= size + 1) {
			return oldbin;
		}

		int[] newbin = new int[size + 1];
		for (int i = 0; i < size; newbin[i] = 0, i++)
			;

		for (int i = 0; i < currlen; i++) {
			newbin[i] = oldbin[i];
		}

		return newbin;
	}
}