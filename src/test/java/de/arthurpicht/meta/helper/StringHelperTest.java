package de.arthurpicht.meta.helper;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringHelperTest {

    @Test
    void replaceTabOrSpaceWithSingleSpace_1() {
        String string = "test test";
        String result = StringHelper.replaceSequenceOfTabsOrSpacesWithSingleSpace(string);
        assertEquals("test test", result);
    }

    @Test
    void replaceTabOrSpaceWithSingleSpace_2() {
        String string = "testtest";
        String result = StringHelper.replaceSequenceOfTabsOrSpacesWithSingleSpace(string);
        assertEquals("testtest", result);
    }

    @Test
    void replaceTabOrSpaceWithSingleSpace_3() {
        String string = "test      test";
        String result = StringHelper.replaceSequenceOfTabsOrSpacesWithSingleSpace(string);
        assertEquals("test test", result);
    }

    @Test
    void replaceTabOrSpaceWithSingleSpace_4() {
        String string = " test      test";
        String result = StringHelper.replaceSequenceOfTabsOrSpacesWithSingleSpace(string);
        assertEquals("test test", result);
    }

    @Test
    void replaceTabOrSpaceWithSingleSpace_5() {
        String string = "test      test   ";
        String result = StringHelper.replaceSequenceOfTabsOrSpacesWithSingleSpace(string);
        assertEquals("test test", result);
    }

    @Test
    void replaceTabOrSpaceWithSingleSpace_6() {
        String string = "test\ttest";
        String result = StringHelper.replaceSequenceOfTabsOrSpacesWithSingleSpace(string);
        assertEquals("test test", result);
    }

    @Test
    void replaceTabOrSpaceWithSingleSpace_7() {
        String string = "test\t test";
        String result = StringHelper.replaceSequenceOfTabsOrSpacesWithSingleSpace(string);
        assertEquals("test test", result);
    }

    @Test
    void replaceTabOrSpaceWithSingleSpace_8() {
        String string = "\ttest\t test";
        String result = StringHelper.replaceSequenceOfTabsOrSpacesWithSingleSpace(string);
        assertEquals("test test", result);
    }

    @Test
    void replaceTabOrSpaceWithSingleSpace_9() {
        String string = "\ttest\t test2    test3";
        String result = StringHelper.replaceSequenceOfTabsOrSpacesWithSingleSpace(string);
        assertEquals("test test2 test3", result);
    }

    @Test
    void replaceTabOrSpaceWithSingleSpace_10() {
        String string = "\ttest\t test2  \t\t  test3   \t ";
        String result = StringHelper.replaceSequenceOfTabsOrSpacesWithSingleSpace(string);
        assertEquals("test test2 test3", result);
    }

    @Test
    void replaceTabOrSpaceWithSingleSpace_11() {
        String string = "";
        String result = StringHelper.replaceSequenceOfTabsOrSpacesWithSingleSpace(string);
        assertEquals("", result);
    }

    @Test
    void replaceTabOrSpaceWithSingleSpace_12() {
        String string = "  \t";
        String result = StringHelper.replaceSequenceOfTabsOrSpacesWithSingleSpace(string);
        assertEquals("", result);
    }


    @Test
    void getCols_1() {
        String string = "AAA BBB CCC";
        List<String> result = StringHelper.getColumns(string);
        assertEquals(3, result.size());
        assertEquals("AAA", result.get(0));
        assertEquals("BBB", result.get(1));
        assertEquals("CCC", result.get(2));
    }

    @Test
    void getCols_2() {
        String string = " AAA BBB  CCC   ";
        List<String> result = StringHelper.getColumns(string);
        assertEquals(3, result.size());
        assertEquals("AAA", result.get(0));
        assertEquals("BBB", result.get(1));
        assertEquals("CCC", result.get(2));
    }

    @Test
    void getCols_3() {
        String string = "";
        List<String> result = StringHelper.getColumns(string);
        assertEquals(0, result.size());
    }

    @Test
    void getCol_1() {
        String string = "AAA BBB CCC";
        assertEquals("AAA", StringHelper.getColumn(string, 0));
        assertEquals("BBB", StringHelper.getColumn(string, 1));
        assertEquals("CCC", StringHelper.getColumn(string, 2));
    }

    @Test
    void getCol_2_neg() {
        String string = "AAA BBB CCC";
        try {
            assertEquals("DDD", StringHelper.getColumn(string, 3));
            fail(IllegalArgumentException.class.getSimpleName() + " expected");
        } catch (IllegalArgumentException e) {
            // DIN
        }
    }

    @Test
    void getCol_4_neg() {
        String string = "  ";
        try {
            assertEquals("A", StringHelper.getColumn(string, 0));
            fail(IllegalArgumentException.class.getSimpleName() + " expected");
        } catch (IllegalArgumentException e) {
            // DIN
        }
    }

}