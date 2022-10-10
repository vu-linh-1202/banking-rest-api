package com.hcl.bankingrestapi.general.util;

import com.hcl.bankingrestapi.general.enums.GenErrorMessage;
import com.hcl.bankingrestapi.general.exception.GenBusinessException;
import org.springframework.util.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;

public class StringUtil {

    public static Long getRandomNumber(int charCount) {
        String randomNumeric;
        do {
            randomNumeric = getRandomNumberAsString(charCount);
        } while (randomNumeric.startsWith("0"));
        Long randomLong = null;
        if (StringUtils.hasText(randomNumeric)) {
            randomLong = Long.parseLong(randomNumeric);
        }
        return randomLong;
    }

    /**
     * To generate random string consisting of numeric characters.
     * Characters will be chosen from the set of characters that matches the regex class Digit.
     * The regex \p{Digit} matches all the characters that are digits.
     *
     * @param charCount
     * @return randomNumeric
     */
    public static String getRandomNumberAsString(int charCount) {
        validateCharCount(charCount);
        String randomNumeric = RandomStringUtils.randomNumeric(charCount);
        return randomNumeric;
    }

    private static void validateCharCount(int charCount) {
        if (charCount < 0) {
            throw new GenBusinessException(GenErrorMessage.VALUE_CANNOT_BE_NEGATIVE);
        }
    }
}
