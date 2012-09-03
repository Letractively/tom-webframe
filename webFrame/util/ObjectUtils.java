package webFrame.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class ObjectUtils {
	public static Object BCDToObject(String bcd) throws Exception {
		Object obj = null;
		byte[] b = bcd.getBytes();
		byte[] c = new byte[b.length / 2];
		int j = 0;
		for (int i = j = 0; i < b.length; i += 2) {
			int t1 = b[i];
			int t2 = b[(i + 1)];
			if (t1 < 0)
				t1 += 256;
			t1 = char2dec(t1);
			if (t2 < 0)
				t2 += 256;
			t2 = char2dec(t2);
			int t3 = t1 << 4 | t2;
			if (t3 >= 128)
				t3 -= 256;
			c[j] = (byte) t3;
			j++;
		}

		ByteArrayInputStream bi = new ByteArrayInputStream(c);
		ObjectInputStream oi = new ObjectInputStream(bi);
		obj = oi.readObject();
		return obj;
	}

	private static int char2dec(int ch) {
		if ((ch >= 48) && (ch <= 57))
			ch -= 48;
		else if ((ch >= 65) && (ch <= 90))
			ch -= 55;
		else if ((ch >= 97) && (ch <= 122))
			ch -= 87;
		else {
			ch = -1;
		}
		return ch;
	}

	public static String objectToBCD(Object obj) throws Exception {
		StringBuffer BCD = new StringBuffer();
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		ObjectOutputStream out = null;

		out = new ObjectOutputStream(bs);
		out.writeObject(obj);
		byte[] bcd = bs.toByteArray();
		for (int i = 0; i < bcd.length; i++) {
			int n = bcd[i];
			if (n < 0) {
				n += 256;
			}
			if (n < 16)
				BCD.append("0" + Integer.toHexString(n));
			else {
				BCD.append(Integer.toHexString(n));
			}
		}
		return BCD.toString();
	}

	public static String objToZipBCD(Object obj) throws Exception {
		String str = objectToBCD(obj);
		String s = str.toUpperCase();
		int ii = str.length();
		byte[] b = new byte[ii / 2];
		int i = 0;
		for (int j = 0; i < ii; j++) {
			int i1;
			if (s.charAt(i) - 'A' >= 0)
				i1 = s.charAt(i) - 'A' + 10;
			else
				i1 = s.charAt(i) - '0';
			int i2;
			if (s.charAt(i + 1) - 'A' >= 0)
				i2 = s.charAt(i + 1) - 'A' + 10;
			else {
				i2 = s.charAt(i + 1) - '0';
			}
			b[j] = (byte) (i1 * 16 + i2);
			i++;

			i++; // 有待验证
		}

		return new String(b);
	}
}
