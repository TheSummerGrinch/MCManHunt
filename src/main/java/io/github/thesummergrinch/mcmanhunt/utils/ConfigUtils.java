package io.github.thesummergrinch.mcmanhunt.utils;

public abstract class ConfigUtils {

    public static boolean isBoolean(final String arg) {
        return arg.equalsIgnoreCase("true")
                || arg.equalsIgnoreCase("false");
    }

    public static boolean isBoolean(final Object arg) {
//        if (arg instanceof Boolean) return true;
//        if (arg instanceof String) return isBoolean((String) arg);
//        return false;

        return arg instanceof Boolean
                || (arg instanceof String
                && (((String) arg).equalsIgnoreCase("true")
                || ((String) arg).equalsIgnoreCase("false")));
    }

}
