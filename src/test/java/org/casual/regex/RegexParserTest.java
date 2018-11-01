package org.casual.regex;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

/**
 * @author miaomuzhi
 * @since 2018/10/31
 */
public class RegexParserTest {

    public RegexParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new RegexParser();
    }

    @Test
    public void regexToNFA() throws Exception{
        Method method = RegexParser.class.getDeclaredMethod("splitRegex", String.class);
        method.setAccessible(true);
        assertEquals("[·, a, (a|b)*]", method.invoke(parser, "a·(a|b)*").toString());
        assertEquals("[*, (a|b)]", method.invoke(parser, "(a|b)*").toString());
        assertEquals("[|, a, b]", method.invoke(parser, "(a|b)").toString());
        assertEquals("[*, ((|a)·b*)]", method.invoke(parser, "((|a)·b*)*").toString());
        assertEquals("[·, (|a), b*]", method.invoke(parser, "((|a)·b*)").toString());
        assertEquals("[|, , a]", method.invoke(parser, "(|a)").toString());
    }
}