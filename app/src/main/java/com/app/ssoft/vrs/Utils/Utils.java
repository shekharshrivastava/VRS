package com.app.ssoft.vrs.Utils;

import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * Created by Shekahar.Shrivastava on 02-Mar-18.
 */

public class Utils {
    public static boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

}
