package de.arthurpicht.meta.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringHelper {

    /**
     * Trims string and replaces a sequence containing more than one space or tab with a single space.
     * New line signs are kept untouched.
     */
    public static String replaceSequenceOfTabsOrSpacesWithSingleSpace(String string) {
        string = string.trim();
        StringBuilder stringBuilder = new StringBuilder();
        boolean found = false;
        for (int i = 0; i < string.length(); i++) {
            char character = string.charAt(i);
            if (character == ' ' || character == '\t') {
                if (!found) {
                    found = true;
                    stringBuilder.append(' ');
                }
            } else {
                if (found) found = false;
                stringBuilder.append(character);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Returns a list of all containing substrings delimited by a sequence of one or more spaces or tabs (columns).
     * Leading and trailing spaces/tabs are ignored.
     */
    public static List<String> getColumns(String string) {
        string = replaceSequenceOfTabsOrSpacesWithSingleSpace(string);
        if (string.equals("")) return new ArrayList<>();
        String[] colArray = string.split(" ");
        return Arrays.asList(colArray);
    }

    /**
     * Returns the n-th substring of specified string (column) delimited by a sequence of one or more spaces or tabs.
     * A {@link IllegalArgumentException} is thrown, if specified col could not be found.
     *
     * @param string string to be queried for columns
     * @param colIndex index of column
     * @return column string
     * @throws IllegalArgumentException If specified index of column could not be found.
     */
    public static String getColumn(String string, int colIndex) {
        List<String> cols = getColumns(string);
        if (colIndex >= cols.size() || colIndex < 0) throw new IllegalArgumentException("'colIndex' out of bounds.");
        return cols.get(colIndex);
    }

}
