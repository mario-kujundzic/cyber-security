package com.security.admin.pki.util;

import java.math.BigInteger;
import java.util.Random;

public class RandomUtil {
	
	public static BigInteger getRandomBigInteger() {
        Random rand = new Random();
        int len = 15;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i != len; i++) {
        	sb.append(rand.nextInt(10));
        }
        String resultString = sb.toString();
        if (resultString.charAt(0) == '0') {
        	resultString = "1" + resultString.substring(1, len);
        }
        BigInteger result = new BigInteger(resultString);
        return result;
    }

}
