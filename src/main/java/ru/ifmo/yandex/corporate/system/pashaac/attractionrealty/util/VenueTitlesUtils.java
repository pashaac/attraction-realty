package ru.ifmo.yandex.corporate.system.pashaac.attractionrealty.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for some util methods around venues
 *
 * Created by Pavel Asadchiy
 * on 23:37 26.01.18.
 */
public class VenueTitlesUtils {

    public static String titleNormalization(String title) {
        String normalizeTitle = title.replaceAll("\\s+", " ").trim();

        Pattern doubleQuotes = Pattern.compile("^(.*)\"(.*)\"(.*)$");
        Pattern singleQuotes = Pattern.compile("^(.*)'(.*)'(.*)$");

        Matcher matcher;
        matcher = doubleQuotes.matcher(normalizeTitle);
        while (matcher.find()) {
            normalizeTitle = matcher.replaceFirst("$1«$2»$3");
        }
        matcher = singleQuotes.matcher(normalizeTitle);
        while (matcher.find()) {
            normalizeTitle = matcher.replaceFirst("$1«$2»$3");
        }

        return normalizeTitle;
    }


}
