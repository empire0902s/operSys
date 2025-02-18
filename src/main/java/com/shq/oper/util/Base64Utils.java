package com.shq.oper.util;

import java.util.HashMap;
import java.util.Map;

public class Base64Utils {

	private static final Map<Integer, Character> base64CharMap = new HashMap<>();
	private static final String base64CharString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	static {
		for (int i = 0; i < base64CharString.length(); i++) {
			char c = base64CharString.charAt(i);
			base64CharMap.put(new Integer(i), new Character(c));
		}
	}

	public static String encode(String origin) {
		if (origin == null) {
			return null;
		}
		if (origin.length() == 0) {
			return "";
		}
		int length = origin.length();
		String binaryString = "";
		// to binary String
		for (int i = 0; i < length; i++) {
			int ascii = origin.charAt(i);
			String binaryCharString = Integer.toBinaryString(ascii);
			while (binaryCharString.length() < 8) {
				binaryCharString = "0" + binaryCharString;
			}
			binaryString += binaryCharString;
		}

		// to base64 index
		int beginIndex = 0;
		int endIndex = beginIndex + 6;
		String base64BinaryString = "";
		String charString = "";
		while ((base64BinaryString = binaryString.substring(beginIndex, endIndex)).length() > 0) {
			// if length is less than 6, add "0".
			while (base64BinaryString.length() < 6) {
				base64BinaryString += "0";
			}
			int index = Integer.parseInt(base64BinaryString, 2);
			char base64Char = base64CharMap.get(index);
			charString = charString + base64Char;
			beginIndex += 6;
			endIndex += 6;
			if (endIndex >= binaryString.length()) {
				endIndex = binaryString.length();
			}
			if (endIndex < beginIndex) {
				break;
			}
		}
		if (length % 3 == 2) {
			charString += "=";
		}
		if (length % 3 == 1) {
			charString += "==";
		}
		return charString;
	}

	public static String decode(String encodedString) {
		if (encodedString == null) {
			return null;
		}
		if (encodedString.length() == 0) {
			return "";
		}
		// get origin base64 String
		String origin = encodedString.substring(0, encodedString.indexOf("="));
		String equals = encodedString.substring(encodedString.indexOf("="));

		String binaryString = "";
		// convert base64 string to binary string
		for (int i = 0; i < origin.length(); i++) {
			char c = origin.charAt(i);
			int ascii = base64CharString.indexOf(c);
			String binaryCharString = Integer.toBinaryString(ascii);
			while (binaryCharString.length() < 6) {
				binaryCharString = "0" + binaryCharString;
			}
			binaryString += binaryCharString;
		}
			// the encoded string has 1 "=", means that the binary string has append 2 "0"
			if (equals.length() == 1) {
				binaryString = binaryString.substring(0, binaryString.length() - 2);
			}
			// the encoded string has 2 "=", means that the binary string has append 4 "0"
			if (equals.length() == 2) {
				binaryString = binaryString.substring(0, binaryString.length() - 4);
			}

			// convert to String
			String charString = "";
			String resultString = "";
			int beginIndex = 0;
			int endIndex = beginIndex + 8;
			while ((charString = binaryString.substring(beginIndex, endIndex)).length() == 8) {
				int ascii1 = Integer.parseInt(charString, 2);
				resultString += (char) ascii1;
				beginIndex += 8;
				endIndex += 8;
				if (endIndex > binaryString.length()) {
					break;
				}
			}
			return resultString;
		}

	public static void main(String[] args) {
		String originString = "Man is distinguished, not only by his reason, but by this singular passion from other animals, which is a lust of the mind, that by a perseverance of delight in the continued and indefatigable generation of knowledge, exceeds the short vehemence of any carnal pleasure.";
		String base64String = "TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlz"
				+ "IHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1c3Qgb2Yg"
				+ "dGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0aGUgY29udGlu"
				+ "dWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdlLCBleGNlZWRzIHRo"
				+ "ZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4=";

		System.out.println("===============encode=================");
		String result = encode(originString);
		System.out.println(result);
		System.out.println(result.equals(base64String));
		System.out.println("===============decode=================");
		String decodedString = decode(base64String);
		System.out.println(decodedString);
		System.out.println(originString.equals(decodedString));
		System.out.println(decode(encode("abcdefg")));
	}
}