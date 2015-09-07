package com.dmart.objectnosql;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilTest {

    @Test
    public void testContains_CI_AI_haystack_null() throws Exception {
        Assert.assertFalse(StringUtil.contains_CI_AI(null, "zac"));
    }

    @Test
    public void testContains_CI_AI_needle_null() throws Exception {
        Assert.assertFalse(StringUtil.contains_CI_AI("zac", null));
    }

    @Test
    public void testContains_CI_AI() throws Exception {
        String withAccents = "ąčęėįšųūžĄČĘĖĮŠŲŪŽ";
        Assert.assertTrue(StringUtil.contains_CI_AI(withAccents, "zac"));
    }

    @Test
    public void testRemoveAccents() throws Exception {
        String withAccents = "ąčęėįšųūžĄČĘĖĮŠŲŪŽ";
        Assert.assertEquals(StringUtil.removeAccents(withAccents), "aceeisuuzACEEISUUZ");
    }
}