package webFrame.util;

import java.security.Key;
import java.security.MessageDigest;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
/**
 * 加密类,已测试
 * @author Administrator
 *
 */
public final class Encrypt {
	public static final int ENCRY_STYLE_MD5 = 1;
	public static final int ENCRY_STYLE_DES = 2;

	public static String encryptString(int style, String str) throws Exception {
		String ret = "";
		if (style == 1) {
			ret = MD5EncryptWithBASE64Encoder(str);
			return ret;
		}
		if (style == 2) {
			ret = DESEncrypt(str);
			return ret;
		}
		return str;
	}

	public static String decryptString(int style, String str) throws Exception {
		String ret = "";
		if (style == 1)
			return str;
		if (style == 2) {
			ret = DESDecrypt(str);
			return ret;
		}
		return str;
	}

	private static byte[] patch(byte[] input, byte[] key, int iInput) throws Exception {
		byte[] temp = new byte[iInput + 8];
		int nInput = iInput / 2;
		int i = 0;
		for (i = 0; i < 3; i++)
			temp[i] = key[i];
		for (i = 0; i < nInput; i++)
			temp[(i + 3)] = input[i];
		for (i = 0; i < 2; i++)
			temp[(i + 3 + nInput)] = key[(i + 3)];
		for (i = 0; i < iInput - nInput; i++)
			temp[(i + 5 + nInput)] = input[(nInput + i)];
		for (i = 0; i < 3; i++)
			temp[(i + 5 + iInput)] = key[(i + 5)];
		return temp;
	}

	private static String DESEncrypt(String source) throws Exception {
		byte[] TempsourceData = source.getBytes();

		int i = TempsourceData.length;
		int k = i - i % 8 + 8;
		byte[] sourceData = new byte[k];
		for (int j = 0; j < k; j++) {
			if (j < i)
				sourceData[j] = TempsourceData[j];
			else {
				sourceData[j] = 0;
			}
		}
		KeyGenerator keyGen = KeyGenerator.getInstance("DES");
		keyGen.init(56);
		Key key = keyGen.generateKey();
		Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding", "SunJCE");

		cipher.init(1, key);
		byte[] encryptData = cipher.doFinal(sourceData);
		byte[] temp = patch(encryptData, key.getEncoded(), sourceData.length);
		return new BASE64Encoder().encode(temp);
	}

	private static byte[] detach(byte[] input, byte[] key, int iInput) throws Exception {
		byte[] temp = new byte[iInput - 8];
		int pos1 = 3;
		int pos2 = 3 + (iInput - 8) / 2;
		int pos3 = 5 + (iInput - 8) / 2;
		int pos4 = 5 + iInput - 8;
		int pos5 = iInput;
		int i = 0;
		for (i = 0; i < pos1; i++) {
			key[i] = input[i];
		}
		for (i = 0; i < pos2 - pos1; i++) {
			temp[i] = input[(pos1 + i)];
		}
		for (i = 0; i < pos3 - pos2; i++) {
			key[(i + pos1)] = input[(pos2 + i)];
		}
		for (i = 0; i < pos4 - pos3; i++) {
			temp[(pos2 - pos1 + i)] = input[(pos3 + i)];
		}
		for (i = 0; i < pos5 - pos4; i++) {
			key[(5 + i)] = input[(pos4 + i)];
		}
		return temp;
	}

	private static String DESDecrypt(String data) throws Exception {
		byte[] detachData = new BASE64Decoder().decodeBuffer(data);
		byte[] key = new byte[8];
		byte[] encrypedData = detach(detachData, key, detachData.length);

		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DES");

		KeySpec keyspec = new DESKeySpec(key);
		keyfactory = SecretKeyFactory.getInstance("DES");
		Key desKey = keyfactory.generateSecret(keyspec);
		Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding", "SunJCE");
		cipher.init(2, desKey);
		byte[] sourceData = cipher.doFinal(encrypedData);
		int i = 0;
		for (; i < sourceData.length; i++) {
			if (sourceData[i] == 0)
				break;
		}
		byte[] temp = new byte[i];
		for (int j = 0; j < i; j++) {
			temp[j] = sourceData[j];
		}
		return new String(temp);
	}

	private static String MD5EncryptWithBASE64Encoder(String password) throws Exception {
		MessageDigest messagedigest = null;
		messagedigest = MessageDigest.getInstance("MD5");
		messagedigest.update(password.getBytes());
		byte[] temp = messagedigest.digest();
		return new BASE64Encoder().encode(temp);
	}
	
	
	/*public static void main(String[] args) throws Exception {
		System.out.println( MD5EncryptWithBASE64Encoder("666666"));
		System.out.println(DESEncrypt("666666")); //每次加密密码都不一样,
		System.out.println(DESDecrypt("wSrQe/TbOYrvbYwCvD7VUg==")); //但是解密的解码都是
		System.out.println(DESDecrypt("pHUQlqeQRJc9LKGXyhlueg==")); //但是解密的解码都是
		System.out.println(decryptString(ENCRY_STYLE_DES ,"sHwNbepsw3B6cDIkrFImeg==")); //但是解密的解码都是
	}*/
}


