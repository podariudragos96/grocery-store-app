package utils;

import java.util.Collections;
import java.util.List;

public class StringUtils {

    public static String extractNumber(final String str) {

        if (str == null || str.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        boolean found = false;
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                sb.append(c);
                found = true;
            } else if (found) {
                // If we already found a digit before and this char is not a digit, stop looping
                break;
            }
        }

        return sb.toString();
    }

    public static boolean doStringListsMatch(List<String> firstList, List<String> secondList) {
        Collections.sort(firstList);
        Collections.sort(secondList);

        return firstList.equals(secondList);
    }

}
