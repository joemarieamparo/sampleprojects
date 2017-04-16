package com.mindandinnovation.chatapp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Lenovo ideapad on 4/15/2017.
 */

public class ValidatorUtil {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static boolean isNull(String string) {
        if (string == null) {
            return true;
        }

        if (string.length() == 0) {
            return true;
        }

        return false;
    }
}
