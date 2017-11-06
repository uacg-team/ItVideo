package  com.itvideo.model.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PasswordGenerator {
	private static final int LENGTH = 8;
	private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
	private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String DIGITS = "0123456789";
	private static final String PUNCTUATION = "!@#*";
	private static boolean useLower = true;
	private static boolean useUpper = true;
	private static boolean useDigits = true;
	private static boolean usePunctuation = true;

	private PasswordGenerator() {
	}
	
	public static void main(String[] args) {
		System.out.println(PasswordGenerator.generate());
	}

	public static String generate() {
		StringBuilder password = new StringBuilder();
		Random random = new Random(System.nanoTime());

		List<String> charCategories = new ArrayList<>();
		if (useLower) {
			charCategories.add(LOWER);
		}
		if (useUpper) {
			charCategories.add(UPPER);
		}
		if (useDigits) {
			charCategories.add(DIGITS);
		}
		if (usePunctuation) {
			charCategories.add(PUNCTUATION);
		}

		for (int i = 0; i < LENGTH; i++) {
			String charCategory = charCategories.get(random.nextInt(charCategories.size()));
			int position = random.nextInt(charCategory.length());
			password.append(charCategory.charAt(position));
		}
		return password.toString();
	}
}
