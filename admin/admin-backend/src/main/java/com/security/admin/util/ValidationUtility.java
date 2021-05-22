package com.security.admin.util;
import com.security.admin.pki.util.PEMUtility;

import java.util.regex.*;

public class ValidationUtility {
    public static final String englishStringRegex = "^[a-zA-Z0-9_.,\\-/!@#$%^&*()=+;:'\" \n\t\r]*$";
    public static final String alphaNumericRegex = "^[a-zA-Z0-9\\-_ .:'\n\t\r]*$";
    public static final String englishAlphabetRegex = "^[a-zA-Z\\-_ '\n\t\r]*$";
    public static final String domainRegex = "^[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)$";
    public static final String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    public static final String base64Regex = "^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$";
    public static final String PEMRegex = "^[a-zA-Z0-9/\\-+ \r\n\r]*$";


    private static Pattern englishStringPattern = Pattern.compile(englishStringRegex);

    public static boolean isEnglishText(String text) {
        return englishStringPattern.matcher(text).matches();
    }
}
