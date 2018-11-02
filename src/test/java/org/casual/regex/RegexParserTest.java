package org.casual.regex;

import org.casual.entity.NFA;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

/**
 * @author miaomuzhi
 * @since 2018/10/31
 */
public class RegexParserTest {

    private RegexParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new RegexParser();
    }

    @Test
    public void splitRegex() throws Exception{
        Method method = RegexParser.class.getDeclaredMethod("splitRegex", String.class);
        method.setAccessible(true);
        assertEquals("[·, a, (a|b)*]", method.invoke(parser, "a·(a|b)*").toString());
        assertEquals("[*, (a|b)]", method.invoke(parser, "(a|b)*").toString());
        assertEquals("[|, a, b]", method.invoke(parser, "(a|b)").toString());
        assertEquals("[*, ((|a)·b*)]", method.invoke(parser, "((|a)·b*)*").toString());
        assertEquals("[·, (|a), b*]", method.invoke(parser, "((|a)·b*)").toString());
        assertEquals("[|, , a]", method.invoke(parser, "(|a)").toString());
        assertEquals("[·, ( |a), b*]", method.invoke(parser, "(( |a)·b*)").toString());
        assertEquals("[|,  , a]", method.invoke(parser, "( |a)").toString());
        assertEquals("[·, \r,  ·\t·\n]", method.invoke(parser, "\r· ·\t·\n").toString());
        assertEquals("[·, \t, \n]", method.invoke(parser, "\t·\n").toString());
        assertEquals("[\\]", method.invoke(parser, "\\").toString());
        assertEquals("[·, \t, \n]", method.invoke(parser, "\t·\n").toString());
        assertEquals("[·, t, \\·n]", method.invoke(parser, "t·\\·n").toString());
        assertEquals("[·, \n, t·\\·n]", method.invoke(parser, "\n·t·\\·n").toString());
        assertEquals("[·, t, \\·n]", method.invoke(parser, "t·\\·n").toString());
        assertEquals("[·, \\, n]", method.invoke(parser, "\\·n").toString());
        assertEquals("[·, (a|b)*, a·(a|b)·(a|b)]", method.invoke(parser, "(a|b)*·a·(a|b)·(a|b)").toString());
    }

    @Test
    public void regexToNFA() {
        NFA nfa = parser.regexToNFA("a·(a|b)*", "first");
        assertEquals(4, nfa.calcSize());
        assertEquals(3, nfa.findById(3).getTransitions().size());
        assertEquals("a", nfa.getStart().getTransitions().get(0).getKey());

        nfa = parser.regexToNFA("((|a)·b*)*", "second");
        assertEquals(5, nfa.calcSize());
        assertEquals(3, nfa.findById(2).getTransitions().size());
        assertEquals("[Pair(key=, val=2), Pair(key=b, val=4)]", nfa.findById(4).getTransitions().toString());
        assertEquals("[Pair(key=, val=1), Pair(key=, val=3), Pair(key=a, val=3)]", nfa.findById(2).getTransitions().toString());

        nfa = parser.regexToNFA(" ·\t·\n", "delimiter");
        assertEquals(4, nfa.calcSize());
    }
}