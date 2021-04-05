package com.security.admin.pki.util;

import java.math.BigInteger;
import java.util.Random;

public class RandomUtil {
	
	public static BigInteger getRandomBigInteger() {
        Random rand = new Random();
        BigInteger result = new BigInteger(4, rand); // (2^4-1) = 15 is the maximum value
        return result;
    }

}
