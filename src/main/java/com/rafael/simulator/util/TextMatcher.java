package com.rafael.simulator.util;

import java.util.regex.Pattern;

/** Keyword matching helpers. Whole-word matching avoids false positives such as "ja" in "janela". */
public final class TextMatcher {

    private TextMatcher() {
    }

    public static boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsWord(String text, String word) {
        Pattern pattern = Pattern.compile("(?<![\\p{L}\\p{N}])" + Pattern.quote(word) + "(?![\\p{L}\\p{N}])");
        return pattern.matcher(text).find();
    }
}
