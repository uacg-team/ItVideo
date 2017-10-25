package  com.itvideo.model.utils;

import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;

/**
 * Hash password using SHA3-256 implemented by BouncyCastle
 */
public final class Hash {
	private static SHA3.Digest256 coder = new SHA3.Digest256();

	public static String getHashPass(String password) {
		byte[] bytes = password.getBytes();
		byte[] hashBytes = coder.digest(bytes);
		StringBuilder sb = new StringBuilder(Hex.toHexString(hashBytes));
		return sb.reverse().toString();
	}
}
