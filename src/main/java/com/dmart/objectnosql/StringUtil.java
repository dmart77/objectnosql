package com.dmart.objectnosql;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtil {

    private static final Pattern ACCENTS_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    public static boolean contains_CI_AI(String haystack, String needle) {

        if (haystack == null || haystack.isEmpty()) {
            return false;
        }

        if (needle == null || needle.isEmpty()) {
            return false;
        }

        final String hsToCompare = removeAccents(haystack).toLowerCase();
        final String nToCompare = removeAccents(needle).toLowerCase();

        return hsToCompare.contains(nToCompare);
    }

    public static String removeAccents(String string) {
        return ACCENTS_PATTERN.matcher(Normalizer.normalize(string, Normalizer.Form.NFD)).replaceAll("");
    }
}
